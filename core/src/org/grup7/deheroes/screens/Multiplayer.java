package org.grup7.deheroes.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import org.grup7.deheroes.actors.heroes.Hero;
import org.grup7.deheroes.actors.heroes.Witch;

public class Multiplayer extends SinglePlayer implements Screen {

    public Multiplayer(Game game, String map) {
        super(game, map);
        Hero onlinePlayer = new Witch(world);
        players.add(onlinePlayer);
        stage.addActor(onlinePlayer);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    protected void actorAct(float delta) {
        super.actorAct(delta);
    }
}

