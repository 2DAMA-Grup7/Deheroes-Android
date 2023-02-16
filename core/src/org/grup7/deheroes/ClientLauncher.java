package org.grup7.deheroes;

import com.badlogic.gdx.Game;

import org.grup7.deheroes.screens.MainMenu;

public class ClientLauncher extends Game {
    @Override
    public void create() {
        // Start main menu
        setScreen(new MainMenu(this));
    }
}
