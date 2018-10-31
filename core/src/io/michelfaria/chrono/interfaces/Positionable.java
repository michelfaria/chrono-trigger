/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:44 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.interfaces;

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
}
