package io.michelfaria.chrono.logic;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import io.michelfaria.chrono.actor.CollisionEntity;
import io.michelfaria.chrono.values.LayerNames;
import org.eclipse.jdt.annotation.Nullable;

import static com.badlogic.gdx.math.Intersector.intersectRectangles;

public class CollisionChecker {

    private CollisionContext collisionContext;

    public CollisionChecker(CollisionContext collisionContext) {
        this.collisionContext = collisionContext;
    }

    /**
     * Returns true if the specified Actor's rectangle collides with any objects in
     * "Collision" layer of this class's CollisionContext's TiledMap or other CollisionObjects
     * in this CollisionContext.
     *
     * @param entity     Actor to check for collision
     */
    public boolean collides(CollisionEntity entity) {
        return collides(entity, null);
    }

    /**
     * Returns true if the specified Actor's rectangle collides with any objects in
     * "Collision" layer of this class's CollisionContext's TiledMap or other CollisionObjects
     * in this CollisionContext.
     *
     * @param entity     Actor to check for collision
     * @param rectangle (Optional) Rectangle if you don't want to use the entity's rectangle
     * @return True if there is a collision
     */
    public boolean collides(CollisionEntity entity, @Nullable Rectangle rectangle) {
        Rectangle entityRectangle;
        if (rectangle == null) {
            entityRectangle = entity.getRectangle();
        } else {
            entityRectangle = rectangle;
        }

        if (collidesWithMap(entityRectangle)) {
            return true;
        }
        for (CollisionEntity otherEntity : collisionContext.collisionEntities) {
            if (entity != otherEntity) {
                if (intersectRectangles(entityRectangle, otherEntity.getRectangle(), new Rectangle())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if the specified rectangle collides with any objects in the
     * "Collision" layer of this class's CollisionContext's TiledMap.
     *
     * @param rectangle Rectangle to check for collision
     * @return True if there is a collision
     */
    public boolean collidesWithMap(Rectangle rectangle) {
        MapLayer collisionLayer = collisionContext.map.getLayers().get(LayerNames.COLLISION);
        MapObjects objects = collisionLayer.getObjects();
        Array<RectangleMapObject> rectangles = objects.getByType(RectangleMapObject.class);

        for (RectangleMapObject rectangleMapObject : rectangles) {
            Rectangle mapRectangle = rectangleMapObject.getRectangle();
            if (intersectRectangles(rectangle, mapRectangle, new Rectangle())) {
                return true;
            }
        }

        return false;
    }
}
