package org.grup7.deheroes.screens;

import static org.grup7.deheroes.screens.MenuScreen.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.grup7.deheroes.helpers.AssetManager;
import org.grup7.deheroes.helpers.InputHandler;
import org.grup7.deheroes.objects.MainChar;
import org.grup7.deheroes.objects.Mob;
import org.grup7.deheroes.objects.MobBoss;
import org.grup7.deheroes.objects.Spell;
import org.grup7.deheroes.utils.Settings;

import java.util.ArrayList;

public class GameScreen implements Screen {
    public static MainChar mainChar = new MainChar(Settings.MainChar_STARTX, Settings.MainChar_STARTY, Settings.MainChar_WIDTH, Settings.MainChar_HEIGHT, 100);
    // TODO CAMBRIA EST QUE TRIGGERED A ALEXIA
    public static HealthBar healthBar;
    public static HealthBar boss_healthBar;
    public static Mob mob;
    public static Mob mob_boss;
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
    private int points = 0;
    private final Label show_points;

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
                    float width = collisionLayer.getTileWidth()-10;
                    float height = collisionLayer.getTileHeight()-10;
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
        healthBar = new HealthBar(100);
        stage.addActor(healthBar);
        show_points = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        stage.addActor(show_points);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                mob = new Mob(Settings.MainChar_WIDTH, Settings.MainChar_HEIGHT, 10, AssetManager.PurpleFlameSheet);
                mob.setCollisionRect(new Rectangle(mob.getX(), mob.getY(), mob.getWidth(), mob.getHeight()));
                mobs.add(mob);
                stage.addActor(mob);
            }
        }, 0, 2);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                spell = new Spell(Settings.MainChar_WIDTH, Settings.MainChar_HEIGHT, mainChar.getY(), mainChar.getX(), mainChar.getWalkDirection());
                spell.setCollisionRect(new Rectangle(spell.getX(), spell.getY(), spell.getWidth(), spell.getHeight()));
                spells.add(spell);
                stage.addActor(spell);

            }
        }, 0, 1);

    }

    @Override
    public void show() {
        AssetManager.GameScreenMusic.setLooping(true);
        AssetManager.GameScreenMusic.play();
    }

    @Override
    public void render(float delta) {
        show_points.setPosition(mainChar.getX() - 30, mainChar.getY() + 120);
        show_points.setText("Points: " + points);
        //show_points.draw(batch, "Points:"+points, Settings.MainChar_STARTX, Settings.MainChar_STARTY);
        if (mainChar.getHp() < 0) {
            // TODO add death menu
            AssetManager.HeroDiesSound.play();
            //Separar dead and game over
            AssetManager.GameOverSound.play();
            Gdx.app.exit();
        } else {
            if (points > 1){
                if (mob_boss == null) {
                    mob_boss = new MobBoss(64,64,500, AssetManager.PurpleFlameBossSheet, 50);
                    mobs.add(mob_boss);
                    mob_boss.setCollisionRect(new Rectangle(mob_boss.getX(), mob_boss.getY(), mob_boss.getWidth(), mob_boss.getHeight()));
                    boss_healthBar = new HealthBar(mob_boss.getHp());
                    stage.addActor(mob_boss);
                    stage.addActor(boss_healthBar);
                }
                if (mob_boss.getHp()<0){
                    mob_boss.dispose();
                } else{
                    boss_healthBar.setX_Y(mob_boss.getX()+15, mob_boss.getY()+60);
                    boss_healthBar.setHealth(mob_boss.getHp());
                    mob_boss.act(delta, mainChar.getPosition());
                }
            }
            healthBar.setX_Y(mainChar.getX(), mainChar.getY()+32);
            healthBar.setHealth(mainChar.getHp());

            mainChar.setCollisionRect(new Rectangle(mainChar.getX(), mainChar.getY(), mainChar.getWidth(), mainChar.getHeight()));
            // Iterate through the obstacle rectangles
            for (Rectangle obstacleRect : obstacleRectangles) {
                if (Intersector.overlaps(mainChar.getCollisionRect(), obstacleRect)) {
                    // Collision detected, stop the player's movement
                    mainChar.ObjectCollision();
                }
            }
            ArrayList<Spell> spells_removed = new ArrayList<>();
            ArrayList<Mob> mobs_eliminated = new ArrayList<>();
            for (Mob mob : mobs) {
                mob.act(delta, mainChar.getPosition());
                mob.setCollisionRect(new Rectangle(mob.getX(), mob.getY(), mob.getWidth(), mob.getHeight()));
                if (Intersector.overlaps(mainChar.getCollisionRect(), mob.getCollisionRect())) {
                    // Collision detected, stop the player's movement
                    mainChar.setHp(mainChar.getHp() - 0.1F);
                    AssetManager.GetHitHeroSound.play();
                }
                for (Spell spell : spells) {
                    if (spell.getX() > 2000 || spell.getY() > 2000) {
                        spells_removed.add(spell);
                        spell.dispose();
                    } else {
                        //spell.act(delta);
                        spell.act(delta, mobs.get(0).getX(), mobs.get(0).getY(), mainChar.getPosition());
                        spell.setCollisionRect(new Rectangle(spell.getX(), spell.getY(), spell.getWidth(), spell.getHeight()));
                        if (Intersector.overlaps(spell.getCollisionRect(), mob.getCollisionRect())) {
                            // Collision detected, remove hp mobs
                            mob.setHp(mob.getHp() - 10);
                            spells_removed.add(spell);

                            spell.dispose();
                        }
                        if (mob.getHp() < 0) {
                            mob.dispose();
                            mobs_eliminated.add(mob);
                            if (mob.isBoss()) {
                                AssetManager.PurpleBossDiesSound.play();
                                points += 50;

                            } else {
                                AssetManager.PurpleFLameDiesSound.play();
                                points += 1;

                            }
                        }
                    }
                }
            }
            for (Spell spell : spells_removed) {
                spells.remove(spell);
            }
            for (Mob mob : mobs_eliminated) {
                mobs.remove(mob);
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
        AssetManager.GameScreenMusic.dispose();
        AssetManager.GameOverSound.dispose();
        AssetManager.GetHitHeroSound.dispose();
        AssetManager.GetHitPurpleFlameSound.dispose();
        AssetManager.GetHitFlameBossSound.dispose();

        AssetManager.HeroDiesSound.dispose();
        AssetManager.PurpleFLameDiesSound.dispose();
        AssetManager.PurpleBossDiesSound.dispose();

        AssetManager.IceSpellSound.dispose();


    }

    public MainChar getMainChar() {
        return mainChar;
    }

    public Stage getStage() {
        return stage;
    }
}
