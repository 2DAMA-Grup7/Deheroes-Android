package org.grup7.deheroes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.grup7.deheroes.Vars;
import org.grup7.deheroes.utils.Assets;
import org.grup7.deheroes.utils.connections.userData;
import org.grup7.deheroes.utils.connections.requestInterface;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginScreen implements Screen {
    private final Game game;
    private final Stage stage;
    private final Music music;

    public LoginScreen(Game game){

        this.game = game;
        music = Gdx.audio.newMusic(Gdx.files.internal(Assets.Music.menu));
        music.setLooping(true);
        music.play();
        stage = new Stage();
        stage.addActor(loginTable());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
    public void hide() {
    }

    @Override
    public void dispose() {
        music.stop();
    }

    private Table loginTable(){
        Skin skin = new Skin(Gdx.files.internal(Assets.Skin.uiSkin));
        Window window = new Window("Login", skin);
        TextField userName = new TextField("User" , skin);
        TextField passWd = new TextField("Passwd" , skin);
        TextButton enter = new TextButton("Enter", skin);


        window.row();
        window.add(userName).center();
        window.add(passWd).center();
        window.row();
        window.add(enter);

        Table table = new Table();
        table.setFillParent(true);
        table.add(window);

        enter.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                //game.setScreen(new MainMenu(game));

                JSONObject data = new JSONObject();

                System.out.println(userName.getText());

                try {
                    data.put("username", userName.getText());
                    data.put("password", passWd.getText());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                System.out.println(data);

                connect(data);
            }
        });
        return table;
    }


    protected void connect(JSONObject data) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Vars.apiURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        requestInterface rInterface = retrofit.create(requestInterface.class);
        userData model = new userData(data);
        Call<userData> call = rInterface.createPost(model);
        call.enqueue(new Callback<userData>() {
            @Override
            public void onResponse(Call<userData> call, Response<userData> response) {
                //AQUI EL QUE FACI ON RESPONSE

                System.out.println("Entramos en el response");
                System.out.println(response.body().toString());
            }

            @Override
            public void onFailure(Call<userData> call, Throwable t) {
                //AQUI EL QUE FACI SI TODO SALE MAL
                System.out.println("ERROR :C");
            }
        });

    }


}
