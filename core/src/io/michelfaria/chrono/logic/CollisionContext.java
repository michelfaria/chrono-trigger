/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:44 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.logic;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import io.michelfaria.chrono.interfaces.CollisionEntity;
import org.jetbrains.annotations.Nullable;

public class CollisionContext {

    @Nullable
    public TiledMap map;

    public CollisionChecker collisionChecker;
    public Array<CollisionEntity> collisionEntities;

    public CollisionContext() {
        this(null);
    }

    public CollisionContext(@Nullable TiledMap map) {
        this.map = map;
        collisionChecker = new CollisionChecker(this);
        collisionEntities = new Array<>(CollisionEntity.class);
    }

    public void addEntity(CollisionEntity entity) {
        collisionEntities.add(entity);
    }
}
