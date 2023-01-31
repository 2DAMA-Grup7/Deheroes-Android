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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.grup7.deheroes.MyGdxGame;
import org.grup7.deheroes.helpers.InputHandler;
import org.grup7.deheroes.objects.MainChar;
import org.grup7.deheroes.objects.Mob;
import org.grup7.deheroes.objects.Spell;
import org.grup7.deheroes.utils.Settings;

import java.awt.peer.ScrollbarPeer;
import java.util.ArrayList;

public class GameScreen implements Screen {
    public static MainChar mainChar = new MainChar(Settings.MainChar_STARTX, Settings.MainChar_STARTY, Settings.MainChar_WIDTH, Settings.MainChar_HEIGHT, 100);
    // TODO CAMBRIA EST QUE TRIGGERED A ALEXIA
    public static HealthBar healthBar;
    public static Mob mob;
    public static Spell spell;

    private final ArrayList<Mob> mobs = new ArrayList<>();
    private final ArrayList<Spell> spells = new ArrayList<>();
    private final Stage stage;
    private final TmxMapLoader mapLoader = new TmxMapLoader();
    // Load the Tiled map
    private final TiledMap map = mapLoader.load("maps/tilemap.tmx");
    // Create a new TiledMapRenderer and set the map
    private final TiledMapRenderer renderer = new OrthogonalTiledMapRenderer(map);
    // Create an ArrayList to store the collision rectangles
    private final ArrayList<Rectangle> obstacleRectangles = new ArrayList<>();

    public GameScreen(Batch prevBatch, Viewport prevViewport) {
        // Iterate through the cells in the collision layer
        // Get the collision layer from the Tiled map
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("collision");
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
        healthBar = new HealthBar(100, mainChar.getX(), mainChar.getY());
        stage.addActor(healthBar);
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                mob = new Mob(Settings.MainChar_WIDTH, Settings.MainChar_HEIGHT);
                mob.setCollisionRect(new Rectangle(mob.getX(), mob.getY(), mob.getWidth(), mob.getHeight()));
                mobs.add(mob);
                stage.addActor(mob);
            }
        }, 0, 2);

        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                spell = new Spell(Settings.MainChar_WIDTH, Settings.MainChar_HEIGHT, mainChar.getY(), mainChar.getX()+32, mainChar.getWalkDirection());
                spell.setCollisionRect(new Rectangle(spell.getX(), spell.getY(), spell.getWidth(), spell.getHeight()));
                spells.add(spell);
                stage.addActor(spell);

            }
        }, 0, 1);




    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(mainChar.getHp()<0){
            //stage.dispose();
            // TODO add death menu
            Gdx.app.exit();
        } else {
            healthBar.setX_Y(mainChar.getX(), mainChar.getY());
            healthBar.setHealth(mainChar.getHp());
            mainChar.setCollisionRect(new Rectangle(mainChar.getX(), mainChar.getY(), mainChar.getWidth(), mainChar.getHeight()));
            // Iterate through the obstacle rectangles
            for (Rectangle obstacleRect : obstacleRectangles) {
                if (Intersector.overlaps(mainChar.getCollisionRect(), obstacleRect)) {
                    // Collision detected, stop the player's movement
                    mainChar.ObjectCollision();
                }

            }
            for (Mob mob : mobs) {
                if (Intersector.overlaps(mainChar.getCollisionRect(), mob.getCollisionRect())) {
                    // Collision detected, stop the player's movement
                    mainChar.setHp(mainChar.getHp()-0.1F);
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
            for (Mob mob:mobs) {
                mob.act(delta, mainChar.getPosition());
                mob.setCollisionRect(new Rectangle(mob.getX(), mob.getY(), mob.getWidth(), mob.getHeight()));

            }
            for (Spell spell:spells) {
                if(spell.getX() > 2000 || spell.getY() > 2000){
                    spells.remove(spell);
                    spell.dispose();
                    break;
                } else {
                    spell.act(delta);
                    spell.setCollisionRect(new Rectangle(spell.getX(), spell.getY(), spell.getWidth(), spell.getHeight()));
                }
            }


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
        mainChar.dispose();
        healthBar.dispose();
        mob.dispose();
    }

    public MainChar getMainChar() {
        return mainChar;
    }

    public Stage getStage() {
        return stage;
    }
}
