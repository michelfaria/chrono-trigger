package io.michelfaria.chrono.events;

import com.badlogic.gdx.controllers.Controllers;
import io.michelfaria.chrono.events.Event;

public abstract class ControllerEvent implements Event {
    private final int controllerId;

    public ControllerEvent(int controllerId) {
        this.controllerId = controllerId;
    }

    public int getControllerId() {
        return controllerId;
    }
}
