package org.grup7.deheroes.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class AssetManager {
    public static Texture HeroSheet;
    public static Texture PurpleFlameSheet;
    public static Texture PurpleFlameBossSheet;


    public static void load() {
        HeroSheet = new Texture(Gdx.files.internal("heroes/witch-sheet.png"));
        HeroSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        PurpleFlameSheet = new Texture(Gdx.files.internal("mobs/purple-flame-sheet.png"));
        PurpleFlameSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        PurpleFlameBossSheet = new Texture(Gdx.files.internal("mobs/purple-flame-boss-sheet.png"));
        PurpleFlameBossSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public static void dispose() {
        HeroSheet.dispose();
        PurpleFlameSheet.dispose();
        PurpleFlameBossSheet.dispose();
    }

}