package org.grup7.deheroes.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.grup7.deheroes.Helpers.AssetManager;
import org.grup7.deheroes.MyGdxGame;
public class PauseMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private Table table;
    private TextButton resumeButton;
    private TextButton quitButton;

    public PauseMenu(final MyGdxGame game) {
        // Initialize the stage and skin
        stage = new Stage();
        skin = AssetManager.UIskin;

        // Create the table
        table = new Table();
        table.setFillParent(true);
        table.center();

        // Create the resume button
        resumeButton = new TextButton("Resume", skin);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Resume the game
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
            }
        });

        // Create the quit button
        quitButton = new TextButton("Quit", skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Quit the game
                Gdx.app.exit();
            }
        });

        // Add the buttons to the table
        table.add(resumeButton).pad(10);
        table.row();
        table.add(quitButton).pad(10);

        // Add the table to the stage
        stage.addActor(table);
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