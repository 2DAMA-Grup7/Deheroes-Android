package org.grup7.deheroes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

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
    private JSONArray mobData;
    private boolean host;
    private float timer;
    private Socket socket;

    public Multiplayer(Game game, String map) {
        super(game, map);
        host = false;
        mobData = new JSONArray();
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
            Hero player = remotePlayers.get(key);
            player.setVelocity(value);
            if (value.x == 0) player.stopX();
            else if (value.y == 0) player.stopY();
            if (value.x > 0) player.setAnimation(player.getWalkRight());
            else if (value.x < 0) player.setAnimation(player.getWalkLeft());
            if (value.y > 0) player.setAnimation(player.getWalkUp());
            else if (value.y < 0) player.setAnimation(player.getWalkDown());
        });


        updatePosQueue.clear();
        addPlayerQueue.clear();
    }

    @Override
    protected void actorAct(float delta) throws JSONException {
        player.act(delta);
        if (host) {
            allMobs.forEach(mob -> {
                if (mob.isAlive()) {
                    mob.act(delta, player);
                } else {
                    if (TimeUtils.nanoTime() - lastMobSpawn > 2000000000) {
                        mob.awake(getMobSpawnPosition());
                        lastMobSpawn = TimeUtils.nanoTime();
                    }
                }
            });
        } else {
            if (mobData.length() != 0) {
                for (int i = 0; i < allMobs.size(); i++) {
                    allMobs.get(i).setHP((float) mobData.getJSONObject(i).getDouble("health"));
                    allMobs.get(i).getBody().setTransform((float) mobData.getJSONObject(i).getDouble("x"), (float) mobData.getJSONObject(i).getDouble("y"), 0);
                    if (allMobs.get(i).getHP() > 0) {
                        allMobs.get(i).getBody().setActive(true);
                        allMobs.get(i).act(delta, player);
                    } else {
                        allMobs.get(i).getBody().setActive(false);
                    }
                }
            }
        }
        remotePlayers.forEach((key, value) -> value.act(delta));
    }

    private void updateServer(float delta) {
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
            JSONArray mobs = new JSONArray();
            allMobs.forEach(mob -> {
                try {
                    JSONObject object = new JSONObject();
                    object.put("x", player.getVelocity().x);
                    object.put("y", player.getVelocity().y);
                    object.put("health", player.getVelocity().y);
                    mobs.put(object);
                } catch (JSONException e) {
                    Gdx.app.log("SOCKET.IO", "Error sending update data");
                }
            });
            socket.emit("updateMobs", mobs);

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
                String playerId = data.getString("id");
                double x = data.getDouble("x");
                double y = data.getDouble("y");
                if (remotePlayers.get(playerId) != null) {
                    updatePosQueue.put(playerId, new Vector2((float) x, (float) y));
                }
            } catch (JSONException e) {
                Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
            }
        }).on("getMobs", args -> {
            mobData = (JSONArray) args[0];
        }).on("getPlayers", args -> {
            JSONArray objects = (JSONArray) args[0];
            try {
                for (int i = 0; i < objects.length(); i++) {
                    addPlayerQueue.add(objects.getJSONObject(i).getString("id"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
        socket.off();
        socket.disconnect();
        super.dispose();
    }
}

