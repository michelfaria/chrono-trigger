package io.michelfaria.chrono.controller.events;

import io.michelfaria.chrono.controller.Buttons;

public class ButtonEvent extends ControllerEvent {

    public enum EventType {
        PRESS, RELEASE
    }

    private final Buttons button;
    private final EventType eventType;

    public ButtonEvent(int controllerId, Buttons button, EventType eventType) {
        super(controllerId);
        this.button = button;
        this.eventType = eventType;
    }

    public EventType getEventType() {
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
