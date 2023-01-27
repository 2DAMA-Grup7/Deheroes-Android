package org.grup7.deheroes.screens;

import static org.grup7.deheroes.screens.MenuScreen.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.grup7.deheroes.helpers.InputHandler;
import org.grup7.deheroes.objects.MainChar;
import org.grup7.deheroes.utils.Settings;

import jdk.tools.jmod.Main;

public class GameScreen implements Screen {
    private final Stage stage;
    private final MainChar mainChar = new MainChar(Settings.MainChar_STARTX, Settings.MainChar_STARTY, Settings.MainChar_WIDTH, Settings.MainChar_HEIGHT);
    TmxMapLoader mapLoader = new TmxMapLoader();
    TiledMap map = mapLoader.load("maps/tilemap.tmx");

    // Create a new TiledMapRenderer and set the map
    TiledMapRenderer renderer = new OrthogonalTiledMapRenderer(map);



    public GameScreen(Batch prevBatch, Viewport prevViewport) {
        Gdx.input.setInputProcessor(new InputHandler(this));
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        stage = new Stage(prevViewport, prevBatch);
        stage.addActor(mainChar);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(mainChar.getX(), mainChar.getY(), 0);
        camera.zoom = 0.4f;
        // Update the camera
        camera.update();
        renderer.setView(camera);

        // Render the map
        renderer.render();

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
