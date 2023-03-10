package org.grup7.deheroes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;

import org.grup7.deheroes.Vars;
import org.grup7.deheroes.actors.heroes.Hero;
import org.grup7.deheroes.actors.heroes.Witch;
import org.grup7.deheroes.actors.mobs.Mob;
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
    private final HashMap<Integer, Vector2> updateMobsPosition;
    private boolean host;
    private float timer;
    private Socket socket;

    public Multiplayer(Game game, String map) {
        super(game, map);
        host = false;
        updateMobsPosition = new HashMap<>();
        addPlayerQueue = new ArrayList<>();
        remotePlayers = new HashMap<>();
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
        updateMobsPosition.forEach((id, position) -> allMobs.get(id).getBody().setTransform(position, 0));
        updateMobsPosition.clear();
        addPlayerQueue.clear();
    }

    @Override
    protected void actorAct(float delta) {
        super.actorAct(delta);
        remotePlayers.forEach((key, value) -> value.act(delta));
    }

    private void updateServer(float delta) {
        timer += delta;
        if (timer >= Vars.UPDATE_TIME) {
            if (player != null && player.hasMoved()) {
                JSONObject data = new JSONObject();
                try {
                    data.put("x", player.getVelocity().x);
                    data.put("y", player.getVelocity().y);
                    socket.emit("playerMoved", data);
                } catch (JSONException e) {
                    Gdx.app.log("SOCKET.IO", "Error sending update data");
                }
            }
            if (host) {
                JSONArray mobs = new JSONArray();
                allMobs.forEach(mob -> {
                    JSONObject object = new JSONObject();
                    try {
                        object.put("x", mob.getX());
                        object.put("y", mob.getY());
                        object.put("hp", mob.getHP());
                        object.put("isAlive", mob.isAlive());
                        mobs.put(object);
                    } catch (JSONException e) {
                        Gdx.app.log("SOCKET.IO", "Error sending update data");
                    }
                });
                socket.emit("updateMobs", mobs);
            }
        }
    }

    private void configSocketEvents() {
        socket.on(Socket.EVENT_CONNECT, args -> {
            Gdx.app.log("SocketIO", "Connected");
        }).on("socketID", args -> {
            JSONObject data = (JSONObject) args[0];
            try {
                String id = data.getString("id");
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
                String id = data.getString("id");
                Gdx.app.log("SocketIO", "Player Disconnected: " + id);
                remotePlayers.get(id).dispose();
                remotePlayers.remove(id);
            } catch (JSONException e) {
                Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
            }
        }).on("playerMoved", args -> {
            JSONObject data = (JSONObject) args[0];
            try {
                Vector2 velocity = new Vector2((float) data.getDouble("x"), (float) data.getDouble("y"));
                Hero player = remotePlayers.get(data.getString("id"));
                if (player != null) {
                    player.setVelocity(velocity);
                    if (velocity.x == 0 | velocity.y == 0) player.stopAnimation();
                    if (velocity.x > 0) player.setAnimation(player.getWalkRight());
                    else if (velocity.x < 0) player.setAnimation(player.getWalkLeft());
                    if (velocity.y > 0) player.setAnimation(player.getWalkUp());
                    else if (velocity.y < 0) player.setAnimation(player.getWalkDown());
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
                Gdx.app.log("SocketIO", "Error Getting Players Data");
            }
        }).on("getMobs", args -> {
            JSONArray mobs = (JSONArray) args[0];
            try {
                for (int i = 0; i < mobs.length(); i++) {
                    JSONObject remoteMob = mobs.getJSONObject(i);
                    Mob localMob = allMobs.get(i);
                    Vector2 position = new Vector2((float) remoteMob.getDouble("x"), (float) remoteMob.getDouble("y"));
                    localMob.setHP((float) remoteMob.getDouble("hp"));
                    localMob.setAlive(remoteMob.getBoolean("isAlive"));
                    updateMobsPosition.put(i, position);
                }
            } catch (JSONException e) {
                Gdx.app.log("SocketIO", "Error Getting Players Data");
            }
        }).on("host", args -> host = true);
    }

    private void connectSocket() {
        try {
            socket = IO.socket(Vars.socketURL);
            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {
        socket.close();
        super.dispose();
    }
}

