package org.grup7.deheroes.actors.spells;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import org.grup7.deheroes.Assets;
import org.grup7.deheroes.actors.Actions;

public class Explosion extends Spell implements Actions {
    private final boolean flag = true;

    public Explosion(World world, Vector2 playerPosition) {
        super(
                world,
                playerPosition.x - 16,
                playerPosition.y - 16,
                64,
                64,
                Assets.Spells.explosion
        );
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        currentAnimation = new Animation<>(0.5F, animation);
        batch.draw(currentAnimation.getKeyFrame(tick), body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2, getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // Update Mob velocity
        setX(body.getPosition().x);
        setY(body.getPosition().y);
    }

    public boolean isFinished() {
        return currentAnimation.isAnimationFinished(getTick());
    }
}