package org.grup7.deheroes.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import org.grup7.deheroes.utils.Settings;

public class Spell extends Actor {
    protected final Vector2 position;
    protected final int width;
    protected final int height;
    protected final TextureRegion[] walk;
    protected Rectangle collisionRect;
    protected int direction;
    protected Animation<TextureRegion> animation;
    protected float stateTime;


    public Spell(int width, int height, float y, float x, int direction, Texture spellSheet) {
        this.width = width;
        this.height = height;
        this.direction = direction;
        switch (direction) {
            case 1:
                y += 32;
            case 2:
                x -= 32;
            case 3:
                y -= 32;
            case 4:
                x += 32;
        }
        position = new Vector2(x, y);
        collisionRect = new Rectangle();
        int rows = 1;
        int cols = 5;
        TextureRegion[][] textureRegions = TextureRegion.split(spellSheet, spellSheet.getWidth() / cols, spellSheet.getHeight() / rows);
        // create TextureRegion arrays for each walking direction
        walk = new TextureRegion[cols];
        setBounds(x, y, width, height);
        setTouchable(Touchable.enabled);
        System.arraycopy(textureRegions[0], 0, walk, 0, cols);
        stateTime = 0f;
    }


    public void act(float delta, float x, float y,Vector2 startPos) {
        stateTime += delta;
        Vector2 positionMob = new Vector2(x, y);

       //Vector2 destination= new Vector2(((float)Math.sqrt(positionMob.x-startPos.x)),(float)Math.sqrt((positionMob.y-startPos.y)));

        //System.out.println("X: " + startPos.x + " Y: " + startPos.y);
        //System.out.println("X: " + positionMob.x + " Y: " + positionMob.y);
        //System.out.println("X: " + destination.x + " Y: " + destination.y);
        //System.out.println("X: " + (destination.x-startPos.x) + " Y: " + (destination.y-startPos.y));

        if (position.x > positionMob.x) {
            this.position.x -= Settings.Mob_VELOCITY * delta;
        }
        if (position.x < positionMob.x) {
            this.position.x += Settings.Mob_VELOCITY * delta;
        }
        if (position.y > positionMob.y) {
            this.position.y -= Settings.Mob_VELOCITY * delta;
        }
        if (position.y < positionMob.y) {
            this.position.y += Settings.Mob_VELOCITY * delta;
        }
    }

    public void act(float delta) {
        stateTime += delta;

        switch (direction) {
            case 1: {
                this.position.y += Settings.Spell_VELOCITY * delta;
                break;
            }

            case 2: {
                this.position.x -= Settings.Spell_VELOCITY * delta;
                break;
            }

            case 3: {
                this.position.y -= Settings.Spell_VELOCITY * delta;
                break;
            }

            case 4: {
                this.position.x += Settings.Spell_VELOCITY * delta;
                break;
            }
            default:
                this.position.x += Settings.Spell_VELOCITY * delta;
                break;

        }
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
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

    public void dispose() {
        this.remove();
    }

    public void explosion(float delta, float x, float y) {
    }
}
