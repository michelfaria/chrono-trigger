/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:38 PM.
 * Copyright (c) 2018. All rights reserved.
 */

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
