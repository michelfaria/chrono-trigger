/*
 * Developed by Michel Faria on 10/29/18 6:47 PM.
 * Last modified 10/25/18 7:46 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public final class GameInput {

    public static final Map<Buttons, Integer> keyboardMappings = new HashMap<>();

    static {
        keyboardMappings.put(Buttons.DPAD_UP, Input.Keys.UP);
        keyboardMappings.put(Buttons.DPAD_DOWN, Input.Keys.DOWN);
        keyboardMappings.put(Buttons.DPAD_LEFT, Input.Keys.LEFT);
        keyboardMappings.put(Buttons.DPAD_RIGHT, Input.Keys.RIGHT);
        keyboardMappings.put(Buttons.ACTION, Input.Keys.X);
        keyboardMappings.put(Buttons.BACK, Input.Keys.Z);
    }

    public static final Map<Buttons, Integer> controllerMappings = new HashMap<>();

    static {
        controllerMappings.put(Buttons.ACTION, XboxController360.BUTTON_A);
        controllerMappings.put(Buttons.BACK, XboxController360.BUTTON_B);
    }

    public static final Map<Buttons, AtomicBoolean> buttonStateMap = new HashMap<>();

    static {
        buttonStateMap.put(Buttons.ACTION, new AtomicBoolean(false));
        buttonStateMap.put(Buttons.BACK, new AtomicBoolean(false));
    }

    private static final List<GameInputObserver> observers = new ArrayList<>();

    private GameInput() {
    }

    public static boolean isButtonPressed(int controllerNumber, Buttons button) {
        if (controllerNumber == 0 && Controllers.getControllers().size == 0) {
            // No controllers - use keyboard
            return isKeyboardKeyPressed(button);
        }

        if (controllerNumber >= Controllers.getControllers().size) {
            throw new IllegalStateException("Controller is not connected: " + controllerNumber);
        }

        Controller controller = Controllers.getControllers().get(controllerNumber);
        switch (button) {
            case DPAD_UP:
                return controller.getAxis(XboxController360.AXIS_LEFT_Y) < -0.5;
            case DPAD_DOWN:
                return controller.getAxis(XboxController360.AXIS_LEFT_Y) > 0.5;
            case DPAD_LEFT:
                return controller.getAxis(XboxController360.AXIS_LEFT_X) < -0.5;
            case DPAD_RIGHT:
                return controller.getAxis(XboxController360.AXIS_LEFT_X) > 0.5;
            default:
                return controller.getButton(
                        Objects.requireNonNull(
                                controllerMappings.get(button), "Button is not mapped for controller: " + button));
        }
    }

    /**
     * Update the observers if any new button changes happened
     */
    public static void tick() {
        for (Buttons button : buttonStateMap.keySet()) {
            int amountInputs = Controllers.getControllers().size;
            // Minimum amount of inputs is 1
            amountInputs = amountInputs == 0 ? 1 : amountInputs;

            for (int i = 0; i < amountInputs; i++) {
                final boolean isButtonPressed = isButtonPressed(i, button);
                final AtomicBoolean storedState = buttonStateMap.get(button);

                if (isButtonPressed && !storedState.get()) {
                    for (GameInputObserver observer : observers) {
                        observer.buttonPressed(i, button);
                    }
                    storedState.set(true);
                } else if (!isButtonPressed && storedState.get()) {
                    for (GameInputObserver observer : observers) {
                        observer.buttonReleased(i, button);
                    }
                    storedState.set(false);
                }
            }
        }
    }

    public static void addObserver(GameInputObserver observer) {
        observers.add(observer);
    }

    private static boolean isKeyboardKeyPressed(Buttons button) {
        return Gdx.input.isKeyPressed(
                Objects.requireNonNull(keyboardMappings.get(button), "No keyboard mapping for button: " + button));
    }

    public interface GameInputObserver {
        void buttonPressed(int controller, Buttons button);

        void buttonReleased(int controller, Buttons button);
    }

    public static class GameInputObserverAdapter implements GameInputObserver {
        @Override
        public void buttonPressed(int controller, Buttons button) {
        }

        @Override
        public void buttonReleased(int controller, Buttons button) {
        }
    }
}
