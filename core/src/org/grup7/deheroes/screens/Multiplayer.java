package org.grup7.deheroes.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import org.grup7.deheroes.actors.heroes.Penguin;
import org.grup7.deheroes.actors.heroes.Witch;
import org.grup7.deheroes.actors.mobs.Mob;
import org.grup7.deheroes.actors.mobs.PurpleFlame;
import org.grup7.deheroes.actors.mobs.PurpleFlameBoss;
import org.grup7.deheroes.actors.spells.IceBall;
import org.grup7.deheroes.actors.spells.Spell;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class Multiplayer implements Screen {

    public static ConcurrentLinkedDeque<Mob> allMobs = new ConcurrentLinkedDeque<>();
    public static ConcurrentLinkedDeque<Spell> allSpells = new ConcurrentLinkedDeque<>();
    public static ConcurrentLinkedDeque<Actor> actorQueue = new ConcurrentLinkedDeque<>();
    public static ConcurrentLinkedDeque<MyActor> removeActorQueue = new ConcurrentLinkedDeque<>();


    private final Box2DDebugRenderer debugRenderer;
    private final TiledMapRenderer mapRenderer;
    private final OrthographicCamera camera;
    private final Stage stage;

    private final World world;
    private Socket socket;
    private final Hero player;
    String id;
    Penguin playerPlayer;
    Texture playerPenguin;
    Texture friendPenguin;
    HashMap<String, Witch> friendlyPlayers;

    private final float UPDATE_TIME = 1/60f;
    float timer;

    public Multiplayer(String map) {
        playerPenguin = new Texture("sprites/heroes/penguin.png");
        friendPenguin = new Texture("sprites/heroes/penguin.png");
        this.world = new World(new Vector2(0, 0), true);
        this.debugRenderer = new Box2DDebugRenderer();
        this.camera = new OrthographicCamera(Vars.gameWidth, Vars.gameHeight);
        this.stage = new Stage(new StretchViewport(Vars.gameWidth, Vars.gameHeight, camera));
        this.player = new Witch(world);
        this.mapRenderer = new OrthogonalTiledMapRenderer(loadMap(map));
        world.setContactListener(new WorldContactListener(player));

        Mob mobBoss = new PurpleFlameBoss(world, player.getPosition());
        allMobs.add(mobBoss);
        stage.addActor(mobBoss);


        friendlyPlayers = new HashMap<String, Witch>();

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
        connectSocket();
        setSocket();
    }

    public void updateServer(float dt){
        timer += dt;
        if(timer>= UPDATE_TIME && playerPlayer != null && playerPlayer.hasMoved()){
            JSONObject data = new JSONObject();
            try{
                data.put("x", playerPlayer.getX());
                data.put("y", playerPlayer.getY());
                socket.emit("playerMoved",data);
            }catch (JSONException e){
                Gdx.app.log("Socket.io","Error sending update data");
            }
        }
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateServer(Gdx.graphics.getDeltaTime());
        actorQueue.forEach(stage::addActor);
        removeActorQueue.forEach(MyActor::dispose);
        removeActorQueue.clear();
        mapRenderer.render();

        stage.getBatch().begin();

        if (player != null){
            stage.addActor(player);
            camera.position.set(player.getX(), player.getY(), 0);

        }
       if(friendlyPlayers !=null){ for(Map.Entry<String,Witch>entry: friendlyPlayers.entrySet()){


        }}

       for(HashMap.Entry<String, Witch> entry : friendlyPlayers.entrySet())
           stage.addActor(entry.getValue());

        stage.getBatch().end();


        world.step(delta, 6, 2);
        camera.update();
        mapRenderer.setView(camera);

        debugRenderer.render(world, camera.combined);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        camera.zoom = 1;
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
        playerPenguin.dispose();
        friendPenguin.dispose();
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
    public void handleInput(float dt){
        if(playerPlayer != null) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                playerPlayer.setPosition(playerPlayer.getX() + (-200 * dt), playerPlayer.getY());
            } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                playerPlayer.setPosition(playerPlayer.getX() + (+200 * dt), playerPlayer.getY());
            }
        }
    }
    public void connectSocket(){
        try {
            socket = IO.socket("http://localhost:3000");
            socket.connect();
        } catch(Exception e){
            System.out.println(e);
        }
    }
    public void setSocket(){
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gdx.app.log("SocketIO", "Connected");
                playerPlayer = new Penguin(playerPenguin);


            }
        }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    id = data.getString("id");
                    Gdx.app.log("SocketIO", "My ID: " + id);

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
                    Gdx.app.log("SocketIO", "New Player Connect: " + id);
                    friendlyPlayers.put(playerId, new Witch(world));


                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting New PlayerID");
                }
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    id = data.getString("id");
                    friendlyPlayers.remove(id);
                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
                }
            }
        }).on("playerMoved", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                  String playerId = data.getString("id");

                  Double x = data.getDouble("x");
                  Double y = data.getDouble("y");
                  if(friendlyPlayers.get(playerId) !=null){
                      friendlyPlayers.get(playerId).setPosition(x.floatValue(),y.floatValue());
                  }
                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
                }
            }
        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray objects = (JSONArray) args[0];
                try {
                    for(int i = 0; i < objects.length(); i++){
                        Witch coopPlayer = new Witch(world);
                        Vector2 position = new Vector2();
                        position.x = ((Double) objects.getJSONObject(i).getDouble("x")).floatValue();
                        position.y = ((Double) objects.getJSONObject(i).getDouble("y")).floatValue();
                        coopPlayer.setPosition(position.x, position.y);

                        friendlyPlayers.put(objects.getJSONObject(i).getString("id"), coopPlayer);
                    }
                } catch(JSONException e){}
            }
        });
    }

    }

