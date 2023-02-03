package org.grup7.deheroes.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;

import org.grup7.deheroes.objects.MainChar;
import org.grup7.deheroes.screens.GameScreen;
import org.grup7.deheroes.screens.OnlineScreen;

public class InputHandler extends InputAdapter implements InputProcessor {

    private final MainChar mainChar;

    public InputHandler(GameScreen screen) {
        mainChar = screen.getMainChar();
    }

    public InputHandler(OnlineScreen screen) {
        mainChar = screen.getMainChar();
    }


    @Override
    public boolean keyDown(int keycode) {
        if (keycode == (Input.Keys.W)) {
            GameScreen.mainChar.setDirection(1);
        }
        if (keycode == (Input.Keys.A)) {
            GameScreen.mainChar.setDirection(2);
        }
        if (keycode == (Input.Keys.S)) {
            GameScreen.mainChar.setDirection(3);
        }
        if (keycode == (Input.Keys.D)) {
            GameScreen.mainChar.setDirection(4);
        }
        mainChar.act(Gdx.graphics.getDeltaTime());
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.W) {
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                keyDown(Input.Keys.A);
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                keyDown(Input.Keys.S);
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                keyDown(Input.Keys.D);
            } else {
                GameScreen.mainChar.setDirection(0);
                mainChar.stopAnimation(1);
            }
        }
        if (keycode == Input.Keys.A) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                keyDown(Input.Keys.W);
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                keyDown(Input.Keys.S);
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                keyDown(Input.Keys.D);
            } else {
                GameScreen.mainChar.setDirection(0);
                mainChar.stopAnimation(2);
            }
        }
        if (keycode == Input.Keys.S) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                keyDown(Input.Keys.W);
            } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                keyDown(Input.Keys.A);
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                keyDown(Input.Keys.D);
            } else {
                GameScreen.mainChar.setDirection(0);
                mainChar.stopAnimation(3);
            }
        }
        if (keycode == Input.Keys.D) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                keyDown(Input.Keys.W);
            } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                keyDown(Input.Keys.A);
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                keyDown(Input.Keys.S);
            } else {
                GameScreen.mainChar.setDirection(0);
                mainChar.stopAnimation(4);
            }
        }
        return false;
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
