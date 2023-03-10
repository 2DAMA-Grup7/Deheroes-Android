package org.grup7.deheroes.actors.spells;

import com.badlogic.gdx.physics.box2d.World;

import org.grup7.deheroes.Vars;
import org.grup7.deheroes.actors.Actions;
import org.grup7.deheroes.utils.Assets;

public class IceBall extends Spell implements Actions {
    public IceBall(World world) {
        super(
                world,
                Vars.deadPointX,
                Vars.deadPointY,
                32,
                32,
                Assets.Spells.iceBall
        );
    }

    public void act(float delta) {
        super.act(delta);
        // Update Mob velocity
        body.setLinearVelocity(velocity);
        setPosition(body.getPosition());
        // If spell is out of the map sleep it
        if ((getX() > 1000 || getX() < -250) || (getY() > 1000 || getY() < -250)) {
            sleep();
        }
    }
}