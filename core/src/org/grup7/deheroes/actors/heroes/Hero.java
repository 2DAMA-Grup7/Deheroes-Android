package org.grup7.deheroes.actors.heroes;

import static org.grup7.deheroes.screens.SinglePlayer.actorQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.TimeUtils;

import org.grup7.deheroes.actors.MyActor;
import org.grup7.deheroes.actors.spells.IceBall;
import org.grup7.deheroes.ui.HealthBar;

import java.util.ArrayList;

public class Hero extends MyActor {
    private final HealthBar healthBar;
    private final ArrayList<IceBall> allSpells;
    private final Vector2 previousPosition;
    private long lastSpellSpawn;
    private boolean direction;
    private float hp;
    private Animation<TextureRegion> currentAnimation;
    private TextureRegion[] walkDown, walkLeft, walkRight, walkUp;


    public Hero(World world, float startX, float startY, float width, float height, float hp, int speed, String texturePath) {
        this.velocity = new Vector2(0, 0);
        this.speed = speed;
        this.hp = hp;
        this.healthBar = new HealthBar(hp);
        this.tick = 0f;
        this.rows = 4;
        this.cols = 5;
        this.lastSpellSpawn = TimeUtils.nanoTime();
        this.allSpells = new ArrayList<>();
        this.world = world;
        this.previousPosition = new Vector2(startX, startY);
        spritesSetup(texturePath);
        setBounds(startX, startY, width, height);
        setTouchable(Touchable.enabled);
        collisionSetup(world);
        actorQueue.add(healthBar);
    }

    public void collisionSetup(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() + getWidth() / 2, getY() + getHeight() / 2);
        bodyDef.fixedRotation = true;
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(getWidth() / 4, getHeight() / 4);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 2;
        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef).setUserData(this);
        polygonShape.dispose();
        this.body = body;
    }

    public void spritesSetup(String texturePath) {
        Texture heroSprite = new Texture(Gdx.files.internal(texturePath));
        TextureRegion[][] textureRegions = TextureRegion.split(heroSprite, heroSprite.getWidth() / cols, heroSprite.getHeight() / rows);
        // Create TextureRegion arrays for each walking direction
        walkDown = new TextureRegion[cols];
        walkLeft = new TextureRegion[cols];
        walkRight = new TextureRegion[cols];
        walkUp = new TextureRegion[cols];
        for (int i = 0; i < cols; i++) {
            walkDown[i] = textureRegions[0][i];
            walkUp[i] = textureRegions[1][i];
            walkRight[i] = textureRegions[2][i];
            walkLeft[i] = textureRegions[3][i];
        }
        // Start animation
        currentAnimation = new Animation<>(0F, walkDown);
    }

    public void act(float delta) {
        // Update time
        tick += delta;
        // Update player velocity
        body.setLinearVelocity(velocity.x * 60, velocity.y * 60);
        setPosition(body.getPosition());
        // Set Health Bar
        healthBar.setX_Y(getX() - getWidth() / 2, getY() + 30 - getHeight() / 2);
        healthBar.setHealth(getHp());
    }

    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currentAnimation.getKeyFrame(tick), body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2, getWidth(), getHeight());
    }

    public void setAnimation(TextureRegion[] type) {
        this.currentAnimation = new Animation<>(0.1F, type);
        this.currentAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public boolean hasMoved() {
        if (previousPosition.x != getX() || previousPosition.y != getY()) {
            previousPosition.x = getX();
            previousPosition.y = getY();
            return true;
        }
        return false;
    }

    public Body getBody() {
        return body;
    }

    public TextureRegion[] getWalkDown() {
        return walkDown;
    }

    public TextureRegion[] getWalkLeft() {
        return walkLeft;
    }

    public TextureRegion[] getWalkRight() {
        return walkRight;
    }

    public TextureRegion[] getWalkUp() {
        return walkUp;
    }

    public long getLastSpellSpawn() {
        return lastSpellSpawn;
    }

    public void setLastSpellSpawn(long lastSpellSpawn) {
        this.lastSpellSpawn = lastSpellSpawn;
    }

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public boolean isDirection() {
        return direction;
    }

    public ArrayList<IceBall> getAllSpells() {
        return allSpells;
    }


    public void moveUp() {
        velocity.y = speed * Gdx.graphics.getDeltaTime();
        setAnimation(walkUp);
    }

    public void moveRight() {
        velocity.x = speed * Gdx.graphics.getDeltaTime();
        setAnimation(walkRight);
        direction = true;
    }

    public void moveDown() {
        velocity.y = -speed * Gdx.graphics.getDeltaTime();
        setAnimation(walkDown);
    }

    public void moveLeft() {
        velocity.x = -speed * Gdx.graphics.getDeltaTime();
        setAnimation(walkLeft);
        direction = false;
    }

    public void stopX() {
        velocity.x = 0;
        currentAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    public void stopY() {
        velocity.y = 0;
        currentAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    @Override
    public void dispose() {
        super.dispose();
        healthBar.dispose();
        allSpells.clear();
    }
}
