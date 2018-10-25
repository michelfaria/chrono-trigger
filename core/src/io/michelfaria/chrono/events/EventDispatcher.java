/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:38 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.events;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventDispatcher {

    private final List<EventListener> eventListeners = new CopyOnWriteArrayList<>();

    public EventDispatcher() {
    }

    public void addEventListener(EventListener listener) {
        eventListeners.add(listener);
        eventListeners.sort(Comparator.comparingInt(EventListener::priority));
    }

    public void removeEventListener(EventListener listener) {
        eventListeners.remove(listener);
    }

    public void emitEvent(Event event) {
        System.out.println("Emitting event: " + event);
        for (EventListener eventListener : eventListeners) {
            eventListener.handleEvent(event);
        }
    }
}
