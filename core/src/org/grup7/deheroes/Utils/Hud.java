package org.grup7.deheroes.Utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.grup7.deheroes.Helpers.AssetManager;


public class Hud {
    public Stage stage;
    private Viewport viewport;
    public static Integer score;
    private Skin skin;
   private static Label scoreLabel;
   private Label playerLabel;

    public Hud(Batch sb){
        skin = AssetManager.UIskin;
        score = 0;

        viewport = new StretchViewport(Settings.GAME_WIDTH,Settings.GAME_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport,sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        playerLabel = new Label("player1",skin);
        scoreLabel = new Label(String.format(String.valueOf(score)), skin) ;

        table.add(playerLabel).padTop(10).center();
        table.row();
        table.add(scoreLabel).center();
        stage.addActor(table);
    }

    public static void addScore(int value) {
        score += value;
        scoreLabel.setText(String.format(String.valueOf(score), AssetManager.UIskin));
    }
}
