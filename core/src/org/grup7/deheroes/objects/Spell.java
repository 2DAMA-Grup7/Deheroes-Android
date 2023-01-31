package org.grup7.deheroes.objects;

import com.badlogic.gdx.math.Vector2;

public class Spell {
    int damage;
    Vector2 position;
    int range;

    public Spell(int damage, Vector2 position, int range) {
        this.damage = damage;
        this.position = position;
        this.range = range;
    }
}
