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
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public final class GameInput {

    public static final Map<Buttons, Integer> keyboardMappings = new HashMap<>();

    static {
        keyboardMappings.put(Buttons.DPAD_UP, Input.Keys.UP);
        keyboardMappings.put(Buttons.DPAD_DOWN, Input.Keys.DOWN);
        keyboardMappings.put(Buttons.DPAD_LEFT, Input.Keys.LEFT);
        keyboardMappings.put(Buttons.DPAD_RIGHT, Input.Keys.RIGHT);
        keyboardMappings.put(Buttons.A, Input.Keys.X);
        keyboardMappings.put(Buttons.B, Input.Keys.Z);
        keyboardMappings.put(Buttons.X, Input.Keys.A);
    }

    public static final Map<Buttons, Integer> controllerMappings = new HashMap<>();

    static {
        controllerMappings.put(Buttons.A, XboxController360.BUTTON_A);
        controllerMappings.put(Buttons.B, XboxController360.BUTTON_B);
        controllerMappings.put(Buttons.X, XboxController360.BUTTON_X);
    }

    public static final Map<Buttons, AtomicBoolean> buttonStateMap = new HashMap<>();

    static {
        buttonStateMap.put(Buttons.A, new AtomicBoolean(false));
        buttonStateMap.put(Buttons.B, new AtomicBoolean(false));
        buttonStateMap.put(Buttons.X, new AtomicBoolean(false));
    }

    private static final Queue<GameInputObserver> observers = new PriorityBlockingQueue<>();

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
                    Gdx.app.debug(GameInput.class.getName(), "Button pressed: " + button);
                    for (GameInputObserver observer : observers) {
                        observer.buttonPressed(i, button);
                    }
                    storedState.set(true);

                } else if (!isButtonPressed && storedState.get()) {
                    Gdx.app.debug(GameInput.class.getName(), "Button released: " + button);
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

    public static void removeObserver(GameInputObserver observer) {
        observers.remove(observer);
    }

    private static boolean isKeyboardKeyPressed(Buttons button) {
        return Gdx.input.isKeyPressed(
                Objects.requireNonNull(keyboardMappings.get(button), "No keyboard mapping for button: " + button));
    }

    public static int observersSize() {
        return observers.size();
    }

    public static Set<GameInputObserver> getObserversCopy() {
        return new HashSet<>(observers);
    }

    public interface GameInputObserver extends Comparable<GameInputObserver> {
        void buttonPressed(int controller, Buttons button);

        void buttonReleased(int controller, Buttons button);

        int getPriority();
    }

    public static class GameInputObserverAdapter implements GameInputObserver {

        protected int priority = 0;

        @Override
        public void buttonPressed(int controller, Buttons button) {
        }

        @Override
        public void buttonReleased(int controller, Buttons button) {
        }

        @Override
        public int getPriority() {
            return priority;
        }

        @Override
        public int compareTo(@NotNull GameInput.GameInputObserver o) {
            return Integer.compare(o.getPriority(), priority);
        }
    }
}
