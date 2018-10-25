/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:44 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.events;

public class HudPauseEvent implements Event {
    private final boolean isPaused;

    public HudPauseEvent(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public String toString() {
        return "HudPauseEvent{" +
                "isPaused=" + isPaused +
                '}';
    }
}
