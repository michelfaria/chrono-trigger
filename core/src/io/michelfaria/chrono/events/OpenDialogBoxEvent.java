package io.michelfaria.chrono.events;

public class OpenDialogBoxEvent implements Event {

    public final String text;

    public OpenDialogBoxEvent(String text) {
        this.text = text;
    }
}
