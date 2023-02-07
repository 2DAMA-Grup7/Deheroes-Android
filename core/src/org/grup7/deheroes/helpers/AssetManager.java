package org.grup7.deheroes.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class AssetManager {
    public static Texture HeroSheet;
    public static Texture PurpleFlameSheet;
    public static Texture PurpleFlameBossSheet;
    public static Texture iceSpellSheet;
    public static Texture explosionSpellSheet;

    public static TiledMap map;

    public static Music MenuMusic;
    public static Music GameScreenMusic;

    public static Sound GameOverSound;

    public static Sound GetHitHeroSound;
    public static Sound GetHitPurpleFlameSound;
    public static Sound GetHitFlameBossSound;

    public static Sound HeroDiesSound;
    public static Sound PurpleFLameDiesSound;
    public static Sound PurpleBossDiesSound;
    public static Sound IceSpellSound;
    public static Skin UIskin;
    public static String chatURL;


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
        // Music

        MenuMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/264295__foolboymedia__sky-loop.wav"));
        GameScreenMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/178374__klankbeeld__estate-february-nl-haanwijk-130217_00.mp3"));

        GameOverSound = Gdx.audio.newSound(Gdx.files.internal("Music/620792__melokacool__game-over.mp3"));

        GetHitHeroSound = Gdx.audio.newSound(Gdx.files.internal("Music/437650__dersuperanton__getting-hit-damage-scream.wav"));
        GetHitPurpleFlameSound = Gdx.audio.newSound(Gdx.files.internal("Music/660769__madpancake__headshot-sound.wav"));
        GetHitFlameBossSound = Gdx.audio.newSound(Gdx.files.internal("Music/639751__ryanz-official__plate-impact.wav"));

        HeroDiesSound = Gdx.audio.newSound(Gdx.files.internal("Music/660769__madpancake__headshot-sound.wav"));
        PurpleFLameDiesSound = Gdx.audio.newSound(Gdx.files.internal("Music/FireBurstStarting.wav"));
        PurpleBossDiesSound = Gdx.audio.newSound(Gdx.files.internal("Music/333496__ebcrosby__die-sound.wav"));

        IceSpellSound = Gdx.audio.newSound(Gdx.files.internal("Music/396499__alonsotm__icespell03.mp3"));


        UIskin = new Skin(Gdx.files.internal("ui/dark-hdpi/Holo-dark-hdpi.json"));

        chatURL = "http://localhost:3000";
    }

    public static void dispose() {
        // PLAYER
        HeroSheet.dispose();

        // NPC
        PurpleFlameSheet.dispose();
        PurpleFlameBossSheet.dispose();

        // SPELLS
        iceSpellSheet.dispose();
        explosionSpellSheet.dispose();

        // MAPS
        map.dispose();

        // Music
        MenuMusic.dispose();

        GameScreenMusic.dispose();
        GameOverSound.dispose();

        GetHitHeroSound.dispose();
        GetHitPurpleFlameSound.dispose();
        GetHitFlameBossSound.dispose();

        HeroDiesSound.dispose();
        PurpleFLameDiesSound.dispose();
        PurpleBossDiesSound.dispose();

        IceSpellSound.dispose();

        UIskin.dispose();


    }

}