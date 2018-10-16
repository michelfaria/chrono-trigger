package io.michelfaria.chrono.logic;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import io.michelfaria.chrono.actor.CollisionEntity;

public class CollisionContext {
    public TiledMap map;
    public CollisionChecker collisionChecker;
    public Array<CollisionEntity> collisionEntities;

    public CollisionContext(TiledMap map) {
        this.map = map;
        this.collisionChecker = new CollisionChecker(this);
        this.collisionEntities = new Array<>();
    }

    public void add(CollisionEntity entity) {
        this.collisionEntities.add(entity);
    }
}
