package org.grup7.deheroes.actors.heroes;

import com.badlogic.gdx.physics.box2d.World;

import org.grup7.deheroes.actors.Actions;
import org.grup7.deheroes.utils.Assets;

public class Rogue extends Hero implements Actions {
    public Rogue(World world) {
        super(
                world,
                300,
                300,
                32,
                32,
                100,
                80,
                Assets.Heroes.rogue);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}