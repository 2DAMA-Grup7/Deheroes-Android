package org.grup7.deheroes.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import org.grup7.deheroes.helpers.AssetManager;
import org.grup7.deheroes.utils.Settings;

import java.util.Random;

public class Mob extends Actor {


    private final Vector2 position;
    private final int width;
    private final int height;
    private Rectangle collisionRect;
    private float hp;


    public Mob(int width, int height, float hp) {
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

    }


    public void act(float delta, Vector2 positionHero) {
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

    public Texture getTexture() {
        return AssetManager.PurpleFlameSheet;
    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(getTexture(), position.x, position.y, width, height);
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }

    public void setCollisionRect(Rectangle collisionRect) {
        this.collisionRect = collisionRect;
    }

    public void dispose() {
        this.remove();
    }
}
