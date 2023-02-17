package org.grup7.deheroes.actors.mobs;

import static org.grup7.deheroes.screens.SinglePlayer.actorQueue;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import org.grup7.deheroes.Assets;
import org.grup7.deheroes.actors.Actions;
import org.grup7.deheroes.actors.heroes.Hero;
import org.grup7.deheroes.actors.spells.Explosion;
import org.grup7.deheroes.ui.HealthBar;

public class PurpleFlameBoss extends Mob implements Actions {
    private final HealthBar healthBar;
    private final Explosion explosion;

    public PurpleFlameBoss(World world, Vector2 playerPosition) {
        super(
                world,
                500,
                500,
                64,
                64,
                40,
                500,
                2,
                Assets.Mobs.purpleFlameBoss
        );
        this.healthBar = new HealthBar(getHp());
        this.explosion = new Explosion(world, playerPosition);
        actorQueue.add(healthBar);
        actorQueue.add(explosion);
    }

    @Override
    public void act(float delta, Hero hero) {
        super.act(delta, hero);
        trackActor(this, delta, hero.getPosition(), 40);
        healthBar.setHealth(getHp());
        healthBar.setX_Y(getX() + 15 - getWidth() / 2, getY() + 60 - getHeight() / 2);
        explosion.act(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.healthBar.dispose();
    }
}