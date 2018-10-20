package io.michelfaria.chrono.events;

import java.util.HashSet;
import java.util.Set;

public class EventDispatcher {

    private final Set<EventListener> eventListeners = new HashSet<>();

    public EventDispatcher() {
    }

    public boolean addEventListener(EventListener listener) {
        return eventListeners.add(listener);
    }

    public boolean removeEventListener(EventListener listener) {
        return eventListeners.remove(listener);
    }

    public void emitEvent(Event event) {
        for (EventListener eventListener : eventListeners) {
            eventListener.handleEvent(event);
        }
    }
}
