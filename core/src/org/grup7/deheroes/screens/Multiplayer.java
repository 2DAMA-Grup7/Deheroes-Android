package org.grup7.deheroes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;

import org.grup7.deheroes.Vars;
import org.grup7.deheroes.actors.heroes.Hero;
import org.grup7.deheroes.actors.heroes.Witch;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Multiplayer extends SinglePlayer implements Screen {
    private final Hero onlinePlayer;
    private Socket socket;

    public Multiplayer(Game game, String map) {
        super(game, map);
        onlinePlayer = new Witch(world);
        players.add(onlinePlayer);
        actorQueue.add(onlinePlayer);
        onlinePlayer.getBody().setTransform(Vars.deadPointX, Vars.deadPointY, 0);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    protected void actorAct(float delta) {
        super.actorAct(delta);
    }

    @Override
    protected void multiplayerSetMob(float randomX, float randomY) {
        JSONObject data = new JSONObject();
        try {
            data.put("x", randomX);
            data.put("y", randomY);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        socket.emit("sendMobs", data);
    }

    public void show() {
        connectSocket();
        setSocket();
    }

    public void setSocket() {
        socket.on(Socket.EVENT_CONNECT, args -> {
            players.get(0).setId(0);
            System.out.println(players.get(0).getId());
            JSONObject data = new JSONObject();
            try {
                data.put("id", players.get(0).getId());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            socket.emit("connection", data);
            Gdx.app.log("SocketIO", "Connected");
        }).on("getP2", args -> {
            JSONObject data = (JSONObject) args[0];
            System.out.println("Connect2");
            try {
                System.out.println("player connected with id: " + data.getInt("id"));
                onlinePlayer.setId(data.getInt("id"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            onlinePlayer.getBody().setTransform(300, 300, 0);
        }).on("playerDisconnected", args -> {
            JSONObject data = (JSONObject) args[0];
            players.forEach(player -> {
                try {
                    if (player.getId() == data.getInt("id")) {
                        players.remove(player);
                    }
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
            });
            Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
        }).on("socketID", args -> {
            JSONObject data = (JSONObject) args[0];
            try {
                System.out.println(data.get("id"));
                data.getString("id");
                Gdx.app.log("SocketIO", "My ID: ");
            } catch (JSONException e) {
                Gdx.app.log("SocketIO", "Error getting ID");
            }
        }).on("playerMoved", args -> {
            JSONObject data = (JSONObject) args[0];
            System.out.println(data.toString());
            players.forEach(player -> {
                try {
                    if (data.getInt("id") == player.getId()) {
                        Vector2 newVelocity = new Vector2((float) data.getDouble("velocityx"), (float) data.getDouble("velocityy"));
                        player.setVelocity(newVelocity);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
        }).on("error", args -> Gdx.app.log("error", "YOU DENSE MOTHERFUCKER, YOU CRAsHED THE GAMEEEE"));
    }

    public void connectSocket() {
        try {
            socket = IO.socket(Vars.localURL);
            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

