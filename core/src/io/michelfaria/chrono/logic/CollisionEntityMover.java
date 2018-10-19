package io.michelfaria.chrono.logic;

import com.badlogic.gdx.math.Rectangle;
import io.michelfaria.chrono.actor.CollisionEntity;

public final class CollisionEntityMover {

    private CollisionEntityMover() {
    }

    /**
     * Moves the entity by the specified numbers.
     *
     * @return If the entity moved successfully, it will return true.
     */
    public static boolean moveBy(CollisionEntity entity, float xMoveSpeed, float yMoveSpeed) {
        final float x = entity.getX();
        final float y = entity.getY();
        final float width = entity.getWidth();
        final float height = entity.getHeight();
        final CollisionChecker collisionChecker = entity.getCollisionContext().collisionChecker;

        final Rectangle futureXRectangle = new Rectangle(x + xMoveSpeed, y, width, height);

        if (collisionChecker.collisions(entity, futureXRectangle).size > 0) {
            xMoveSpeed = 0;
        }

        final Rectangle futureYRectangle = new Rectangle(x, y + yMoveSpeed, width, height);

        if (collisionChecker.collisions(entity, futureYRectangle).size > 0) {
            yMoveSpeed = 0;
        }
        if (xMoveSpeed != 0 || yMoveSpeed != 0) {
            entity.setX(x + xMoveSpeed);
            entity.setY(y + yMoveSpeed);
            return true;
        }
        return false;
    }
}
