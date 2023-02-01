package org.grup7.deheroes.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class HealthBar extends Actor {
    private final Texture emptyBar;
    private final Texture fullBar;
    private final float maxHealth;
    private float currentHealth;
    private float y, x;

    public HealthBar(float maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        emptyBar = new Texture("ui/empty-bar.png");
        fullBar = new Texture("ui/full-bar.png");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(emptyBar, x, y);
        batch.draw(fullBar, x, y, fullBar.getWidth() * currentHealth / maxHealth, fullBar.getHeight());
    }


    public void setHealth(float health) {
        currentHealth = health;
    }

    @Override
    public float getY() {
        return y;
    }

    public void setX_Y(float x, float y) {
        this.y = y;
        this.x = x;
    }

    @Override
    public float getX() {
        return x;
    }

    public void dispose() {
        this.remove();
    }
}
