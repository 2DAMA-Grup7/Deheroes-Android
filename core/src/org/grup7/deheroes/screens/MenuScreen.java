package org.grup7.deheroes.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.grup7.deheroes.MyGdxGame;
import org.grup7.deheroes.utils.Settings;

public class MenuScreen implements Screen {
    public static OrthographicCamera camera;
    private final Stage stage;
    private final MyGdxGame game;

    public MenuScreen(MyGdxGame game) {
        this.game = game;
        camera = new OrthographicCamera(Settings.GAME_WIDTH, Settings.GAME_HEIGHT);

        StretchViewport viewport = new StretchViewport(Settings.GAME_WIDTH, Settings.GAME_HEIGHT, camera);
        stage = new Stage(viewport);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        stage.draw();
        stage.act(delta);

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(stage.getBatch(), stage.getViewport()));
            dispose();
        }

    }

    @Override
    public void resize(int width, int height) {
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
    public void dispose() {
    }
}
