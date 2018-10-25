/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:44 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.interfaces;

import com.badlogic.gdx.math.Rectangle;
import io.michelfaria.chrono.logic.CollisionContext;

public interface CollisionEntity extends Positionable, Sizable {

    CollisionContext getCollisionContext();

    boolean isCollisionEnabled();

    default Rectangle getRectangle() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }
}