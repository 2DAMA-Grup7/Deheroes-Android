package org.grup7.deheroes.actors;

import com.badlogic.gdx.math.Vector2;

public class MobPurpleFlameBoss extends Mob {

    public MobPurpleFlameBoss(float width, float height, float speed, String texturePath) {
        super(width, height, speed, texturePath);
    }

    public void act(float delta, Vector2 positionHero) {
        update(delta);
        trackPlayer(delta, positionHero, 5);
    }

}
