package org.grup7.deheroes.screens;


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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.grup7.deheroes.Vars;
import org.grup7.deheroes.actors.heroes.Hero;
import org.grup7.deheroes.actors.heroes.Witch;
import org.grup7.deheroes.actors.mobs.Mob;
import org.grup7.deheroes.actors.mobs.PurpleFlame;
import org.grup7.deheroes.actors.mobs.PurpleFlameBoss;
import org.grup7.deheroes.actors.spells.IceBall;
import org.grup7.deheroes.actors.spells.Spell;
import org.grup7.deheroes.utils.WorldContactListener;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

public class SinglePlayer implements Screen {
    public static ConcurrentLinkedDeque<Actor> actorQueue = new ConcurrentLinkedDeque<>();
    public static ConcurrentLinkedDeque<Mob> allMobs = new ConcurrentLinkedDeque<>();
    public static ConcurrentLinkedDeque<Spell> allSpells = new ConcurrentLinkedDeque<>();
    private final Box2DDebugRenderer debugRenderer;
    private final TiledMapRenderer mapRenderer;
    private final OrthographicCamera camera;
    private final Stage stage;
    private final Hero player;
    private final World world;
    private long lastSpellSpawn;
    private long lastMobSpawn;

    public SinglePlayer(String map) {
        this.world = new World(new Vector2(0, 0), true);
        this.debugRenderer = new Box2DDebugRenderer();
        this.camera = new OrthographicCamera(Vars.gameWidth, Vars.gameHeight);
        this.stage = new Stage(new StretchViewport(Vars.gameWidth, Vars.gameHeight, camera));
        this.player = new Witch(world);
        this.lastSpellSpawn = TimeUtils.nanoTime();
        this.mapRenderer = new OrthogonalTiledMapRenderer(loadMap(map));
        world.setContactListener(new WorldContactListener(player));
        stage.addActor(player);
        mobsCreation();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        actorQueue.forEach(stage::addActor);
        actorQueue.clear();
        actorAct(delta);
        //System.out.println("PlayerX: " + player.getX() + " PlayerY: " + player.getY());
        world.step(delta, 6, 2);
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
        mapRenderer.setView(camera);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();
        debugRenderer.render(world, camera.combined);
        stage.draw();
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
    }

    private Vector2 closerMob() {
        Vector2 closerMob = new Vector2(0, 0);
        Float smallerDistance = Float.MAX_VALUE;
        for (Mob mob : allMobs) {
            if (mob.getDistanceHero() < smallerDistance) {
                smallerDistance = mob.getDistanceHero();
                closerMob = mob.getPosition();
            }
        }
        return closerMob;
    }

    private void actorAct(float delta) {
        // Player
        player.act(delta);
        // Mobs
        allMobs.forEach(mob -> {
            if (mob.isAlive()) {
                mob.act(delta, player);
            } else {
                if (TimeUtils.nanoTime() - lastMobSpawn > 2000000000) {
                    mob.awake(new Vector2(new Random().nextInt(300), new Random().nextInt(300)));
                    lastMobSpawn = TimeUtils.nanoTime();
                }
            }
        });
        // Spells
        allSpells.forEach(spell -> {
            if (spell.isAlive()) {
                spell.act(delta);
            } else {
                if (TimeUtils.nanoTime() - lastSpellSpawn > 1000000000) {
                    spell.awake(player.getPosition());
                    spell.setDestination(closerMob(), player.getPosition());
                    lastSpellSpawn = TimeUtils.nanoTime();
                }
            }
        });
    }

    private void mobsCreation() {
        Mob mobBoss = new PurpleFlameBoss(world, player.getPosition(), 30);
        allMobs.add(mobBoss);
        stage.addActor(mobBoss);
        // Create x spells & mobs
        for (int i = 0; i < 30; i++) {
            // spells
            IceBall iceBall = new IceBall(world);
            allSpells.add(iceBall);
            stage.addActor(iceBall);
            // mobs
            Mob mob = new PurpleFlame(world, i);
            allMobs.add(mob);
            stage.addActor(mob);
        }
    }

    private TiledMap loadMap(String mapPath) {
        // Load the Tiled map
        TiledMap map = new TmxMapLoader().load(mapPath);
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("trees");
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

