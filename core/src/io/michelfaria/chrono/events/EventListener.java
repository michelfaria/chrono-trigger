package io.michelfaria.chrono.events;

public interface EventListener {
    boolean handleEvent(Event event);

    default int priority() {
        return 0;
    }
}
