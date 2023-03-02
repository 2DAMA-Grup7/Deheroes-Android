package org.grup7.deheroes.actors.spells;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import org.grup7.deheroes.actors.Actions;
import org.grup7.deheroes.utils.Assets;

public class MeleeHit extends Spell implements Actions {

    public MeleeHit(World world, Vector2 playerPosition) {
        super(
                world,
                playerPosition.x - 16,
                playerPosition.y - 16,
                64,
                64,
                Assets.Spells.explosion
        );
    }

    public void act(float delta) {
        super.act(delta);
    }
}