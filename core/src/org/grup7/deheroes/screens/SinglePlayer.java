package org.grup7.deheroes.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
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
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.grup7.deheroes.Vars;
import org.grup7.deheroes.actors.heroes.Hero;
import org.grup7.deheroes.actors.heroes.Witch;
import org.grup7.deheroes.actors.mobs.Mob;
import org.grup7.deheroes.actors.mobs.PurpleFlame;
import org.grup7.deheroes.actors.mobs.PurpleFlameBoss;
import org.grup7.deheroes.input.InputHandler;
import org.grup7.deheroes.ui.Hud;
import org.grup7.deheroes.utils.Assets;
import org.grup7.deheroes.utils.WorldContactListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class SinglePlayer implements Screen {
    public static ArrayList<Actor> actorQueue = new ArrayList<>();
    public static ArrayList<Mob> allMobs = new ArrayList<>();
    public static int score = 0;

    protected final Box2DDebugRenderer debugRenderer;
    protected final TiledMapRenderer mapRenderer;
    protected final OrthographicCamera camera;
    protected final Hud hud;
    protected final Stage stage;
    protected final Hero player;
    protected final World world;
    protected final Game game;
    int time=0;

    protected long lastMobSpawn;



    public SinglePlayer(Game game, String map) {
        this.game = game;
        this.world = new World(new Vector2(0, 0), true);
        this.debugRenderer = new Box2DDebugRenderer();
        this.camera = new OrthographicCamera(Vars.gameWidth, Vars.gameHeight);
        this.stage = new Stage(new StretchViewport(Vars.gameWidth, Vars.gameHeight, camera));
        this.hud = new Hud();
        this.mapRenderer = new OrthogonalTiledMapRenderer(loadMap(map));
        Hero player = new Witch(world);
        this.player = player;
        world.setContactListener(new WorldContactListener(player));
        stage.addActor(player);
        addTouchpad();
        Gdx.input.setInputProcessor(new InputHandler(player));

        mobsCreation();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if (player.getHp() < 0) {
            JSONObject data = new JSONObject();
            try {
                data.put("score" , score);
                data.put("time" , time);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            connect(data);

            Gdx.audio.newSound(Gdx.files.internal(Assets.Sounds.gameOver)).play();
            dispose();
            game.setScreen(new GameOver(game));
        } else {
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

    protected void actorAct(float delta) {
        // Player
        player.act(delta);
        // Mobs
        allMobs.forEach(mob -> {
            if (mob.isAlive()) {
                mob.act(delta, player);
            } else {
                if (TimeUtils.nanoTime() - lastMobSpawn > 2000000000) {
                    mob.awake(getMobSpawnPosition());
                    lastMobSpawn = TimeUtils.nanoTime();
                }
            }
        });
    }

    Touchpad touchpad; float touchpadX, touchpadY;

    public void addTouchpad(){
        Skin skin = new Skin();
        Texture joystick = new Texture(Gdx.files.internal("Joystick.png"));
        skin.add("joystick",joystick);
        Texture joystickknob = new Texture(Gdx.files.internal("SmallHandleFilled.png"));
        skin.add("knob",joystickknob);
        skin.setScale(1/32f);
        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        touchpadStyle.background = skin.getDrawable("joystick");
        touchpadStyle.knob = skin.getDrawable("knob");

        touchpad = new Touchpad(1, touchpadStyle);
        touchpad.setBounds(2, 4, 10, 10);
        stage.addActor(touchpad);
    }

    protected Vector2 getMobSpawnPosition() {
        return new Vector2(new Random().nextInt(300), new Random().nextInt(300));
    }

    protected void mobsCreation() {
        Mob mobBoss = new PurpleFlameBoss(world, player.getPosition());
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
    private void connect(JSONObject data){
        Net.HttpRequest httpPOST = new Net.HttpRequest(Net.HttpMethods.POST);
        httpPOST.setUrl(Vars.scoreURL);
        Preferences prefs = Gdx.app.getPreferences("accessToken");
        httpPOST.setHeader("x-access-token" , prefs.getString("token") );
        try {
            httpPOST.setContent("score=" + data.getInt("score") + "&time=" + data.getInt("time"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Gdx.net.sendHttpRequest(httpPOST, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Gdx.app.log("MSG", httpResponse.getResultAsString());
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("Send", "was NOT successful!");
            }

            @Override
            public void cancelled() {
                Gdx.app.log("Send", "was cancelled!");
            }
        });
    }

}

