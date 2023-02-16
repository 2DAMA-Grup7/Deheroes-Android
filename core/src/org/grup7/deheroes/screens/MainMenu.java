package org.grup7.deheroes.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.grup7.deheroes.Assets;
import org.grup7.deheroes.ClientLauncher;

public class MainMenu implements Screen {

    private final Stage stage;

    public MainMenu(final ClientLauncher game) {
        stage = new Stage();
        stage.addActor(menuTable(game));

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
        stage.getViewport().update(width, height, true);
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
    dispose();
    }

    private Table menuTable(final ClientLauncher game) {
        Skin skin = new Skin(Gdx.files.internal(Assets.Skin.uiSkin));

        final Table table = new Table();
        table.setFillParent(true);

        Window window = new Window("Menu", skin);
        // uso espacios para mover al medio lol, no encuentro otra forma

        TextButton starButton = new TextButton("Start", skin);
        TextButton onlineButton = new TextButton("Online", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        window.add(starButton).center();
        window.row();
        window.add(onlineButton).center();
        window.row();

        window.add(exitButton).center();
        table.add(window);

        starButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SinglePlayer(Assets.Maps.landOfDeath));
            }
        });

        onlineButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new Multiplayer(Assets.Maps.landOfDeath));
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        return table; }
}