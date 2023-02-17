package org.grup7.deheroes.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

import org.grup7.deheroes.Vars;
import org.grup7.deheroes.actors.heroes.Hero;
import org.grup7.deheroes.actors.heroes.Witch;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Connections {
    protected Hero localPlayer;
    protected Hero remotePlayer;
    protected World world;
    protected Stage stage;
    private Socket socket;
    private float tick;

    // Send local player info to server
    public void updateServer(float time) {
        tick += time;
        float UPDATE_TIME = 1 / 60f;
        if (tick >= UPDATE_TIME && localPlayer != null) {
            JSONObject data = new JSONObject();
            try {
                data.put("x", localPlayer.getX());
                data.put("y", localPlayer.getY());
                socket.emit("playerMoved", data);
            } catch (JSONException e) {
                Gdx.app.log("Socket.io", e.toString());
            }
        }
    }

    public void connectSocket() {
        try {
            socket = IO.socket(Vars.serverURL);
            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSocket() {
        socket.on(Socket.EVENT_CONNECT, args -> {
                Gdx.app.log("SocketIO", "Connected");
                })
                .on("newPlayer", args -> {
                    remotePlayer = new Witch(world);
                    stage.addActor(remotePlayer);
                })
                .on("playerDisconnected", args -> remotePlayer.dispose())
                .on("playerMoved", args -> {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        double x = data.getDouble("x");
                        double y = data.getDouble("y");
                        if (remotePlayer != null) {
                            remotePlayer.setPosition((float) x, (float) y);
                        }
                    } catch (JSONException e) {
                        Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
                    }
                });
    }


}
