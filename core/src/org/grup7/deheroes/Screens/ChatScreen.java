package org.grup7.deheroes.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.grup7.deheroes.Helpers.AssetManager;
import org.json.JSONArray;
import org.json.JSONException;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatScreen implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final Table SetNickTable;
    private final Table SetChatTable;
    private Socket socket;
    private String nickname;
    private TextArea textArea;
    private List<String> usersList;
    private Label ChatSpace;
    private TextField nickInput;
    private final String chatURL;


    public ChatScreen() {
        chatURL = AssetManager.chatURL;
        skin = AssetManager.UIskin;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        SetNickTable = LoginTable();
        SetChatTable = ChatRoomTable();

        stage.addActor(SetNickTable);
        stage.addActor(SetChatTable);
    }

    private Table LoginTable(){
        final Table table = new Table();
        table.setFillParent(true);

        Window window = new Window("Login", skin);
        window.getTitleLabel().setAlignment(Align.center);

        TextButton join_button = new TextButton("Join", skin);
        nickInput = new TextField("", skin);

        window.add(new Label("Enter Your Nickname", skin));
        window.row();
        window.add(nickInput);
        window.row();
        window.add(join_button);


        table.add(window);
        join_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                nickname = nickInput.getText();
                if(!nickname.isEmpty()){
                    SetNickTable.setVisible(false);
                    SetChatTable.setVisible(true);
                    try {
                        socket = IO.socket(chatURL);
                        socket.connect();
                        socketThings();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return table;
    }
    private Table ChatRoomTable(){
        Table table = new Table();
        table.setFillParent(true);

        ChatSpace = new Label("", skin);
        ChatSpace.setWrap(true);
        ChatSpace.setAlignment(Align.topLeft);


        ScrollPane chatScroll = new ScrollPane(ChatSpace, skin);
        chatScroll.setFadeScrollBars(false);

        table.add(chatScroll).width(400).height(400f).colspan(2);

        usersList = new List<>(skin);

        ScrollPane usersScroll = new ScrollPane(usersList, skin);
        usersScroll.setFadeScrollBars(false);

        table.add(usersScroll).width(150f).height(400f).colspan(2);

        textArea = new TextArea("", skin);
        textArea.setPrefRows(2);

        ScrollPane inputScroll = new ScrollPane(textArea, skin);
        inputScroll.setFadeScrollBars(false);
        table.row();
        table.add(inputScroll).width(300f).colspan(2);
        TextButton send_button = new TextButton("Send", skin);
        table.add(send_button).colspan(2);
        table.setVisible(false);
        send_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                String text = textArea.getText();

                if(!text.isEmpty()){
                    socket.emit("userMessage", nickname + ": " + text + "\n");
                    textArea.setText("");
                }
            }
        });
        return table;
    }
    private void socketThings(){
        final Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socket.emit("setNick", nickname);
            }
        }).on("getUsers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray array = (JSONArray)args[0];
                Array<String> users = new Array<>();
                try{
                    for(int i = 0; i < array.length(); i++){
                        String nick = array.getJSONObject(i).getString("name");
                        users.add(nick);
                    }
                }catch (JSONException e){
                    System.out.println("ERROR GETTING USERS");
                }
                users.add(nickname);
                usersList.setItems(users);
            }
        }).on("userMessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String text = (String)args[0];
                ChatSpace.setText(ChatSpace.getText() + text);
            }
        }).on("newUser", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                String name = (String)args[0];
                Array<String> users = usersList.getItems();
                users.add(name);
                usersList.setItems(users);

            }
        }).on("userDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String name = (String)args[0];
                Array<String> users = usersList.getItems();
                if(users.contains(name, false)){
                    users.removeValue(name, false);
                    usersList.setItems(users);
                }
            }
        });
    }
    @Override
    public void show() {
        AssetManager.GameScreenMusic.setLooping(true);
        AssetManager.GameScreenMusic.play();
    }
    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
    @Override
    public void resize(int width, int height){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getViewport().update(width, height, true);
    }
    @Override
    public void pause() {

    }
    @Override
    public void resume() {

    }
    @Override
    public void hide() {

    }

    @Override
    public void dispose () {
        stage.dispose();
        skin.dispose();
    }
}