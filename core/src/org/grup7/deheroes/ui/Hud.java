package org.grup7.deheroes.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
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
        table.add(touchpad).padTop(100).padLeft(10);

        stage.addActor(table);
    }

    Touchpad touchpad;
    float touchpadX, touchpadY;


    /*public void addTouchpad(){
        Skin skin = new Skin();
        Texture joystick = new Texture(Gdx.files.internal("Joystick.png"));
        skin.add("joystick",joystick);
        Texture joystickknob = new Texture(Gdx.files.internal("SmallHandleFilled.png"));
        skin.add("knob",joystickknob);
        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        touchpadStyle.background = skin.getDrawable("joystick");
        touchpadStyle.knob = skin.getDrawable("knob");

        touchpad = new Touchpad(1, touchpadStyle);
        touchpad.setBounds(2, 4, 10, 10);
        stage.addActor(touchpad);
    }


    public static void playerMovementAndroid(Touchpad touchpad, Hero player){
        float touchpadX;
        float touchpadY;

        touchpadX = touchpad.getKnobPercentX();
        touchpadY = touchpad.getKnobPercentY();
        System.out.println(touchpadX+"      "+touchpadY);
        if(touchpadX ==0f && touchpadY == 0f){
            player.stopX();
            player.stopY();
        }else{
            if(touchpadX>0.45f){
                player.moveRight();
            }else if(touchpadX<-0.45f){
                player.moveLeft();
            } else {
                player.stopX();
                player.stopY();
            }
            if(touchpadY>0.45f){
                player.moveUp();
            }else if(touchpadY<-0.45f){
                player.moveDown();

            }else {
                player.stopX();
                player.stopY();
            }
        }
    }*/

    public void updateScoreLabel(int score) {
        scoreLabel.setText(String.format(String.valueOf(score), skin));
    }

    public Stage getStage() {
        return stage;
    }
}
