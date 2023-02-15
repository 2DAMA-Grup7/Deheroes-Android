package org.grup7.deheroes.actors.mobs;

import com.badlogic.gdx.physics.box2d.World;

import org.grup7.deheroes.Assets;
import org.grup7.deheroes.actors.Actions;
import org.grup7.deheroes.actors.heroes.Hero;

import java.util.Random;

public class PurpleFlame extends Mob implements Actions {

    public PurpleFlame(World world) {
        super(
                world,
                new Random().nextInt(960),
                new Random().nextInt(640),
                32,
                32,
                60,
                50,
                0,
                Assets.Mobs.purpleFlame
        );
    }

    @Override
    public void act(float delta, Hero hero) {
        super.act(delta, hero);
        trackActor(this, delta, hero.getPosition(), 0);
    }
}