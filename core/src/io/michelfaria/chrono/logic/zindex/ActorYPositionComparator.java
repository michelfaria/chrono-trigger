/*
 * Developed by Michel Faria on 10/29/18 8:20 PM.
 * Last modified 10/25/18 7:46 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.logic.zindex;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Comparator;

/**
 * Compares the Y positions of actors
 */
public class ActorYPositionComparator implements Comparator<Actor> {

    @Override
    public int compare(Actor ac1, Actor ac2) {
        float difference = ac2.getY() - ac1.getY();
        if (difference > 0) {
            return 1;
        } else if (difference < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
