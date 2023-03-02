package org.grup7.deheroes.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.sun.org.apache.xpath.internal.operations.Mult;

import org.grup7.deheroes.Vars;
import org.grup7.deheroes.actors.heroes.Hero;
import org.grup7.deheroes.actors.heroes.Witch;
import org.json.JSONException;
import org.json.JSONObject;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Multiplayer extends SinglePlayer implements Screen {
    private Socket socket;
    private Hero onlinePlayer;

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
    protected void actorAct(float delta){
        super.actorAct(delta);
    }

    @Override
    protected void setMob(float tmpx, float tmpy){
        JSONObject data = new JSONObject();
        try {
            data.put("x" , tmpx);
            data.put("y" , tmpy);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        socket.emit("sendMobs" , data);
    }


    public void show() {
        connectSocket();
        setSocket();
    }

    public void setSocket(){
        socket.on(Socket.EVENT_CONNECT, args -> {
            players.get(0).setId(0);
            System.out.println(players.get(0).getId());
            JSONObject data = new JSONObject();
            try {
                data.put("id" , players.get(0).getId());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            socket.emit("connectionP1" , data);
            Gdx.app.log("SocketIO", "Connected");
        }).on("getP2", args -> {
            JSONObject data = new JSONObject(args[0]);
            try {
                System.out.println(data.getInt("id"));
                onlinePlayer.setId(data.getInt("id"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            onlinePlayer.setPosition(300,300);
            //stage.addActor(onlinePlayer);

        }).on("playerDisconnected", args -> {
            JSONObject data = (JSONObject) args[0];
                players.forEach(player->{
                    try {
                        if(player.getId()==data.getInt("id")){
                            players.remove(player);
                        }
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
            }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    System.out.println(data.get("id"));
                    data.getString("id");
                    Gdx.app.log("SocketIO", "My ID: ");
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting ID");
                }
            }
        });

    }

    public void connectSocket(){
        try {
            socket = IO.socket(Vars.localURL);
            socket.connect();
        } catch(Exception e){
            System.out.println(e);
        }
    }


}

