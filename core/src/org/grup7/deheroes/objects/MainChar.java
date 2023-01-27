package org.grup7.deheroes.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import org.grup7.deheroes.helpers.AssetManager;
import org.grup7.deheroes.screens.GameScreen;
import org.grup7.deheroes.utils.Settings;

public class MainChar extends Actor {
    public static int direction;

    private final float frameDuration = 0.1F;
    private final TextureRegion[] walkDown;
    private final TextureRegion[] walkLeft;
    private final TextureRegion[] walkRight;
    private final TextureRegion[] walkUp;
    private final Vector2 position;
    private final int width;
    private final int height;
    private Rectangle collisionRect;
    private Animation<TextureRegion> animation;
    private float stateTime;
    private float prev_x;
    private float prev_y;

    public MainChar(float x, float y, int width, int height) {
        this.width = width;
        this.height = height;
        prev_x = 0;
        prev_y = 0;
        position = new Vector2(x, y);
        setBounds(position.x, position.y, width, height);
        setTouchable(Touchable.enabled);
        // use TextureRegion.split() to create 2D array of TextureRegions
        int rows = 4;
        int cols = 5;
        TextureRegion[][] textureRegions = TextureRegion.split(AssetManager.spriteSheet, AssetManager.spriteSheet.getWidth() / cols, AssetManager.spriteSheet.getHeight() / rows);

        // create TextureRegion arrays for each walking direction
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
        animation = new Animation<>(0f, walkDown);
        stateTime = 0f;
    }

    public void act(float delta) {
        stateTime += delta;
        if (direction == 1) {
            prev_x = GameScreen.mainChar.getX();
            animation = new Animation<>(frameDuration, walkUp);
            animation.setPlayMode(Animation.PlayMode.LOOP);
            if (this.position.y + height + Settings.MainChar_VELOCITY * delta <= Settings.GAME_HEIGHT) {
                this.position.y += Settings.MainChar_VELOCITY * delta;
            }
        }
        if (direction == 2) {
            prev_y = GameScreen.mainChar.getY();
            animation = new Animation<>(frameDuration, walkLeft);
            animation.setPlayMode(Animation.PlayMode.LOOP);
            if (this.position.x - Settings.MainChar_VELOCITY * delta >= 0) {
                this.position.x -= Settings.MainChar_VELOCITY * delta;
            }
        }
        if (direction == 3) {
            prev_x = GameScreen.mainChar.getX();
            animation = new Animation<>(frameDuration, walkDown);
            animation.setPlayMode(Animation.PlayMode.LOOP);
            if (this.position.y - Settings.MainChar_VELOCITY * delta >= 0) {
                this.position.y -= Settings.MainChar_VELOCITY * delta;
            }
        }
        if (direction == 4) {
            prev_y = GameScreen.mainChar.getY();
            animation = new Animation<>(frameDuration, walkRight);
            animation.setPlayMode(Animation.PlayMode.LOOP);
            if (this.position.x + width + Settings.MainChar_VELOCITY * delta <= Settings.GAME_WIDTH) {
                this.position.x += Settings.MainChar_VELOCITY * delta;
            }
        }
    }

    public void stopAnimation(int idle) {
        switch (idle) {
            case 1:
                animation = new Animation<>(frameDuration, walkUp);
                animation.setPlayMode(Animation.PlayMode.NORMAL);
                direction = 0;
                break;
            case 2:
                animation = new Animation<>(frameDuration, walkLeft);
                animation.setPlayMode(Animation.PlayMode.NORMAL);
                direction = 0;
                break;

            case 3:
                animation = new Animation<>(frameDuration, walkDown);
                animation.setPlayMode(Animation.PlayMode.NORMAL);
                direction = 0;
                break;
            case 4:
                animation = new Animation<>(frameDuration, walkRight);
                animation.setPlayMode(Animation.PlayMode.NORMAL);
                direction = 0;
                break;

        }
    }



    public void setPlayer_x(float player_x) {
        this.position.x = player_x;
    }



    public void setPlayer_y(float player_y) {
        this.position.y = player_y;
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
        batch.draw(getCurrentFrame(), position.x, position.y, width, height);
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }

    public void setCollisionRect(Rectangle collisionRect) {
        this.collisionRect = collisionRect;
    }

    public float getPrev_x() {
        return prev_x;
    }

    public void setPrev_x(float prev_x) {
        this.prev_x = prev_x;
    }

    public float getPrev_y() {
        return prev_y;
    }

    public void setPrev_y(float prev_y) {
        this.prev_y = prev_y;
    }


}
