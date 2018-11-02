/*
 * Developed by Michel Faria on 10/29/18 8:11 PM.
 * Last modified 10/25/18 7:45 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.logic.collision;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.interfaces.CollisionEntity;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.badlogic.gdx.math.Intersector.intersectRectangles;
import static io.michelfaria.chrono.MapConstants.LAYER_COLLISION;

@ParametersAreNonnullByDefault
public final class CollisionChecker {

    private Game.Context ctx;

    public CollisionChecker(Game.Context ctx) {
        this.ctx = ctx;
    }

    /**
     * Returns all collisions made by the specified CollisionEntity with other CollisionEntities and the map's collidable
     * layer.
     */
    public Array<CollisionEntity> collisions(CollisionEntity entity) {
        return collisions(entity, null);
    }

    /**
     * Returns all collisions made by the specified CollisionEntity with other CollisionEntities in this class's
     * CollisionContext and the map's collidable layer.
     *
     * If a rectangle is specified, then the Rectangle will be used for checking for collisions instead of the
     * CollisionEntity's rectangle.
     */
    public Array<CollisionEntity> collisions(CollisionEntity entity, @Nullable Rectangle rectangle) {
        if (!entity.isCollisionEnabled()) {
            return new Array<>(CollisionEntity.class);
        }
        if (rectangle == null) {
            rectangle = entity.getRectangle();
        }

        Array<CollisionEntity> collisions = mapCollisions(rectangle);
        collisions.addAll(entityCollisions(entity, rectangle));

        return collisions;
    }

    /**
     * Returns all collisions made by the specified CollisionEntity with other CollisionEntities in this class's
     * CollisionContext.
     *
     * If a rectangle is specified, then the Rectangle will be used for checking for collisions instead of the
     * CollisionEntity's rectangle.
     *
     * @implNote The entity parameter is just used to ensure that the entity does not collide with itself.
     */
    public Array<CollisionEntity> entityCollisions(CollisionEntity entity, @Nullable Rectangle rectangle) {
        if (rectangle == null) {
            rectangle = entity.getRectangle();
        }

        Array<CollisionEntity> collisionEntities = new Array<>(CollisionEntity.class);

        for (CollisionEntity otherEntity : ctx.collisionEntities) {
            if (entity != otherEntity
                    && otherEntity.isCollisionEnabled()
                    && intersectRectangles(rectangle, otherEntity.getRectangle(), new Rectangle())) {
                collisionEntities.add(otherEntity);
            }
        }
        return collisionEntities;
    }

    /**
     * Returns all collisions made by the specified Rectangle with the map's collidable layer.
     */
    public Array<CollisionEntity> mapCollisions(Rectangle rectangle) {
        if (ctx.map == null) {
            return new Array<>(CollisionEntity.class);
        }

        MapLayer collisionLayer = ctx.map.getLayers().get(LAYER_COLLISION);

        Array<RectangleMapObject> rectangles = collisionLayer.getObjects().getByType(RectangleMapObject.class);
        Array<CollisionEntity> collisionEntities = new Array<>(CollisionEntity.class);

        for (RectangleMapObject rmo : rectangles) {
            Rectangle mapRectangle = rmo.getRectangle();

            if (intersectRectangles(rectangle, mapRectangle, new Rectangle())) {
                collisionEntities.add(new MapCollisionEntity(mapRectangle));
            }
        }

        return collisionEntities;
    }
}
