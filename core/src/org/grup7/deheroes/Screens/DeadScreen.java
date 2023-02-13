package org.grup7.deheroes.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.grup7.deheroes.Helpers.AssetManager;
import org.grup7.deheroes.MyGdxGame;
import org.grup7.deheroes.Utils.Hud;
public class DeadScreen implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final MyGdxGame game;
    public DeadScreen() {
        this.game = new MyGdxGame();
        int score = Hud.getScore();
        // Initialize the stage and skin
        stage = new Stage();
        skin = AssetManager.UIskin;

        // Create the table
        Table table = new Table();
        table.setFillParent(true);

        // Create the score label
        Label scoreLabel = new Label("Your score: " + score, skin);
        scoreLabel.setFontScale(2);

        // Create the play again button
        TextButton playAgainButton = new TextButton("Play Again", skin);
        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                game.setScreen(new GameScreen(game));
            }
        });

        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Quit the game
                Gdx.app.exit();
            }
        });

        table.add(scoreLabel).top().colspan(2).pad(20);
        table.row();
        table.add(playAgainButton).width(150).pad(10);
        table.add(quitButton).width(150).pad(10);

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