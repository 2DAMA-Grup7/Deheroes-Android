package org.grup7.deheroes.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import org.grup7.deheroes.utils.Assets;

import java.net.URISyntaxException;
import java.util.Set;

import io.socket.client.IO;

public class MainMenu implements Screen {
    private final Game game;
    private final Stage stage;
    private final Music music;
    private String nickname;

    private final Table SetNickTable;
    private final Table setMenuTable;
    private TextField nickInput;


    protected Skin skin = new Skin(Gdx.files.internal(Assets.Skin.uiSkin));

    public MainMenu(Game game) {
        this.game = game;
        music = Gdx.audio.newMusic(Gdx.files.internal(Assets.Music.menu));
        music.setLooping(true);
        music.play();
        stage = new Stage();


        SetNickTable = LoginTable();
        setMenuTable = menuTable();

        stage.addActor(setMenuTable);
        stage.addActor(SetNickTable);

        SetNickTable.setVisible(false);
        setMenuTable.setVisible(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        music.stop();
    }

    private Table menuTable() {
        TextButton startButton = new TextButton("Start", skin);
        TextButton onlineButton = new TextButton("Online", skin);
        TextButton exitButton = new TextButton("Exit", skin);
        Window window = new Window("Menu", skin);
        window.add(startButton).center();
        window.row();
        window.add(onlineButton).center();
        window.row();
        window.add(exitButton).center();
        Table table = new Table();
        table.setFillParent(true);
        table.add(window);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SinglePlayer(game, Assets.Maps.landOfDeath));
                dispose();
            }
        });
        onlineButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMenuTable.setVisible(false);
                SetNickTable.setVisible(true);
                /*game.setScreen(new Multiplayer(game, Assets.Maps.landOfDeath));
                dispose();*/
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        return table;
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
        window.row();
        TextButton backButton = new TextButton("Back", skin);



        table.add(window);
        join_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                nickname = nickInput.getText();
                if(!nickname.isEmpty()){
                    SetNickTable.setVisible(false);
                    game.setScreen(new Multiplayer(game, Assets.Maps.landOfDeath));
                    dispose();
                }
            }
        });
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SetNickTable.setVisible(false);
                setMenuTable.setVisible(true);
            }
        });

        return table;
    }




}