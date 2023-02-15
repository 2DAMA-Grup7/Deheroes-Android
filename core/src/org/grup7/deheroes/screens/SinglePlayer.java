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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.grup7.deheroes.Vars;
import org.grup7.deheroes.WorldContactListener;
import org.grup7.deheroes.actors.MyActor;
import org.grup7.deheroes.actors.heroes.Hero;
import org.grup7.deheroes.actors.heroes.Witch;
import org.grup7.deheroes.actors.mobs.Mob;
import org.grup7.deheroes.actors.mobs.PurpleFlame;
import org.grup7.deheroes.actors.mobs.PurpleFlameBoss;
import org.grup7.deheroes.actors.spells.IceBall;
import org.grup7.deheroes.actors.spells.Spell;

import java.util.concurrent.ConcurrentLinkedDeque;

public class SinglePlayer implements Screen {
    public static ConcurrentLinkedDeque<Actor> actorQueue = new ConcurrentLinkedDeque<>();
    public static ConcurrentLinkedDeque<MyActor> removeActorQueue = new ConcurrentLinkedDeque<>();
    public static ConcurrentLinkedDeque<Mob> allMobs = new ConcurrentLinkedDeque<>();
    public static ConcurrentLinkedDeque<Spell> allSpells = new ConcurrentLinkedDeque<>();
    private final Box2DDebugRenderer debugRenderer;
    private final TiledMapRenderer mapRenderer;
    private final OrthographicCamera camera;
    private final Stage stage;
    private final Hero player;
    private final World world;

    public SinglePlayer( String map) {
        this.world = new World(new Vector2(0, 0), true);
        this.debugRenderer = new Box2DDebugRenderer();
        this.camera = new OrthographicCamera(Vars.gameWidth, Vars.gameHeight);
        this.stage = new Stage(new StretchViewport(Vars.gameWidth, Vars.gameHeight, camera));
        this.player = new Witch(world);
        this.mapRenderer = new OrthogonalTiledMapRenderer(loadMap(map));
        world.setContactListener(new WorldContactListener(player));
        stage.addActor(player);
        Mob mobBoss = new PurpleFlameBoss(world, player.getPosition());
        allMobs.add(mobBoss);
        stage.addActor(mobBoss);
        // Create Basic Enemies

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Mob mob = new PurpleFlame(world);
                allMobs.add(mob);
                stage.addActor(mob);
            }
        }, 0, 2);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Spell spell = new IceBall(world, player.getPosition(), new Vector2(allMobs.getFirst().getX(), allMobs.getFirst().getY()));
                allSpells.add(spell);
                stage.addActor(spell);
            }
        }, 0, 1);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        actorQueue.forEach(stage::addActor);
        removeActorQueue.forEach(MyActor::dispose);
        removeActorQueue.clear();
        player.act(delta);
        allMobs.forEach(mob -> mob.act(delta, player));
        allSpells.forEach(spell -> spell.act(delta));
        world.step(delta, 6, 2);
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
        mapRenderer.setView(camera);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();
        debugRenderer.render(world, camera.combined);
        stage.draw();
        actorQueue.clear();
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

