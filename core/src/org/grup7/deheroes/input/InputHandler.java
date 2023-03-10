package org.grup7.deheroes.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import org.grup7.deheroes.actors.heroes.Hero;

public class InputHandler extends InputAdapter implements InputProcessor {
    private final Hero player;

    public InputHandler(Hero player) {
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.D:
                player.moveRight();
                break;
            case Input.Keys.A:
                player.moveLeft();
                break;
            case Input.Keys.S:
                player.moveDown();
                break;
            case Input.Keys.W:
                player.moveUp();
                break;
            case Input.Keys.ESCAPE:
                //Gdx.app.exit();
                System.exit(0);
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.D:
                if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                    player.moveLeft();
                } else {
                    player.stopX();
                    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                        player.setAnimation(player.getWalkUp());
                    } else if ((Gdx.input.isKeyPressed(Input.Keys.S))) {
                        player.setAnimation(player.getWalkDown());
                    }
                }
                break;
            case Input.Keys.A:
                if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    player.moveRight();
                } else {
                    player.stopX();
                    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                        player.setAnimation(player.getWalkUp());
                    } else if ((Gdx.input.isKeyPressed(Input.Keys.S))) {
                        player.setAnimation(player.getWalkDown());
                    }
                }
                break;
            case Input.Keys.S:
                if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                    player.moveUp();
                } else {
                    player.stopY();
                    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                        player.setAnimation(player.getWalkLeft());
                    } else if ((Gdx.input.isKeyPressed(Input.Keys.D))) {
                        player.setAnimation(player.getWalkRight());
                    }
                }
                break;
            case Input.Keys.W:
                if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                    player.moveDown();
                } else {
                    player.stopY();
                    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                        player.setAnimation(player.getWalkLeft());
                    } else if ((Gdx.input.isKeyPressed(Input.Keys.D))) {
                        player.setAnimation(player.getWalkRight());
                    }
                }
                break;
        }
        return true;
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
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}