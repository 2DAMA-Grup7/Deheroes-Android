package org.grup7.deheroes.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.grup7.deheroes.Vars;
import org.grup7.deheroes.actors.heroes.Hero;
import org.grup7.deheroes.actors.heroes.Rogue;
import org.grup7.deheroes.actors.mobs.Mob;
import org.grup7.deheroes.actors.mobs.PurpleFlame;
import org.grup7.deheroes.actors.mobs.PurpleFlameBoss;
import org.grup7.deheroes.input.InputHandler;
import org.grup7.deheroes.ui.Hud;
import org.grup7.deheroes.utils.Assets;
import org.grup7.deheroes.utils.WorldContactListener;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

public class SinglePlayer implements Screen {
    public static ConcurrentLinkedDeque<Actor> actorQueue = new ConcurrentLinkedDeque<>();
    public static ConcurrentLinkedDeque<Mob> allMobs = new ConcurrentLinkedDeque<>();
    public static int score = 0;

    protected final Box2DDebugRenderer debugRenderer;
    protected final TiledMapRenderer mapRenderer;
    protected final OrthographicCamera camera;
    protected final Hud hud;
    protected final Stage stage;
    protected final ArrayList<Hero> players;
    protected final World world;
    protected final Game game;

    protected long lastMobSpawn;

    public SinglePlayer(Game game, String map) {
        this.game = game;
        this.world = new World(new Vector2(0, 0), true);
        this.debugRenderer = new Box2DDebugRenderer();
        this.camera = new OrthographicCamera(Vars.gameWidth, Vars.gameHeight);
        this.stage = new Stage(new StretchViewport(Vars.gameWidth, Vars.gameHeight, camera));
        this.players = new ArrayList<>();
        this.hud = new Hud();
        this.mapRenderer = new OrthogonalTiledMapRenderer(loadMap(map));
        Hero player = new Rogue(world);
        players.add(player);
        world.setContactListener(new WorldContactListener(player));
        stage.addActor(player);
        Gdx.input.setInputProcessor(new InputHandler(player));
        mobsCreation();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if (players.get(0).getHp() < 0) {
            Gdx.audio.newSound(Gdx.files.internal(Assets.Sounds.gameOver)).play();
            dispose();
            game.setScreen(new GameOver(game));
        } else {
            actorQueue.forEach(stage::addActor);
            actorQueue.clear();
            actorAct(delta);
            //System.out.println("PlayerX: " + player.getX() + " PlayerY: " + player.getY());
            world.step(delta, 6, 2);
            camera.position.set(players.get(0).getX(), players.get(0).getY(), 0);
            camera.update();
            mapRenderer.setView(camera);
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            mapRenderer.render();
            debugRenderer.render(world, camera.combined);
            hud.updateScoreLabel(score);
            stage.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
            stage.draw();
            hud.getStage().draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.zoom = 0.4f;
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
        allMobs.clear();
        actorQueue.clear();
        score = 0;
    }

    private Hero closerPlayer(Vector2 distanceMob) {
        Hero closerPlayer = null;
        float smallerDistance = Float.MAX_VALUE;
        for (Hero player : players) {
            if (player.getPosition().dst(distanceMob) < smallerDistance) {
                smallerDistance = player.getPosition().dst(distanceMob);
                closerPlayer = player;
            }
        }
        return closerPlayer;
    }

    protected void actorAct(float delta) {
        // Player
        players.forEach(player -> player.act(delta));
        // Mobs
        allMobs.forEach(mob -> {
            if (mob.isAlive()) {
                mob.act(delta, closerPlayer(mob.getPosition()));
            } else {
                if (TimeUtils.nanoTime() - lastMobSpawn > 2000000000) {
                    float randomX = new Random().nextInt(300);
                    float randomY = new Random().nextInt(300);
                    mob.awake(new Vector2(randomX, randomY));
                    multiplayerSetMob(randomX, randomY);
                    lastMobSpawn = TimeUtils.nanoTime();
                }
            }
        });
    }

    protected void multiplayerSetMob(float randomX, float randomY) {
    }

    protected void multiplayerMovement(Vector2 velocity){}

    private void mobsCreation() {
        Mob mobBoss = new PurpleFlameBoss(world, players.get(0).getPosition());
        allMobs.add(mobBoss);
        stage.addActor(mobBoss);
        // Create x mobs
        for (int i = 0; i < 30; i++) {
            Mob mob = new PurpleFlame(world);
            allMobs.add(mob);
            stage.addActor(mob);
        }
    }

    private TiledMap loadMap(String mapPath) {
        // Load the Tiled map
        TiledMap map = new TmxMapLoader().load(mapPath);
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("trees");
        // Define the boundaries of the world using a BodyDef
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(0, 0);
        Body groundBody = world.createBody(groundBodyDef);
        // Define the shape of the boundaries using a ChainShape
        ChainShape groundBox = new ChainShape();
        groundBox.createChain(new Vector2[]{
                new Vector2(0, 0),
                new Vector2(Vars.gameWidth, 0),
                new Vector2(Vars.gameWidth, Vars.gameHeight),
                new Vector2(0, Vars.gameHeight),
                new Vector2(0, 0)
        });
        // Attach the shape to the body using a FixtureDef
        FixtureDef def = new FixtureDef();
        def.shape = groundBox;
        def.density = 0f;
        def.friction = 0.3f;
        def.restitution = 0.4f;
        groundBody.createFixture(def);
        // Set map collisions
        for (int x = 0; x < collisionLayer.getWidth(); x++) {
            for (int y = 0; y < collisionLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);
                if (cell != null) {
                    float xPos = x * collisionLayer.getTileWidth();
                    float yPos = y * collisionLayer.getTileHeight();
                    float width = collisionLayer.getTileWidth() - 10;
                    float height = collisionLayer.getTileHeight() - 10;
                    BodyDef bodyDef = new BodyDef();
                    bodyDef.type = BodyDef.BodyType.StaticBody;
                    bodyDef.position.set(xPos + width / 2, yPos + height / 2);
                    bodyDef.fixedRotation = true;
                    Body body = world.createBody(bodyDef);
                    PolygonShape polygonShape = new PolygonShape();
                    polygonShape.setAsBox(width / 2, height / 2);
                    FixtureDef fixtureDef = new FixtureDef();
                    fixtureDef.shape = polygonShape;
                    body.createFixture(fixtureDef).setUserData(this);
                    polygonShape.dispose();
                }
            }
        }
        return map;
    }
}

