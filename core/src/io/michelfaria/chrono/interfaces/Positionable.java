/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:44 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.interfaces;

import com.badlogic.gdx.math.Vector2;

import java.util.Collection;

public interface Positionable extends Sizable {
    float getX();

    void setX(float x);

    float getY();

    void setY(float y);

    @Override
    default float getWidth() {
        return 0;
    }

    @Override
    default float getHeight() {
        return 0;
    }

    @Override
    default void setWidth(float width) {
    }

    @Override
    default void setHeight(float height) {
    }

    default Vector2 vec2() {
        return new Vector2(getX(), getY());
    }

    /**
     * Find the Positionable in the List that is closest to "to".
     */
    static <T extends Positionable, R extends Positionable> R findClosest(T to, Collection<R> positionables) {
        if (positionables.size() == 0) {
            throw new IllegalArgumentException("No positionables provided");
        }
        R closest = null;
        if (positionables.size() == 1) {
            // Return the first positionable
            for (R positionable : positionables) {
                return positionable;
            }
        } else {
            assert positionables.size() > 0;
            float closestDistance = Float.MAX_VALUE;
            for (R positionable : positionables) {
                float distance = Vector2.dst(positionable.getX(), positionable.getY(), to.getX(), to.getY());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closest = positionable;
                }
            }
        }
        assert closest != null;
        return closest;
    }
}
