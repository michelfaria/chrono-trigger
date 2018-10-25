/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:44 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.events;

public interface EventListener {
    boolean handleEvent(Event event);

    default int priority() {
        return 0;
    }
}
