package org.grup7.deheroes.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import org.grup7.deheroes.helpers.AssetManager;
import org.grup7.deheroes.utils.Settings;

public class MainChar extends Actor {
    private final Vector2 position;
    private final int width;
    private final int height;
    private final Rectangle collisionRect;

    public MainChar(float x, float y, int width, int height) {
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);
        collisionRect = new Rectangle();
        setBounds(position.x, position.y, width, height);
        setTouchable(Touchable.enabled);
    }

    public void act(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (this.position.y - Settings.MainChar_VELOCITY * delta >= 0) {
                this.position.y -= Settings.MainChar_VELOCITY * delta;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (this.position.y + height + Settings.MainChar_VELOCITY * delta <= Settings.GAME_HEIGHT) {
                this.position.y += Settings.MainChar_VELOCITY * delta;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (this.position.x - Settings.MainChar_VELOCITY * delta >= 0) {
                this.position.x -= Settings.MainChar_VELOCITY * delta;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (this.position.x + width + Settings.MainChar_VELOCITY * delta <= Settings.GAME_WIDTH) {
                this.position.x += Settings.MainChar_VELOCITY * delta;
            }
        }
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
        return AssetManager.sprite;
    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(getTexture(), position.x, position.y, width, height);
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }

}