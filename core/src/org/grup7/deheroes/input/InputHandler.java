package org.grup7.deheroes.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;

import org.grup7.deheroes.actors.Hero;

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
                Gdx.app.exit();
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            player.stop();
        }
        return true;
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