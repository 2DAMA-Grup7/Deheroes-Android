package org.grup7.deheroes.actors.mobs;

import com.badlogic.gdx.physics.box2d.World;

import org.grup7.deheroes.actors.Actions;
import org.grup7.deheroes.actors.heroes.Hero;
import org.grup7.deheroes.utils.Assets;
import org.grup7.deheroes.utils.Config;

public class PurpleFlame extends Mob implements Actions {

    public PurpleFlame(World world) {
        super(
                world,
                32,
                32,
                Config.purpleFlame.speed,
                Config.purpleFlame.hp,
                Config.purpleFlame.points,
                Assets.Sounds.purpleFlameHit,
                Assets.Sounds.purpleFlameDie,
                Assets.Mobs.purpleFlame
        );
    }

    @Override
    public void act(float delta, Hero hero) {
        super.act(delta, hero);
        trackActor(this, delta, hero.getPosition(), 0);
    }
}