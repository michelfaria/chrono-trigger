/*
 * Developed by Michel Faria on 10/29/18 8:20 PM.
 * Last modified 10/28/18 11:15 AM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.logic.zindex;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;


public final class ActorZIndexUpdater {

    public static ActorYPositionComparator comparator = new ActorYPositionComparator();

    private ActorZIndexUpdater() {
    }

    public static void updateActorsZIndex(Array<Actor> actors) {
        actors.sort(comparator);
        for (int i = 0; i < actors.size; i++) {
            actors.get(i).setZIndex(i);
        }
    }
}
