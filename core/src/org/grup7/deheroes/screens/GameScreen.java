package org.grup7.deheroes.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.grup7.deheroes.objects.MainChar;
import org.grup7.deheroes.utils.Settings;

public class GameScreen implements Screen {
    private final Stage stage;
    private final MainChar mainChar = new MainChar(Settings.MainChar_STARTX, Settings.MainChar_STARTY, Settings.MainChar_WIDTH, Settings.MainChar_HEIGHT);

    public GameScreen(Batch prevBatch, Viewport prevViewport) {
       
        stage = new Stage(prevViewport, prevBatch);
        stage.addActor(mainChar);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 0);
        ScreenUtils.clear(1, 1, 1, 0);
        stage.draw();
        mainChar.act(delta);
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

    public MainChar getMainChar() {
        return mainChar;
    }

    public Stage getStage() {
        return stage;
    }
}
