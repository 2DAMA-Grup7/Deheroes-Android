package org.grup7.deheroes.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.grup7.deheroes.Helpers.AssetManager;
import org.grup7.deheroes.MyGdxGame;

public class PauseMenu implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final TextButton resumeButton;
    private final TextButton quitButton;
    private final MyGdxGame game;

    public PauseMenu(final MyGdxGame game) {
        this.game = game;
        // Initialize the stage and skin
        stage = new Stage();
        skin = AssetManager.UIskin;

        // Create the resume button
        resumeButton = new TextButton("Resume", skin);
        resumeButton.setPosition(Gdx.graphics.getWidth() / 2 - 100, 200);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Resume the game
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
            }
        });

        // Create the quit button
        quitButton = new TextButton("Quit", skin);
        quitButton.setPosition(Gdx.graphics.getWidth() / 2 - 100, 100);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Quit the game
                Gdx.app.exit();
            }
        });

        // Add the buttons to the stage
        stage.addActor(resumeButton);
        stage.addActor(quitButton);
    }

    @Override
    public void show() {
        // Set the input processor to the stage
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Update the stage's viewport
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // Dispose of the stage and skin
        stage.dispose();
        skin.dispose();
    }
}