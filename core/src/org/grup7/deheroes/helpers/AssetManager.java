package org.grup7.deheroes.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.net.MalformedURLException;
import java.net.URL;

public class AssetManager {
    public static String chatURL;
    public static Texture HeroSheet;
    public static Texture PurpleFlameSheet;
    public static Skin UIskin;

    public static void load()  {
        HeroSheet = new Texture(Gdx.files.internal("heroes/witch-sheet.png"));
        HeroSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        PurpleFlameSheet = new Texture(Gdx.files.internal("mobs/purple-flame.png"));
        PurpleFlameSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        UIskin = new Skin(Gdx.files.internal("ui/Skin/uiskin.json"));

        chatURL = new String("http://localhost:3000");

    }

    public static void dispose() {
        HeroSheet.dispose();
        PurpleFlameSheet.dispose();
    }

}