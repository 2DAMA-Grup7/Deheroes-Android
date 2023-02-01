package org.grup7.deheroes.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class AssetManager {
    public static Texture HeroSheet;
    public static Texture PurpleFlameSheet;
    public static Texture PurpleFlameBossSheet;
    public static TiledMap map;
    public static Texture iceSpellSheet;
    public static Texture explosionSpellSheet;


    public static void load() {
        // PLAYER
        HeroSheet = new Texture(Gdx.files.internal("heroes/witch-sheet.png"));
        HeroSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        // NPC
        PurpleFlameSheet = new Texture(Gdx.files.internal("mobs/purple-flame-sheet.png"));
        PurpleFlameSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        PurpleFlameBossSheet = new Texture(Gdx.files.internal("mobs/purple-flame-boss-sheet.png"));
        PurpleFlameBossSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        // SPELLS
        iceSpellSheet = new Texture(Gdx.files.internal("spells/ice-ball-sheet.png"));
        iceSpellSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        explosionSpellSheet = new Texture(Gdx.files.internal("spells/explosion-sheet.png"));
        explosionSpellSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        // MAPS
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("maps/tilemap.tmx");
    }

    public static void dispose() {
        HeroSheet.dispose();
        PurpleFlameSheet.dispose();
        PurpleFlameBossSheet.dispose();
        map.dispose();
    }

}