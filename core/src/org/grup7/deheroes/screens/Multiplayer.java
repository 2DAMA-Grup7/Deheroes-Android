package org.grup7.deheroes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;

import org.grup7.deheroes.Vars;
import org.grup7.deheroes.actors.heroes.Hero;
import org.grup7.deheroes.actors.heroes.Witch;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Multiplayer extends SinglePlayer implements Screen {
    private final HashMap<String, Hero> remotePlayers;
    private final ArrayList<String> addPlayerQueue;
    private final HashMap<String, Vector2> updatePosQueue;
    private String id;
    private float timer;
    private Socket socket;

    public Multiplayer(Game game, String map) {
        super(game, map);
        addPlayerQueue = new ArrayList<>();
        remotePlayers = new HashMap<>();
        updatePosQueue = new HashMap<>();
        connectSocket();
        configSocketEvents();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        updateServer(delta);
        addPlayerQueue.forEach(id -> {
            Hero newPlayer = new Witch(world);
            remotePlayers.put(id, newPlayer);
            stage.addActor(newPlayer);
        });
        updatePosQueue.forEach((key, value) -> {
            if (value.equals(new Vector2(1, 0))) {
                remotePlayers.get(key).moveLeft();
            }
            //remotePlayers.get(key).getBody().setTransform(value,0);
        });
        updatePosQueue.clear();
        addPlayerQueue.clear();
    }

    @Override
    protected void actorAct(float delta) {
        super.actorAct(delta);
        remotePlayers.forEach((key, value) -> value.act(delta));
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public void configSocketEvents() {
        socket.on(Socket.EVENT_CONNECT, args -> {
            Gdx.app.log("SocketIO", "Connected");
        }).on("socketID", args -> {
            JSONObject data = (JSONObject) args[0];
            try {
                id = data.getString("id");
                Gdx.app.log("SocketIO", "My ID: " + id);
            } catch (JSONException e) {
                Gdx.app.log("SocketIO", "Error getting ID");
            }
        }).on("newPlayer", args -> {
            JSONObject data = (JSONObject) args[0];
            try {
                String playerId = data.getString("id");
                Gdx.app.log("SocketIO", "New Player Connect: " + playerId);
                addPlayerQueue.add(playerId);
            } catch (JSONException e) {
                Gdx.app.log("SocketIO", "Error getting New PlayerID");
            }
        }).on("playerDisconnected", args -> {
            JSONObject data = (JSONObject) args[0];
            try {
                id = data.getString("id");
                remotePlayers.remove(id);
            } catch (JSONException e) {
                Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
            }
        }).on("playerMoved", args -> {
            JSONObject data = (JSONObject) args[0];
            try {
                String playerId = data.getString("id");
                double x = data.getDouble("x");
                double y = data.getDouble("y");
                if (remotePlayers.get(playerId) != null) {
                    updatePosQueue.put(playerId, new Vector2((float) x, (float) y));
                }
            } catch (JSONException e) {
                Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
            }
        }).on("getPlayers", args -> {
            JSONArray objects = (JSONArray) args[0];
            try {
                for (int i = 0; i < objects.length(); i++) {
                    addPlayerQueue.add(objects.getJSONObject(i).getString("id"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateServer(float delta) {
        timer += delta;
        if (timer >= Vars.UPDATE_TIME && player != null && player.hasMoved()) {
            JSONObject data = new JSONObject();
            try {
                data.put("x", player.getVelocity().x);
                data.put("y", player.getVelocity().y);
                socket.emit("playerMoved", data);
            } catch (JSONException e) {
                Gdx.app.log("SOCKET.IO", "Error sending update data");
            }
        }
    }

    private void connectSocket() {
        try {
            socket = IO.socket(Vars.localURL);
            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

