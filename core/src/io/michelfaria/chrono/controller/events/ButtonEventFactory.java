package io.michelfaria.chrono.controller.events;

import io.michelfaria.chrono.controller.Buttons;

@FunctionalInterface
public interface ButtonEventFactory {
    ControllerEvent make(int controllerNumber, Buttons button, ButtonEvent.EventType eventType);
}
