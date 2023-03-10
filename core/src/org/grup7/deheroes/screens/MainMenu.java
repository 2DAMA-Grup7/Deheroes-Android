package org.grup7.deheroes.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.grup7.deheroes.utils.Assets;

public class MainMenu implements Screen {
    private final Game game;
    private final Stage stage;
    private final Music music;
    private final Table setMenuTable;
    private final Table setCharTable;
    protected Skin skin = new Skin(Gdx.files.internal(Assets.Skin.uiSkin));
    //private String nickname;
    //private TextField nickInput;

    public MainMenu(Game game) {
        this.game = game;
        music = Gdx.audio.newMusic(Gdx.files.internal(Assets.Music.menu));
        music.setLooping(true);
        music.play();
        stage = new Stage();

        setMenuTable = menuTable();
        setCharTable = charTable();

        stage.addActor(setMenuTable);
        stage.addActor(setCharTable);

        setMenuTable.setVisible(true);
        setCharTable.setVisible(false);
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
                setMenuTable.setVisible(false);
                setCharTable.setVisible(true);
                /*game.setScreen(new SinglePlayer(game, Assets.Maps.landOfDeath));
                dispose();*/
            }
        });
        onlineButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new Multiplayer(game, Assets.Maps.landOfDeath, true));
                dispose();
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

    private Table charTable() {
        TextButton witch = new TextButton("Witch", skin);
        TextButton rogue = new TextButton("Rogue", skin);

        Window window = new Window("Choose a character", skin);
        window.add(witch).center();
        window.row();
        window.add(rogue).center();
        window.row();

        Table table = new Table();
        table.setFillParent(true);
        table.add(window);

        witch.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SinglePlayer(game, Assets.Maps.landOfDeath, true));
                dispose();
            }
        });

        rogue.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SinglePlayer(game, Assets.Maps.landOfDeath, false));
                dispose();
            }
        });


        return table;
    }

}