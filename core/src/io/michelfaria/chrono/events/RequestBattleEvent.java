/*
 * Developed by Michel Faria on 10/29/18 3:13 PM.
 * Last modified 10/29/18 3:13 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.events;

public class RequestBattleEvent implements Event {
    public final int battleGroupId;

    public RequestBattleEvent(int battleGroupId) {
        this.battleGroupId = battleGroupId;
    }

    @Override
    public String toString() {
        return "RequestBattleEvent{" +
                "battleGroupId=" + battleGroupId +
                '}';
    }
}
