package io.michelfaria.chrono.events;

public class HudPauseEvent implements Event {
    private final boolean isPaused;

    public HudPauseEvent(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public boolean isPaused() {
        return isPaused;
    }
}
