package io.michelfaria.chrono.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public final class Keyboard {
    public static boolean isButtonPressed(Buttons button) {
        switch (button) {
            case A:
                return Gdx.input.isKeyPressed(Input.Keys.X);
            case B:
                return Gdx.input.isKeyPressed(Input.Keys.Z);
            case X:
                return Gdx.input.isKeyPressed(Input.Keys.A);
            case Y:
                return Gdx.input.isKeyPressed(Input.Keys.S);
            case DPAD_UP:
                return Gdx.input.isKeyPressed(Input.Keys.DPAD_UP);
            case DPAD_DOWN:
                return Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN);
            case DPAD_LEFT:
                return Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT);
            case DPAD_RIGHT:
                return Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT);
            default:
                throw new RuntimeException("Unknown button: " + button);
        }
    }
}
