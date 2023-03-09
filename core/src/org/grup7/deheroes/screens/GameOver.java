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
import org.json.JSONObject;

public class GameOver implements Screen {
    private final Game game;
    private final Stage stage;
    private final Music music;

    public GameOver(Game game, JSONObject data) {
        this.game = game;
        music = Gdx.audio.newMusic(Gdx.files.internal(Assets.Music.menu));
        music.setLooping(true);
        music.play();
        stage = new Stage();
        stage.addActor(menuTable());
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
        Skin skin = new Skin(Gdx.files.internal(Assets.Skin.uiSkin));
        TextButton restartButton = new TextButton("New Game", skin);
        TextButton returnButton = new TextButton("Menu", skin);
        TextButton exitButton = new TextButton("Exit", skin);
        Window window = new Window("GAME OVER", skin);
        window.add(restartButton).center();
        window.row();
        window.add(returnButton).center();
        window.row();
        window.add(exitButton).center();
        Table table = new Table();
        table.setFillParent(true);
        table.add(window);
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SinglePlayer(game, Assets.Maps.landOfDeath));
                dispose();
            }
        });
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenu(game));
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
}