package org.grup7.deheroes.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.grup7.deheroes.Assets;
import org.grup7.deheroes.Vars;
import org.grup7.deheroes.actors.Hero;
import org.grup7.deheroes.actors.Mob;
import org.grup7.deheroes.actors.MobPurpleFlameBoss;

import java.util.ArrayList;

public class SinglePlayer implements Screen {
    private final TiledMapRenderer mapRenderer;
    private final ArrayList<Rectangle> mapCollisions;
    private final OrthographicCamera camera;
    private final Stage stage;
    private final Hero player;
    private ArrayList<Mob> allMobs;


    public SinglePlayer(Batch prevBatch, String map, String heroSprite) {
        this.camera = new OrthographicCamera(Vars.gameWidth, Vars.gameHeight);
        this.stage = new Stage(new StretchViewport(Vars.gameWidth, Vars.gameHeight, camera), prevBatch);
        this.player = new Hero(Vars.startX, Vars.startY, 100, heroSprite);
        this.mapCollisions = new ArrayList<>();
        this.allMobs = new ArrayList<>();
        this.mapRenderer = new OrthogonalTiledMapRenderer(loadMap(map));
        stage.addActor(player);
        allMobs.add(new MobPurpleFlameBoss(32, 32, 60F, Assets.Mobs.purpleFlame));
        stage.addActor(allMobs.get(0));
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        player.act(delta);

        allMobs.forEach(mob -> {
            mob.act(delta, player.getPosition());
        });

        camera.position.set(player.getX(), player.getY(), 0);
        camera.zoom = 0.4f;
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();
        stage.draw();
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

    public TiledMap loadMap(String mapPath) {
        TmxMapLoader mapLoader = new TmxMapLoader();
        // Load the Tiled map
        TiledMap map = mapLoader.load(mapPath);
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("collision");
        for (int x = 0; x < collisionLayer.getWidth(); x++) {
            for (int y = 0; y < collisionLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);
                if (cell != null) {
                    // Create a rectangle for the cell
                    float xPos = x * collisionLayer.getTileWidth();
                    float yPos = y * collisionLayer.getTileHeight();
                    float width = collisionLayer.getTileWidth() - 10;
                    float height = collisionLayer.getTileHeight() - 10;
                    Rectangle rect = new Rectangle(xPos, yPos, width, height);
                    // Add the rectangle to the ArrayList
                    mapCollisions.add(rect);
                }
            }
        }
        return map;
    }
}

