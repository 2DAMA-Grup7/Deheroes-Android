package org.grup7.deheroes.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;

public class SpellBoss extends Spell {
    public SpellBoss(int width, int height, float y, float x, int direction, Texture spellSheet) {
        super(width, height, y, x, direction, spellSheet);
    }

    @Override
    public void explosion(float delta, float x, float y) {
        stateTime += delta;
        this.position.x = x;
        this.position.y = y;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float frameDuration = 0.5F;
        animation = new Animation<>(frameDuration, walk);
        animation.setPlayMode(Animation.PlayMode.NORMAL);
        batch.draw(getCurrentFrame(), position.x, position.y, width, height);
    }


}
