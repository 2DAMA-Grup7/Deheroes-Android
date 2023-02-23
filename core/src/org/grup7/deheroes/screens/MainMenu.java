package org.grup7.deheroes.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.grup7.deheroes.Assets;
import org.grup7.deheroes.ClientLauncher;
import org.grup7.deheroes.screens.multiplayer.Host;

public class MainMenu implements Screen {

    private final Stage stage;

    public MainMenu(final ClientLauncher game) {
        stage = new Stage();
        Skin skin = new Skin(Gdx.files.internal(Assets.Skin.uiSkin));

        TextButton startButton = new TextButton("Start", skin);
        startButton.setPosition(Gdx.graphics.getWidth() / 2F - startButton.getWidth() / 2, Gdx.graphics.getHeight() / 2F);

        TextButton onlineButton = new TextButton("Online", skin);
        onlineButton.setPosition(Gdx.graphics.getWidth() / 2F - onlineButton.getWidth() / 2, Gdx.graphics.getHeight()/2F-onlineButton.getHeight());

        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.setPosition(Gdx.graphics.getWidth() / 2F - exitButton.getWidth() / 2, Gdx.graphics.getHeight() / 2F - exitButton.getHeight()*2);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SinglePlayer(stage.getBatch(), Assets.Maps.landOfDeath));
            }
        });

        onlineButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new Host(stage.getBatch(), Assets.Maps.landOfDeath));
            }
        });


        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        stage.addActor(startButton);
        stage.addActor(onlineButton);
        stage.addActor(exitButton);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {

    }
}