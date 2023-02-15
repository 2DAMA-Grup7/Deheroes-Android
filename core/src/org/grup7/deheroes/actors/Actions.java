package org.grup7.deheroes.actors;

import com.badlogic.gdx.math.Vector2;

public interface Actions {

    // move the mob to the player position
    default void trackActor(MyActor actor, float delta, Vector2 trackedActor, int distance) {
        // Move the mob
        if (actor.getX() > trackedActor.x + distance) {
            actor.setVelocityX(-actor.getSpeed() * delta);
        }
        if (actor.getX() < trackedActor.x + distance) {
            actor.setVelocityX(actor.getSpeed() * delta);
        }
        if (actor.getY() > trackedActor.y + distance) {
            actor.setVelocityY(-actor.getSpeed() * delta);
        }
        if (actor.getY() < trackedActor.y + distance) {
            actor.setVelocityY(actor.getSpeed() * delta);
        }
    }
}
