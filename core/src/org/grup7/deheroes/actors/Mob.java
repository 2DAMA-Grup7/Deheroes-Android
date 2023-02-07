package org.grup7.deheroes.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Mob extends Actor {
    private final int rows, cols;
    private Rectangle collision;
    private float hp;
    private float tick;
    private float speed;
    private Animation<TextureRegion> currentAnimation;
    private TextureRegion[] animation;


    public Mob(float width, float height, float speed, String texturePath) {
        this.tick = 0f;
        this.rows = 1;
        this.cols = 6;
        this.speed = speed;
        spritesSetup(texturePath);
        setBounds(getX(), getY(), width, height);
    }

    private void spritesSetup(String texturePath) {
        Texture mobSprite = new Texture(Gdx.files.internal(texturePath));
        TextureRegion[][] textureRegions = TextureRegion.split(mobSprite, mobSprite.getWidth() / cols, mobSprite.getHeight() / rows);
        animation = new TextureRegion[cols];
        System.arraycopy(textureRegions[0], 0, animation, 0, cols);
    }


    protected void update(float delta) {
        // Update time
        tick += delta;
        // Update collision
        collision = new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public void trackPlayer(float delta, Vector2 positionHero, int distance) {
        // Move the mob
        if (getX() > positionHero.x + distance) {
            setX(getX() - speed * delta);
        }
        if (getX() < positionHero.x + distance) {
            setX(getX() + speed * delta);
        }
        if (getY() > positionHero.y + distance) {
            setY(getY() - speed * delta);
        }
        if (getY() < positionHero.y + distance) {
            setY(getY() + speed * delta);
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        currentAnimation = new Animation<>(0.1F, animation);
        currentAnimation.setPlayMode(Animation.PlayMode.LOOP);
        batch.draw(currentAnimation.getKeyFrame(tick), getX(), getY(), getWidth(), getHeight());
    }


    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public Rectangle getCollision() {
        return collision;

    }

    public void dispose() {
        this.remove();
    }

    public void act(float delta, Vector2 position) {
    }
}
