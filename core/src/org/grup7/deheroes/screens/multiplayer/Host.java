package org.grup7.deheroes.screens.multiplayer;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ConcurrentLinkedDeque;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Host implements Screen {

    public static ConcurrentLinkedDeque<Actor> actorQueue = new ConcurrentLinkedDeque<>();
    public static ConcurrentLinkedDeque<MyActor> removeActorQueue = new ConcurrentLinkedDeque<>();
    public static ConcurrentLinkedDeque<Mob> allMobs = new ConcurrentLinkedDeque<>();
    public static ConcurrentLinkedDeque<Spell> allSpells = new ConcurrentLinkedDeque<>();
    private final Box2DDebugRenderer debugRenderer;
    private final TiledMapRenderer mapRenderer;
    private final OrthographicCamera camera;
    private final Stage stage;
    private final Hero playerHero;
    private final World world;
    private Socket socket;
    private Hero coopPlayer;


    public Host(Batch prevBatch, String map) {
        this.world = new World(new Vector2(0, 0), true);
        this.debugRenderer = new Box2DDebugRenderer();
        this.camera = new OrthographicCamera(Vars.gameWidth, Vars.gameHeight);
        this.stage = new Stage(new StretchViewport(Vars.gameWidth, Vars.gameHeight, camera), prevBatch);
        this.playerHero = new Witch(world);
        this.mapRenderer = new OrthogonalTiledMapRenderer(loadMap(map));
        world.setContactListener(new WorldContactListener(playerHero));
        stage.addActor(playerHero);
        Mob mobBoss = new PurpleFlameBoss(world, playerHero.getPosition());
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
                Spell spell = new IceBall(world, playerHero.getPosition(), new Vector2(allMobs.getFirst().getX(), allMobs.getFirst().getY()));
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
        playerHero.act(delta);
        allMobs.forEach(mob -> mob.act(delta, playerHero));
        allSpells.forEach(spell -> spell.act(delta));

        setWorld();

        world.step(delta, 6, 2);
        camera.position.set(playerHero.getX(), playerHero.getY(), 0);
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

    //Conectem amb el servidor
    public void connectSocket(){
        try {
            socket = IO.socket("http://alumnes.inspedralbes.cat:7379");
            socket.connect();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    //Enviem la info del mon
    public void setWorld(){
        socket.on(Socket.EVENT_CONNECT, args -> Gdx.app.log("setWorld", "entrem"))
                .on("mobs", args -> {
                    JSONArray mobs = new JSONArray();
                    allMobs.forEach(mobs::put);
                    socket.emit("sendMobs", mobs);
                    try {
                        System.out.println(mobs.get(1).toString());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
    public void setSocket(){
        socket.on(Socket.EVENT_CONNECT, args -> Gdx.app.log("SocketIO", "Connected")).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    playerHero.id = data.getString("id");
                    Gdx.app.log("SocketIO", "My ID: " + playerHero.id);

                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting ID");
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerId = data.getString("id");
                    Gdx.app.log("SocketIO", "New Player Connect: " + playerHero.id);
                    //friendlyPlayers.put(playerId, new Hero(friendHero));

                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting New PlayerID");
                }
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    coopPlayer.id = data.getString("id");
                    System.out.println("Disconnected player: " + coopPlayer.id);
                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
                }
            }
        }).on("playerMoved", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    Double x = data.getDouble("x");
                    Double y = data.getDouble("y");
                    if(coopPlayer.id !=null){
                       coopPlayer.setPosition(x.floatValue(),y.floatValue());
                    }
                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
                }
            }
        }).on("getPlayers", args -> {
            JSONArray objects = (JSONArray) args[0];

        });
    }
}
