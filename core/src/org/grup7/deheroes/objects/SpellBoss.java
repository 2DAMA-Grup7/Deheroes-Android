package org.grup7.deheroes.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;

public class SpellBoss extends Spell {
    public SpellBoss(int width, int height, float y, float x, int direction, Texture spellSheet) {
        super(width, height, y, x, direction, spellSheet);
        this.position.x = x;
        this.position.y = y;
    }

    @Override
    public void explosion(float delta) {
        stateTime += delta / 2;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float frameDuration = 0.1F;
        animation = new Animation<>(frameDuration, walk);
        batch.draw(getCurrentFrame(), position.x, position.y, width, height);
    }
}
