package org.grup7.deheroes.actors.mobs;

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

import org.grup7.deheroes.Vars;
import org.grup7.deheroes.actors.MyActor;
import org.grup7.deheroes.actors.heroes.Hero;

public class Mob extends MyActor {
    private final float maxHP;
    private float HP;
    private TextureRegion[] animation;

    public Mob(World world, float width, float height, float speed, float hp, String texturePath) {
        this.velocity = new Vector2(0, 0);
        this.tick = 0f;
        this.rows = 1;
        this.cols = 6;
        this.speed = speed;
        this.HP = hp;
        this.maxHP = hp;
        this.world = world;
        this.alive = false;
        spritesSetup(texturePath);
        setBounds(Vars.deadPointX, Vars.deadPointY, width, height);
        collisionSetup(world);
    }

    public void act(float delta, Hero hero) {
        if (getHP() < 0) {
            sleep();
        } else {
            // Update time
            tick += delta;
            // Update Mob velocity
            body.setLinearVelocity(velocity.x * speed, velocity.y * speed);
            setX(body.getPosition().x);
            setY(body.getPosition().y);
        }
    }

    public void collisionSetup(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() + getWidth() / 2, getY() + +getHeight() / 2);
        bodyDef.fixedRotation = true;
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(getWidth() / 4, getHeight() / 4);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 0.1F;
        fixtureDef.friction = 0;
        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef).setUserData(this);
        polygonShape.dispose();
        body.setActive(false);
        this.body = body;
    }

    private void spritesSetup(String texturePath) {
        Texture mobSprite = new Texture(Gdx.files.internal(texturePath));
        TextureRegion[][] textureRegions = TextureRegion.split(mobSprite, mobSprite.getWidth() / cols, mobSprite.getHeight() / rows);
        animation = new TextureRegion[cols];
        System.arraycopy(textureRegions[0], 0, animation, 0, cols);
    }

    public void draw(Batch batch, float parentAlpha) {
        Animation<TextureRegion> currentAnimation = new Animation<>(0.1F, animation);
        currentAnimation.setPlayMode(Animation.PlayMode.LOOP);
        batch.draw(currentAnimation.getKeyFrame(tick), body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2, getWidth(), getHeight());
    }

    @Override
    public void awake(Vector2 spawnPoint) {
        super.awake(spawnPoint);
        body.setActive(true);
        setHP(maxHP);
    }

    @Override
    public void sleep() {
        super.sleep();
        body.setActive(false);
    }

    public Body getBody() {
        return body;
    }

    public float getHP() {
        return HP;
    }

    public void setHP(float HP) {
        this.HP = HP;
    }

    public float getMaxHP() {
        return maxHP;
    }
}
