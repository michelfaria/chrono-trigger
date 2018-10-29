/*
 * Developed by Michel Faria on 10/29/18 8:14 PM.
 * Last modified 10/25/18 7:45 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.interfaces;

import com.badlogic.gdx.math.Rectangle;

public interface CollisionEntity extends Positionable, Sizable {

    boolean isCollisionEnabled();

    default Rectangle getRectangle() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }
}