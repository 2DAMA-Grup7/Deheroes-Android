package org.grup7.deheroes.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class AssetManager {
    public static Texture HeroSheet;
    public static Texture PurpleFlameSheet;
    public static Texture PurpleFlameBossSheet;
    public static Texture iceSpellSheet;

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
    public static void load() {

        HeroSheet = new Texture(Gdx.files.internal("heroes/witch-sheet.png"));
        HeroSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        PurpleFlameSheet = new Texture(Gdx.files.internal("mobs/purple-flame-sheet.png"));
        PurpleFlameSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        PurpleFlameBossSheet = new Texture(Gdx.files.internal("mobs/purple-flame-boss-sheet.png"));
        PurpleFlameBossSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        iceSpellSheet = new Texture(Gdx.files.internal("spells/ice-ball-sheet.png"));
        iceSpellSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        MenuMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/ music.mp3"));
        GameScreenMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/ music.mp3"));

        GameOverSound = Gdx.audio.newSound(Gdx.files.internal("Music/ music.mp3"));

        GetHitHeroSound = Gdx.audio.newSound(Gdx.files.internal("Music/ music.mp3"));
        GetHitPurpleFlameSound = Gdx.audio.newSound(Gdx.files.internal("Music/ music.mp3"));
        GetHitFlameBossSound = Gdx.audio.newSound(Gdx.files.internal("Music/ music.mp3"));

        HeroDiesSound = Gdx.audio.newSound(Gdx.files.internal("Music/ music.mp3"));
        PurpleFLameDiesSound = Gdx.audio.newSound(Gdx.files.internal("Music/ music.mp3"));
        PurpleBossDiesSound = Gdx.audio.newSound(Gdx.files.internal("Music/ music.mp3"));

        IceSpellSound = Gdx.audio.newSound(Gdx.files.internal("Music/ music.mp3"));

    }

    public static void dispose() {
        HeroSheet.dispose();
        PurpleFlameSheet.dispose();
        PurpleFlameBossSheet.dispose();
        iceSpellSheet.dispose();

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


    }

}