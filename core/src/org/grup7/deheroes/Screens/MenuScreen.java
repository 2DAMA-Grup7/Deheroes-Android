package org.grup7.deheroes.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.grup7.deheroes.Helpers.AssetManager;
import org.grup7.deheroes.MyGdxGame;

public class MenuScreen implements Screen {
    private final MyGdxGame game;

    private final Stage stage;

    public MenuScreen(MyGdxGame game) {
        this.game = game;
        stage = new Stage();
        stage.addActor(mainMenu());
    }

    @Override
    public void show() {
        AssetManager.MenuMusic.setLooping(true);
        AssetManager.MenuMusic.play();
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
       // game.batch.setProjectionMatrix(camera.combined);
        stage.draw();
        stage.act();

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
    public void hide() {

    }

    @Override
    public void dispose() {
        AssetManager.MenuMusic.dispose();

    }


    private Table mainMenu() {
        final Table table = new Table();
        table.setFillParent(true);

        Window window = new Window("   Menu", AssetManager.UIskin);
        // uso espacios para mover al medio lol, no encuentro otra forma

        TextButton StarButton = new TextButton("Start", AssetManager.UIskin);
        TextButton OnlineButton = new TextButton("Online", AssetManager.UIskin);
        TextButton ChatButton = new TextButton("Chat", AssetManager.UIskin);
        TextButton ExitButton = new TextButton("Exit", AssetManager.UIskin);

        window.add(StarButton).center();
        window.row();
        window.add(OnlineButton).center();
        window.row();
        window.add(ChatButton).center();
        window.row();
        window.add(ExitButton).center();
        table.add(window);

        StarButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();


                game.setScreen(new GameScreen(game));
            }
        });

        ExitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        ChatButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new ChatScreen());
            }
        });
        return table;
    }
}
