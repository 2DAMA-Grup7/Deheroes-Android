package org.grup7.deheroes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

import org.grup7.deheroes.Vars;
import org.grup7.deheroes.actors.heroes.Hero;
import org.grup7.deheroes.actors.heroes.Witch;
import org.grup7.deheroes.actors.mobs.Mob;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Multiplayer extends SinglePlayer implements Screen {
    private final HashMap<String, Hero> remotePlayers;
    private final ArrayList<String> addPlayerQueue;
    private final HashMap<Integer, Vector2> awakeMobsQueue;
    private final HashMap<Integer, Vector2> updateMobsPosition;
    private String myId;
    private float timer;
    private Socket socket;

    public Multiplayer(Game game, String map) {
        super(game, map);
        updateMobsPosition = new HashMap<>();
        addPlayerQueue = new ArrayList<>();
        remotePlayers = new HashMap<>();
        awakeMobsQueue = new HashMap<>();
        player.setHost(false);
        connectSocket();
        configSocketEvents();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        System.out.println(allMobs.get(0).getVelocity());
        updateServer(delta);
        addPlayerQueue.forEach(id -> {
            Hero newPlayer = new Witch(world);
            remotePlayers.put(id, newPlayer);
            stage.addActor(newPlayer);
        });
        try {
            updateMobsPosition.forEach((id, position) -> allMobs.get(id).getBody().setTransform(position, 0));
            awakeMobsQueue.forEach((id, position) -> allMobs.get(id).awake(position));
        } catch (ConcurrentModificationException e) {
            Gdx.app.log("Box2d", "The Body is already being modified, transform skipped");
        }
        updateMobsPosition.clear();
        awakeMobsQueue.clear();
        addPlayerQueue.clear();
    }

    @Override
    protected void actorAct(float delta) {
        // Player
        player.act(delta);
        // Mobs
        for (int i = 0; i < allMobs.size(); i++) {
            Mob mob = allMobs.get(i);
            if (mob.isAlive()) {
                mob.act(delta, player);
            } else {
                if (TimeUtils.nanoTime() - lastMobSpawn > 2000000000 && player.isHost()) {
                    Vector2 randomPos = getMobSpawnPosition();
                    mob.awake(randomPos);
                    JSONObject data = new JSONObject();
                    try {
                        data.put("id", i);
                        data.put("x", randomPos.x);
                        data.put("y", randomPos.y);
                        socket.emit("awake", data);
                    } catch (JSONException e) {
                        Gdx.app.log("SOCKET.IO", "Error awakening");
                    }
                    lastMobSpawn = TimeUtils.nanoTime();
                }
            }
        }
        remotePlayers.forEach((key, value) -> value.act(delta));
    }

    private void updateServer(float delta) {
        timer += delta;
        if (timer >= Vars.UPDATE_TIME && player != null) {
            if (player.hasMoved()) {
                JSONObject data = new JSONObject();
                try {
                    data.put("x", player.getVelocity().x);
                    data.put("y", player.getVelocity().y);
                    data.put("hp", player.getHp());
                    socket.emit("playerMoved", data);
                } catch (JSONException e) {
                    Gdx.app.log("SOCKET.IO", "Error sending player update data");
                }
            }
            if (player.isHost()) {
                for (int i = 0; i < allMobs.size(); i++) {
                    Mob mob = allMobs.get(i);
                    if (mob.hasMoved()) {
                        JSONObject data = new JSONObject();
                        try {
                            data.put("id", i);
                            data.put("x", mob.getVelocity().x);
                            data.put("y", mob.getVelocity().y);
                            data.put("hp", mob.getHP());
                            data.put("isAlive", mob.isAlive());
                            socket.emit("mobMoved", data);
                        } catch (JSONException e) {
                            Gdx.app.log("SOCKET.IO", "Error sending mob update data");
                        }
                    }
                }
            }
        }
    }

    private void dataCorrector() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                JSONArray mobs = new JSONArray();
                allMobs.forEach(mob -> {
                    JSONObject object = new JSONObject();
                    try {
                        object.put("x", mob.getX());
                        object.put("y", mob.getY());
                        object.put("hp", mob.getHP());
                        mobs.put(object);
                    } catch (JSONException e) {
                        Gdx.app.log("SOCKET.IO", "Error sending update data");
                    }
                });
                socket.emit("updateMobs", mobs);
                /* WIP
                JSONArray allSpells = new JSONArray();
                remotePlayers.forEach((key, player) -> {
                    JSONArray spellsPlayer = new JSONArray();
                    player.getAllSpells().forEach(spell -> {
                        JSONObject object = new JSONObject();
                        try {
                            object.put("x", spell.getX());
                            object.put("y", spell.getY());
                            object.put("hp", spell.getHP());
                            spellsPlayer.put(object);
                        } catch (JSONException e) {
                            Gdx.app.log("SOCKET.IO", "Error sending update data");
                        }
                    });
                    allSpells.put(spellsPlayer);
                });
                socket.emit("updateSpells", allSpells);
                */
            }
        }, 0, Vars.correctorInterval);
    }

    private void configSocketEvents() {
        socket.on(Socket.EVENT_CONNECT, args -> {
            Gdx.app.log("SocketIO", "Connected");
        }).on("host", args -> {
            player.setHost(true);
            dataCorrector();
        }).on("socketID", args -> {
            JSONObject data = (JSONObject) args[0];
            try {
                myId = data.getString("id");
                Gdx.app.log("SocketIO", "My ID: " + myId);
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
                    player.setHp((float) data.getDouble("hp"));
                    if (velocity.x == 0 | velocity.y == 0) player.stopAnimation();
                    if (velocity.x > 0) player.setAnimation(player.getWalkRight());
                    else if (velocity.x < 0) player.setAnimation(player.getWalkLeft());
                    if (velocity.y > 0) player.setAnimation(player.getWalkUp());
                    else if (velocity.y < 0) player.setAnimation(player.getWalkDown());
                }
            } catch (JSONException e) {
                Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
            }
        }).on("mobMoved", args -> {
            JSONObject data = (JSONObject) args[0];
            try {
                Mob mob = allMobs.get(data.getInt("id"));
                mob.setHP((float) data.getDouble("hp"));
                mob.setAlive(data.getBoolean("isAlive"));
                mob.setVelocity(new Vector2((float) data.getDouble("x"), (float) data.getDouble("y")));
            } catch (JSONException e) {
                Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
            }
        }).on("awakeMob", args -> {
            JSONObject data = (JSONObject) args[0];
            try {
                awakeMobsQueue.put(data.getInt("id"), new Vector2((float) data.getDouble("x"), (float) data.getDouble("y")));
            } catch (JSONException e) {
                throw new RuntimeException(e);
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
                    updateMobsPosition.put(i, position);
                }
            } catch (JSONException e) {
                Gdx.app.log("SocketIO", "Error Getting Players Data");
            }
        });
    }

    private void connectSocket() {
        try {
            socket = IO.socket(Vars.localURL);
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

