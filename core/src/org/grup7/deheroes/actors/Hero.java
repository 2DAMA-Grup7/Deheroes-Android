package org.grup7.deheroes.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import org.grup7.deheroes.Vars;
import org.grup7.deheroes.input.InputHandler;

public class Hero extends Actor {

    private final Vector2 position;
    private final Vector2 velocity;
    private final int rows, cols;
    private Rectangle collision;
    private float hp;
    private float tick;
    private Animation<TextureRegion> currentAnimation;
    private TextureRegion[] walkDown, walkLeft, walkRight, walkUp;


    public Hero(float x, float y, float hp, String texturePath) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, 0);
        this.hp = hp;
        this.tick = 0f;
        this.rows = 4;
        this.cols = 5;
        spritesSetup(texturePath);
        setBounds(getX(), getY(), Vars.playerWidth, Vars.playerHeight);
        setTouchable(Touchable.enabled);
        Gdx.input.setInputProcessor(new InputHandler(this));
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
        // Update collision
        collision = new Rectangle(getX(), getY(), getWidth(), getHeight());
        // Update velocity player
        position.add(velocity);
    }

    public void draw(Batch batch, float parentAlpha) {
        //super.draw(batch, parentAlpha);
        batch.draw(currentAnimation.getKeyFrame(tick), getX(), getY(), getWidth(), getHeight());
    }

    public void setAnimation(TextureRegion[] type) {
        this.currentAnimation = new Animation<>(0.1F, type);
        this.currentAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    @Override
    public float getX() {
        return position.x;
    }

    @Override
    public float getY() {
        return position.y;
    }

    public Vector2 getPosition() {
        return position;
    }


    public Rectangle getCollision() {
        return collision;
    }


    public void moveRight() {
        velocity.x = Vars.playerSpeed / 10F;
        setAnimation(walkRight);
    }

    public void moveLeft() {
        velocity.x = -Vars.playerSpeed / 10F;
        setAnimation(walkLeft);
    }

    public void moveUp() {
        velocity.y = Vars.playerSpeed / 10F;
        setAnimation(walkUp);
    }

    public void moveDown() {
        velocity.y = -Vars.playerSpeed / 10F;
        setAnimation(walkDown);
    }

    public void stop() {
        velocity.x = 0;
        velocity.y = 0;
        currentAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    public void dispose() {
        this.remove();
    }

}
