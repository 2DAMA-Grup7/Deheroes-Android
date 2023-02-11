package org.grup7.deheroes.objects;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import org.grup7.deheroes.Utils.Settings;

import java.util.Random;

public class Mob extends Actor {


    protected final Vector2 position;
    private final int width;
    private final int height;
    private Rectangle collisionRect;
    private float hp;
    private Animation<TextureRegion> animation;
    protected float stateTime;
    private final TextureRegion[] walk;


    public Mob(int width, int height, float hp, Texture mobSheet) {
        this.width = width;
        this.height = height;
        this.hp = hp;
        Random rand = new Random();
        float randomX = rand.nextInt(960);
        float randomY = rand.nextInt(640);
        position = new Vector2(randomX, randomY);
        collisionRect = new Rectangle();
        setBounds(randomX, randomY, width, height);
        setTouchable(Touchable.enabled);
        int rows = 1;
        int cols = 6;
        TextureRegion[][] textureRegions = TextureRegion.split(mobSheet, mobSheet.getWidth() / cols, mobSheet.getHeight() / rows);
        // create TextureRegion arrays for each walking direction
        walk = new TextureRegion[cols];
        System.arraycopy(textureRegions[0], 0, walk, 0, cols);
        stateTime = 0f;

    }


    public void act(float delta, Vector2 positionHero) {
        stateTime += delta;
        if (position.x > positionHero.x) {
            this.position.x -= Settings.Mob_VELOCITY * delta;
        }
        if (position.x < positionHero.x) {
            this.position.x += Settings.Mob_VELOCITY * delta;
        }
        if (position.y > positionHero.y) {
            this.position.y -= Settings.Mob_VELOCITY * delta;
        }
        if (position.y < positionHero.y) {
            this.position.y += Settings.Mob_VELOCITY * delta;
        }
    }

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public TextureRegion getCurrentFrame() {
        return animation.getKeyFrame(stateTime);
    }


    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float frameDuration = 0.1F;
        animation = new Animation<>(frameDuration, walk);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        batch.draw(getCurrentFrame(), position.x, position.y, width, height);
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }

    public void setCollisionRect(Rectangle collisionRect) {
        this.collisionRect = collisionRect;
    }

    public boolean isBoss() {
        return false;
    }
    public void dispose() {
        this.remove();
    }
}
