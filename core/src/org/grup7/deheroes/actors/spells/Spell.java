package org.grup7.deheroes.actors.spells;

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

import org.grup7.deheroes.actors.MyActor;

public class Spell extends MyActor {
    protected Animation<TextureRegion> currentAnimation;
    protected TextureRegion[] animation;
    protected Vector2 mobTargeted;
    protected int HP;

    public Spell(World world, float startX, float startY, float width, float height, String texturePath) {
        this.velocity = new Vector2(0, 0);
        this.tick = 0f;
        this.rows = 1;
        this.cols = 5;
        this.world = world;
        this.alive = false;
        this.HP = 1;
        spritesSetup(texturePath);
        setBounds(startX, startY, width, height);
        collisionSetup(world);
    }

    public void act(float delta) {
        if (HP == 0) {
            sleep();
        } else {
            // Update time
            tick += delta;
        }
    }

    public void collisionSetup(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(getX() + getWidth() / 2, getY() + getHeight() / 2);
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(getWidth() / 4, getHeight() / 4);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.isSensor = true;
        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef).setUserData(this);
        polygonShape.dispose();
        this.body = body;
    }

    private void spritesSetup(String texturePath) {
        Texture mobSprite = new Texture(Gdx.files.internal(texturePath));
        TextureRegion[][] textureRegions = TextureRegion.split(mobSprite, mobSprite.getWidth() / cols, mobSprite.getHeight() / rows);
        animation = new TextureRegion[cols];
        System.arraycopy(textureRegions[0], 0, animation, 0, cols);
        currentAnimation = new Animation<>(0F, animation);
    }

    public void setDestination(Vector2 destination, Vector2 heroPosition) {
        this.velocity = destination.sub(heroPosition);
    }

    public void draw(Batch batch, float parentAlpha) {
        currentAnimation = new Animation<>(0.1F, animation);
        currentAnimation.setPlayMode(Animation.PlayMode.LOOP);
        batch.draw(currentAnimation.getKeyFrame(tick), body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2, getWidth(), getHeight());
    }

    @Override
    public void awake(Vector2 spawnPoint) {
        super.awake(spawnPoint);
        setHP(1);
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getMobTargeted() {
        return mobTargeted;
    }

    public void setMobTargeted(Vector2 mobTargeted) {
        this.mobTargeted = mobTargeted;
    }
}
