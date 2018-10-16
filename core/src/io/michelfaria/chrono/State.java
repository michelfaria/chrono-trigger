package io.michelfaria.chrono;

public class State {
    private boolean debug = true;
    private boolean hudPause = false;

    public State() {
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isHudPause() {
        return hudPause;
    }

    public void setHudPause(boolean hudPause) {
        this.hudPause = hudPause;
    }
}
