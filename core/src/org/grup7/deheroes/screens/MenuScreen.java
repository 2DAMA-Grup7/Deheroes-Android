package org.grup7.deheroes.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.grup7.deheroes.MyGdxGame;
import org.grup7.deheroes.utils.Settings;

public class MenuScreen implements Screen {
    public static OrthographicCamera camera;
    private final MyGdxGame game;
    private final Stage stage;
    private final BitmapFont font = new BitmapFont();


    public MenuScreen(MyGdxGame game) {
        this.game = game;
        camera = new OrthographicCamera(Settings.GAME_WIDTH, Settings.GAME_HEIGHT);
        StretchViewport viewport = new StretchViewport(Settings.GAME_WIDTH, Settings.GAME_HEIGHT, camera);
        stage = new Stage(viewport);
    }

    @Override
    public void show() {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();

        textButtonStyle.fontColor = Color.WHITE;

        textButtonStyle.font = font;

        TextButton startButton = new TextButton("Start", textButtonStyle);
        startButton.setPosition(Settings.GAME_WIDTH / 2F - startButton.getWidth() / 2, Settings.GAME_HEIGHT / 2F );

        TextButton exitButton = new TextButton("Exit", textButtonStyle);
        exitButton.setPosition(Settings.GAME_WIDTH / 2F - exitButton.getWidth() / 2, Settings.GAME_HEIGHT / 2F - startButton.getHeight());

        TextButton chatButton = new TextButton("Chat", textButtonStyle);
        chatButton.setPosition(Settings.GAME_WIDTH / 2F -chatButton.getWidth() / 2,Settings.GAME_HEIGHT/ 2F - startButton.getHeight()- exitButton.getHeight() );

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new GameScreen(stage.getBatch(), stage.getViewport()));
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        chatButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new ChatScreen(stage.getBatch(), stage.getViewport()));
            }
        });

        stage.addActor(startButton);
        stage.addActor(exitButton);
        stage.addActor(chatButton);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        stage.draw();
        stage.act(delta);
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
    public void hide() {
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
