package org.grup7.deheroes.actors.heroes;

import com.badlogic.gdx.physics.box2d.World;

import org.grup7.deheroes.Assets;
import org.grup7.deheroes.actors.Actions;

public class Witch extends Hero implements Actions {
    public Witch(World world) {
        super(
                world,
                300,
                300,
                32,
                32,
                100,
                80,
                Assets.Heroes.witch);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

    }
}