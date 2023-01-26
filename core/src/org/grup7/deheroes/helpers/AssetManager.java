package org.grup7.deheroes.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class AssetManager {
    public static Texture sprite;

    public static void load() {
        sprite = new Texture(Gdx.files.internal("heroes/witch.png"));
        sprite.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public static void dispose() {
        sprite.dispose();
    }

}