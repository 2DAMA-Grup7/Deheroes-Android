package org.grup7.deheroes.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import org.grup7.deheroes.utils.Settings;

public class MobBoss extends Mob{

    private final int distance;

    public MobBoss(int width, int height, float hp, Texture mobSheet, int distance) {
        super(width, height, hp, mobSheet);
        this.distance = distance;
    }

    @Override
    public void act(float delta, Vector2 positionHero) {
        stateTime += delta;
        if (position.x > positionHero.x+distance) {
            this.position.x -= Settings.Mob_VELOCITY * delta;
        }
        if (position.x < positionHero.x+distance) {
            this.position.x += Settings.Mob_VELOCITY * delta;
        }
        if (position.y > positionHero.y+distance) {
            this.position.y -= Settings.Mob_VELOCITY * delta;
        }
        if (position.y < positionHero.y+distance) {
            this.position.y += Settings.Mob_VELOCITY * delta;
        }
    }
    public boolean isBoss() {
        return true;
    }

}
