package org.grup7.deheroes.actors.heroes;

import static org.grup7.deheroes.screens.SinglePlayer.actorQueue;

import com.badlogic.gdx.physics.box2d.World;

import org.grup7.deheroes.actors.Actions;
import org.grup7.deheroes.actors.spells.MeleeHit;
import org.grup7.deheroes.utils.Assets;

public class Rogue extends Hero implements Actions {
    private final MeleeHit hit;

    public Rogue(World world) {
        super(
                world,
                300,
                300,
                32,
                32,
                120,
                80,
                Assets.Heroes.rogue);
        this.hit = new MeleeHit(world);
        actorQueue.add(hit);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        hit.act(delta, this);
    }

    @Override
    public void dispose() {
        super.dispose();
        hit.dispose();
    }

}