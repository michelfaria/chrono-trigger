package io.michelfaria.chrono.logic;

import com.badlogic.gdx.math.Rectangle;
import io.michelfaria.chrono.actor.CollisionEntity;


public class MapCollisionEntity implements CollisionEntity {

    public CollisionContext collisionContext;
    public float x;
    public float y;
    public float width;
    public float height;
    public boolean isCollisionEnabled = true;

    public MapCollisionEntity(CollisionContext collisionContext, float x, float y, float width, float height) {
        this.collisionContext = collisionContext;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public MapCollisionEntity(CollisionContext collisionContext, Rectangle rectangle) {
        this(collisionContext, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    @Override
    public CollisionContext getCollisionContext() {
        return collisionContext;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public boolean isCollisionEnabled() {
        return isCollisionEnabled;
    }
}
