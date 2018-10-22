package io.michelfaria.chrono.controller;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;
import io.michelfaria.chrono.controller.events.ButtonEvent;
import io.michelfaria.chrono.events.EventDispatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ControllerEventEmitter {

    private EventDispatcher eventDispatcher;

    private final Map<Buttons, AtomicBoolean> buttonStateMap = new HashMap<>();
    {
        buttonStateMap.put(Buttons.A, new AtomicBoolean(false));
        buttonStateMap.put(Buttons.B, new AtomicBoolean(false));
    }

    public ControllerEventEmitter(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    public void tick() {
        for (Buttons button : buttonStateMap.keySet()) {
            int amountControllers = Controllers.getControllers().size;
            // Minimum amount of controllers is 0
            amountControllers = amountControllers == 0 ? 1 : amountControllers;

            for (int i = 0; i < amountControllers; i++) {
                final boolean isButtonPressed = Ctrl.isButtonPressed(i, button);
                final AtomicBoolean storedState = buttonStateMap.get(button);

                if (isButtonPressed && !storedState.get()) {
                    eventDispatcher.emitEvent(new ButtonEvent(i, button, ButtonEvent.EventType.PRESS));
                    storedState.set(true);

                } else if (!isButtonPressed && storedState.get()) {
                    eventDispatcher.emitEvent(new ButtonEvent(i, button, ButtonEvent.EventType.RELEASE));
                    storedState.set(false);
                }
            }
        }
    }
}
