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

public class MainMenu implements Screen {

    private final Stage stage;
    private final Skin skin;

    public MainMenu(final ClientLauncher game) {
        stage = new Stage();
        skin = new Skin(Gdx.files.internal(Assets.Skin.uiSkin));

        TextButton startButton = new TextButton("Start", skin);
        startButton.setPosition(Gdx.graphics.getWidth() / 2F - startButton.getWidth() / 2, Gdx.graphics.getHeight() / 2F);

        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.setPosition(Gdx.graphics.getWidth() / 2F - exitButton.getWidth() / 2, Gdx.graphics.getHeight() / 2F - exitButton.getHeight());


        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SinglePlayer(stage.getBatch(), Assets.Maps.landOfDeath, Assets.Heroes.witch));
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        stage.addActor(startButton);
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