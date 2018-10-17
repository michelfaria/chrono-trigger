package io.michelfaria.chrono.actor;

import com.badlogic.gdx.math.Rectangle;
import io.michelfaria.chrono.logic.CollisionContext;

public interface CollisionEntity {

    CollisionContext getCollisionContext();

    float getX();

    void setX(float x);

    float getY();

    void setY(float y);

    float getWidth();

    float getHeight();

    default Rectangle getRectangle() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    boolean isCollisionEnabled();
}