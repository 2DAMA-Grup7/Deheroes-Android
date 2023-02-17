package org.grup7.deheroes.actors.spells;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;

import org.grup7.deheroes.actors.Actions;
import org.grup7.deheroes.actors.heroes.Hero;
import org.grup7.deheroes.utils.Assets;

public class Explosion extends Spell implements Actions {
    private boolean flag;

    public Explosion(World world, Vector2 playerPosition) {
        super(
                world,
                playerPosition.x - 16,
                playerPosition.y - 16,
                64,
                64,
                Assets.Spells.explosion
        );
        this.flag = true;
        this.body.setActive(false);
    }

    public void act(float delta, Hero player) {
        super.act(delta);
        if (isFinished() && flag) {
            flag = false;
            body.setActive(true);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    body.setTransform(player.getPosition(), 0);
                    body.setActive(false);
                    tick = 0;
                    flag = true;
                }
            }, 0.4F);
        }
        setPosition(body.getPosition());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        currentAnimation = new Animation<>(0.2F, animation);
        batch.draw(currentAnimation.getKeyFrame(tick), body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2, getWidth(), getHeight());
    }

    public boolean isFinished() {
        return currentAnimation.isAnimationFinished(tick);
    }
}