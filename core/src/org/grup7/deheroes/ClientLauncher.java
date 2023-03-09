package org.grup7.deheroes;

import com.badlogic.gdx.Game;

import org.grup7.deheroes.screens.LoginScreen;
import org.grup7.deheroes.screens.MainMenu;

public class ClientLauncher extends Game {

    @Override
    public void create() {
        setScreen(new LoginScreen(this));
    }
}
