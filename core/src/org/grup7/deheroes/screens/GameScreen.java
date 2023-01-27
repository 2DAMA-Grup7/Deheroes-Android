package org.grup7.deheroes.screens;

import static org.grup7.deheroes.screens.MenuScreen.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.grup7.deheroes.helpers.InputHandler;
import org.grup7.deheroes.objects.MainChar;
import org.grup7.deheroes.utils.Settings;

import java.util.ArrayList;

public class GameScreen implements Screen {
    public static MainChar mainChar = new MainChar(Settings.MainChar_STARTX, Settings.MainChar_STARTY, Settings.MainChar_WIDTH, Settings.MainChar_HEIGHT);
    private final Stage stage;
    TmxMapLoader mapLoader = new TmxMapLoader();

    // Load the Tiled map
    TiledMap map = mapLoader.load("maps/tilemap.tmx");
    // Get the collision layer from the Tiled map
    TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("collision");
    // Create a new TiledMapRenderer and set the map
    TiledMapRenderer renderer = new OrthogonalTiledMapRenderer(map);
    // Create an ArrayList to store the collision rectangles
    ArrayList<Rectangle> obstacleRectangles = new ArrayList<>();

    public GameScreen(Batch prevBatch, Viewport prevViewport) {
        // Iterate through the cells in the collision layer
        for (int x = 0; x < collisionLayer.getWidth(); x++) {
            for (int y = 0; y < collisionLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);
                if (cell != null) {
                    // Create a rectangle for the cell
                    float xPos = x * collisionLayer.getTileWidth();
                    float yPos = y * collisionLayer.getTileHeight();
                    float width = collisionLayer.getTileWidth();
                    float height = collisionLayer.getTileHeight();
                    Rectangle rect = new Rectangle(xPos, yPos, width, height);
                    // Add the rectangle to the ArrayList
                    obstacleRectangles.add(rect);
                }
            }
        }

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
        mainChar.setCollisionRect(new Rectangle(mainChar.getX(), mainChar.getY(), mainChar.getWidth(), mainChar.getHeight()));

        // Iterate through the obstacle rectangles
        for (Rectangle obstacleRect : obstacleRectangles) {
            if (Intersector.overlaps(mainChar.getCollisionRect(), obstacleRect)) {
                // Collision detected, stop the player's movement
                mainChar.setPlayer_x(mainChar.getPrev_x());
                mainChar.setPlayer_y(mainChar.getPrev_y());
            }
        }


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
