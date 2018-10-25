/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:38 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.events;

import io.michelfaria.chrono.controller.Buttons;

public class ButtonEvent extends ControllerEvent {

    private final Buttons button;
    private final ButtonEventType eventType;

    public ButtonEvent(int controllerId, Buttons button, ButtonEventType eventType) {
        super(controllerId);
        this.button = button;
        this.eventType = eventType;
    }

    public ButtonEventType getEventType() {
        return eventType;
    }

    public Buttons getButton() {
        return button;
    }

    @Override
    public String toString() {
        return "ButtonEvent{" +
                "button=" + button +
                ", eventType=" + eventType +
                '}';
    }
}
