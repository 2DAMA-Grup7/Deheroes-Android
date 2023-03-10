package org.grup7.deheroes.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.grup7.deheroes.Vars;
import org.grup7.deheroes.actors.heroes.Hero;
import org.grup7.deheroes.utils.Assets;

public class Hud {
    private final Label scoreLabel;
    private final Skin skin;
    private final Stage stage;

    public Hud(Hero player) {
        this.skin = new Skin(Gdx.files.internal(Assets.Skin.uiSkin));
        this.scoreLabel = new Label(String.valueOf(0), skin);
        this.stage = new Stage(new StretchViewport(Vars.gameWidth, Vars.gameHeight, new OrthographicCamera()));

        Table table = new Table();
        table.top();
        table.setFillParent(true);
        table.add(new Label("Score", skin)).padTop(10).center();
        table.row();
        table.add(scoreLabel).center();
        table.row();

        stage.addActor(table);
    }


    public void updateScoreLabel(int score) {
        scoreLabel.setText(String.format(String.valueOf(score), skin));
    }

    public Stage getStage() {
        return stage;
    }
}
