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
import org.grup7.deheroes.actors.spells.Spell;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Random;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Multiplayer extends SinglePlayer implements Screen {
    private final HashMap<String, Hero> remotePlayers;
    private final ArrayList<String> addPlayerQueue;
    private final HashMap<Integer, Vector2> awakeMobsQueue;
    private final HashMap<Integer, Vector2> updateMobsPosition;
    private final HashMap<String, ArrayList<Vector2>> updateSpellsPosition;
    private final HashMap<String, Vector2> updatePlayersPosition;
    private String myId;
    private float timer;
    private Socket socket;

    public Multiplayer(Game game, String map) {
        super(game, map);
        updateMobsPosition = new HashMap<>();
        updateSpellsPosition = new HashMap<>();
        updatePlayersPosition = new HashMap<>();
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
        updateServer(delta);
        addPlayerQueue.forEach(id -> {
            Hero newPlayer = new Witch(world);
            remotePlayers.put(id, newPlayer);
            stage.addActor(newPlayer);
        });
        try {
            updateSpellsPosition.forEach((key, spells) -> {
                if (key.equals(myId)) {
                    for (int i = 0; i < player.getAllSpells().size(); i++) {
                        Spell spell = player.getAllSpells().get(i);
                        Vector2 spellVelocity = spell.getVelocity();
                        Vector2 pos = spells.get(i);
                        spell.getBody().setTransform(pos, 0);
                        spell.getBody().setLinearVelocity(spellVelocity);
                    }
                } else {
                    for (int i = 0; i < remotePlayers.get(key).getAllSpells().size(); i++) {
                        Spell spell = remotePlayers.get(key).getAllSpells().get(i);
                        Vector2 spellVelocity = spell.getVelocity();
                        Vector2 pos = spells.get(i);
                        spell.getBody().setTransform(pos, 0);
                        spell.getBody().setLinearVelocity(spellVelocity);
                    }
                }
            });
            updatePlayersPosition.forEach((id, position) -> remotePlayers.get(id).getBody().setTransform(position, 0));
            updateMobsPosition.forEach((id, position) -> allMobs.get(id).getBody().setTransform(position, 0));
            awakeMobsQueue.forEach((id, position) -> allMobs.get(id).awake(position));
        } catch (ConcurrentModificationException e) {
            Gdx.app.log("Box2d", "The Body is already being modified, transform skipped");
        }
        updateSpellsPosition.clear();
        updatePlayersPosition.clear();
        updateMobsPosition.clear();
        awakeMobsQueue.clear();
        addPlayerQueue.clear();
    }

    private Hero closerPlayer(Vector2 distanceMob) {
        Hero closerPlayer = null;
        float smallerDistance = Float.MAX_VALUE;
        ArrayList<Hero> players = new ArrayList<>();
        players.add(player);
        remotePlayers.forEach((key, hero) -> players.add(hero));
        for (Hero player : players) {
            if (player.getPosition().dst(distanceMob) < smallerDistance) {
                smallerDistance = player.getPosition().dst(distanceMob);
                closerPlayer = player;
            }
        }
        return closerPlayer;
    }

    @Override
    protected void actorAct(float delta) {
        // Player
        player.act(delta);
        // Mobs
        for (int i = 0; i < allMobs.size(); i++) {
            Mob mob = allMobs.get(i);
            if (mob.isAlive()) {
                mob.act(delta, closerPlayer(mob.getPosition()));
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
                    data.put("posX", player.getPosition().x);
                    data.put("posY", player.getPosition().y);
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

                JSONArray allSpells = new JSONArray();
                JSONArray mySpells = new JSONArray();
                player.getAllSpells().forEach(spell -> {
                    JSONObject object = new JSONObject();
                    try {
                        object.put("x", spell.getX());
                        object.put("y", spell.getY());
                        object.put("velx", spell.getVelocity().x);
                        object.put("vely", spell.getVelocity().y);
                        object.put("hp", spell.getHP());
                        object.put("id", myId);
                        mySpells.put(object);
                    } catch (JSONException e) {
                        Gdx.app.log("SOCKET.IO", "Error sending spell update data");
                    }
                });
                allSpells.put(mySpells);
                remotePlayers.forEach((key, player) -> {
                    JSONArray spellsPlayer = new JSONArray();
                    player.getAllSpells().forEach(spell -> {
                        JSONObject object = new JSONObject();
                        try {
                            object.put("x", spell.getX());
                            object.put("y", spell.getY());
                            object.put("velx", spell.getVelocity().x);
                            object.put("vely", spell.getVelocity().y);
                            object.put("hp", spell.getHP());
                            object.put("id", key);
                            spellsPlayer.put(object);
                        } catch (JSONException e) {
                            Gdx.app.log("SOCKET.IO", "Error sending spell update data");
                        }
                    });
                    allSpells.put(spellsPlayer);
                });
                socket.emit("updateSpells", allSpells);

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
                    if (new Random().nextInt(100) == 1)
                        updatePlayersPosition.put(data.getString("id"), new Vector2((float) data.getDouble("posX"), (float) data.getDouble("posY")));
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
                //mob.setVelocity(new Vector2((float) data.getDouble("x"), (float) data.getDouble("y")));
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
        }).on("getSpells", args -> {
            JSONArray allSpells = (JSONArray) args[0];
            try {
                for (int i = 0; i < allSpells.length(); i++) {
                    JSONArray allSpellsPlayer = allSpells.getJSONArray(i);
                    ArrayList<Vector2> spellArrayList = new ArrayList<>();
                    String playerID = "";
                    for (int j = 0; j < allSpellsPlayer.length(); j++) {
                        JSONObject object = allSpellsPlayer.getJSONObject(j);
                        spellArrayList.add(new Vector2((float) object.getDouble("x"), (float) object.getDouble("y")));
                        playerID = object.getString("id");
                        Spell spell;
                        if (myId.equals(playerID)) {
                            spell = player.getAllSpells().get(j);
                        } else {
                            spell = remotePlayers.get(playerID).getAllSpells().get(j);
                        }
                        spell.setHP(object.getInt("hp"));
                        spell.setVelocity(new Vector2((float) object.getDouble("velx"), (float) object.getDouble("vely")));
                    }
                    updateSpellsPosition.put(playerID, spellArrayList);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Gdx.app.log("SocketIO", "Error Getting Players Data");
            }
        });
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

