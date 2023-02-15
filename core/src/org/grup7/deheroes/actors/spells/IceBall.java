package org.grup7.deheroes.actors.spells;

import static org.grup7.deheroes.screens.SinglePlayer.removeActorQueue;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import org.grup7.deheroes.Assets;
import org.grup7.deheroes.actors.Actions;

public class IceBall extends Spell implements Actions {
    public IceBall(World world, Vector2 playerPosition, Vector2 destination) {
        super(
                world,
                playerPosition.x - 16,
                playerPosition.y - 16,
                32,
                32,
                Assets.Spells.iceBall
        );
        this.velocity = destination.sub(new Vector2(getX(), getY()));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // Update Mob velocity
        body.setLinearVelocity(velocity.x, velocity.y);
        setX(body.getPosition().x);
        setY(body.getPosition().y);
        if ((getX() > 1000 || getX() < -1000) || (getY() > 1000 || getY() < -1000)) {
            removeActorQueue.add(this);
        }
    }


}