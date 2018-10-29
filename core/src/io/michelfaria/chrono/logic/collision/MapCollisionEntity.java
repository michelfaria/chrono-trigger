/*
 * Developed by Michel Faria on 10/29/18 8:11 PM.
 * Last modified 10/25/18 7:46 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.logic.collision;

import com.badlogic.gdx.math.Rectangle;
import io.michelfaria.chrono.interfaces.CollisionEntity;


public class MapCollisionEntity implements CollisionEntity {

    public float x;
    public float y;
    public float width;
    public float height;
    public boolean isCollisionEnabled = true;

    public MapCollisionEntity(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public MapCollisionEntity(Rectangle rectangle) {
        this(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
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
    public void setWidth(float width) {
        this.width = width;
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public boolean isCollisionEnabled() {
        return isCollisionEnabled;
    }
}
