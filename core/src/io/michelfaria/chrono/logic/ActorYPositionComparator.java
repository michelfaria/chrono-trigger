package io.michelfaria.chrono.logic;

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
