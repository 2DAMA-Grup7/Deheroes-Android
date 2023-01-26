package org.grup7.deheroes.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class AssetManager {
    public static Texture sprite;
    public static Texture spriteSheet;

    public static void load() {
        spriteSheet = new Texture(Gdx.files.internal("heroes/witch-sheet.png"));
        spriteSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public static void dispose() {
        spriteSheet.dispose();
    }

}