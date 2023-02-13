package org.grup7.deheroes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.grup7.deheroes.Helpers.AssetManager;
import org.grup7.deheroes.Screens.MenuScreen;


public class MyGdxGame extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
      batch = new SpriteBatch();
      AssetManager.load();
      setScreen(new MenuScreen(this));


    }
    @Override
    public void render(){
        super.render();
    }
    @Override
    public void dispose() {
        super.dispose();
        AssetManager.dispose();
    }


}