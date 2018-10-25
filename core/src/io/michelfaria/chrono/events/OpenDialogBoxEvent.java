/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:44 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.events;

public class OpenDialogBoxEvent implements Event {

    public final String text;

    public OpenDialogBoxEvent(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "OpenDialogBoxEvent{" +
                "text='" + text + '\'' +
                '}';
    }
}
