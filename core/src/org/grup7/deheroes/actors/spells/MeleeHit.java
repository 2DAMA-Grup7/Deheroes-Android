package org.grup7.deheroes.actors.spells;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

import org.grup7.deheroes.Vars;
import org.grup7.deheroes.actors.Actions;
import org.grup7.deheroes.actors.heroes.Hero;
import org.grup7.deheroes.utils.Assets;

public class MeleeHit extends Spell implements Actions {
    private long lastSpawn;
    private boolean flag;

    public MeleeHit(World world) {
        super(
                world,
                Vars.deadPointX,
                Vars.deadPointY,
                128,
                32,
                Assets.Spells.meleeAttack
        );
        this.lastSpawn = TimeUtils.nanoTime();
        this.flag = false;
    }

    public void act(float delta, Hero player) {
        super.act(delta);
        if (TimeUtils.nanoTime() - lastSpawn > 1000000000) {
            awake(player.getPosition());
            lastSpawn = TimeUtils.nanoTime();
            flag = true;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    sleep();
                    flag = false;
                }
            }, 0.4F);
        }
        if (flag) {
            if (player.isDirection()) {
                body.setTransform(player.getX() + 32, player.getY(), 0);
            } else {
                body.setTransform(player.getX() - 32, player.getY(), 0);
            }
            setPosition(body.getPosition());
        }
    }


}