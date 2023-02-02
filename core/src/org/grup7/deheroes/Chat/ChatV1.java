package org.grup7.deheroes.Chat;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

public class ChatV1 {
    public static void main(String[] args) throws URISyntaxException {
        Socket socket = IO.socket("http://localhost:3001");
        socket.connect();

        socket.on("chat message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Received message: " + args[0]);
            }
        });
        socket.emit("chat message", "yellow");
    }
}
