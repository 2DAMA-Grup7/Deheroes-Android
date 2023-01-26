package org.grup7.deheroes;

import com.badlogic.gdx.Game;

import org.grup7.deheroes.helpers.AssetManager;
import org.grup7.deheroes.screens.MenuScreen;

public class MyGdxGame extends Game {
    @Override
    public void create() {
        AssetManager.load();
        setScreen(new MenuScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetManager.dispose();
    }
}