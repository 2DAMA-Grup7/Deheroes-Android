package org.grup7.deheroes.actors.heroes;

import static org.grup7.deheroes.screens.SinglePlayer.actorQueue;
import static org.grup7.deheroes.screens.SinglePlayer.allMobs;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

import org.grup7.deheroes.actors.Actions;
import org.grup7.deheroes.actors.mobs.Mob;
import org.grup7.deheroes.actors.spells.IceBall;
import org.grup7.deheroes.utils.Assets;

public class Witch extends Hero implements Actions {
    public Witch(World world) {
        super(
                world,
                300,
                300,
                32,
                32,
                70,
                70,
                Assets.Heroes.witch);
        // spells
        for (int i = 0; i < 30; i++) {
            IceBall iceBall = new IceBall(world);
            getAllSpells().add(iceBall);
            actorQueue.add(iceBall);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        getAllSpells().forEach(spell -> {
            if (spell.isAlive()) {
                spell.act(delta);
            } else {
                if (TimeUtils.nanoTime() - getLastSpellSpawn() > 1000000000) {
                    spell.awake(getPosition());
                    spell.setDestination(closerMob(), getPosition());
                    setLastSpellSpawn(TimeUtils.nanoTime());
                }
            }
        });
    }

    private Vector2 closerMob() {
        Vector2 closerMob = new Vector2(0, 0);
        float smallerDistance = Float.MAX_VALUE;
        for (Mob mob : allMobs) {
            if (mob.getDistanceHero() < smallerDistance) {
                smallerDistance = mob.getDistanceHero();
                closerMob = mob.getPosition();
            }
        }
        return closerMob;
    }


}