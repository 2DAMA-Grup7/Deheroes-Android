package org.grup7.deheroes.objects;

import com.badlogic.gdx.Gdx;
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
    private final Vector2 position;
    private final int width;
    private final int height;
    private Rectangle collisionRect;
    private int direction;
    public static Texture iceSpellSheet;
    private Animation<TextureRegion> animation;
    private final TextureRegion[] walk;
    private float stateTime;


    public Spell(int width, int height, float y, float x, int direction) {
        this.width = width;
        this.height = height;
        this.direction = direction;
        position = new Vector2(x,y);
        collisionRect = new Rectangle();
        iceSpellSheet = new Texture(Gdx.files.internal("spells/ice-ball-sheet.png"));
        iceSpellSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        int rows = 1;
        int cols = 5;
        TextureRegion[][] textureRegions = TextureRegion.split(iceSpellSheet, iceSpellSheet.getWidth() / cols, iceSpellSheet.getHeight() / rows);
        // create TextureRegion arrays for each walking direction
        walk = new TextureRegion[cols];
        setBounds(x, y, width, height);
        setTouchable(Touchable.enabled);
        System.arraycopy(textureRegions[0], 0, walk, 0, cols);
        stateTime = 0f;
    }


    public void act(float delta) {
        stateTime += delta;

        switch (direction){
            case 1: {
                this.position.y += Settings.Spell_VELOCITY * delta;
            }

            case 2:{
                this.position.x -= Settings.Spell_VELOCITY * delta;
            }

            case 3:{
                this.position.y -= Settings.Spell_VELOCITY * delta;
            }

            case 4:{
                this.position.x += Settings.Spell_VELOCITY * delta;
            }
            default:
                this.position.x += Settings.Spell_VELOCITY * delta;

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

    public void dispose(){
        this.remove();
    }

    public void setCollisionRect(Rectangle collisionRect) {
        this.collisionRect = collisionRect;
    }

}
