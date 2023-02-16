package org.grup7.deheroes.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import org.grup7.deheroes.Assets;

public class HealthBar extends Actor {
    private final Texture emptyBar;
    private final Texture fullBar;
    private final float maxHealth;
    private float currentHealth;

    public HealthBar(float maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.emptyBar = new Texture(Assets.UI.hpBarEmpty);
        this.fullBar = new Texture(Assets.UI.hpBarFull);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(emptyBar, getX(), getY());
        batch.draw(fullBar, getX(), getY(), fullBar.getWidth() * currentHealth / maxHealth, fullBar.getHeight());
    }

    public void setHealth(float health) {
        currentHealth = health;
    }

    public void setX_Y(float x, float y) {
        setY(y);
        setX(x);
    }

    public void dispose() {
        this.remove();
    }
}
