package org.grup7.deheroes.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.grup7.deheroes.MyGdxGame;
import org.grup7.deheroes.helpers.AssetManager;
import org.grup7.deheroes.utils.Settings;

public class MenuScreen implements Screen {
    public static OrthographicCamera camera;
    private final MyGdxGame game;
    private final Stage stage;

    public MenuScreen(MyGdxGame game) {
        this.game = game;
        camera = new OrthographicCamera(Settings.GAME_WIDTH, Settings.GAME_HEIGHT);
        StretchViewport viewport = new StretchViewport(Settings.GAME_WIDTH, Settings.GAME_HEIGHT, camera);
        stage = new Stage(viewport);
        stage.addActor(mainMenu());
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
                game.setScreen(new GameScreen(stage.getBatch(), stage.getViewport()));
            }
        });
        OnlineButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new OnlineScreen(stage.getBatch(), stage.getViewport()));
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
                game.setScreen(new ChatScreen(stage.getBatch(), stage.getViewport()));
            }
        });
        return table;
    }
    @Override
    public void show() {
        AssetManager.MenuMusic.setLooping(true);
        AssetManager.MenuMusic.play();
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        stage.act(delta);


    }

    @Override
    public void resize(int width, int height){
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
}
