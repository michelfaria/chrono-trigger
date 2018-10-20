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