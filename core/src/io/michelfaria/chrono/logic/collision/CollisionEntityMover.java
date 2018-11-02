/*
 * Developed by Michel Faria on 10/29/18 8:42 PM.
 * Last modified 10/28/18 11:22 AM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.logic.collision;

import com.badlogic.gdx.math.Rectangle;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.interfaces.CollisionEntity;

public final class CollisionEntityMover {

    private Game.Context ctx;

    public CollisionEntityMover(Game.Context ctx) {
        this.ctx = ctx;
    }

    /**
     * Moves the entity by the specified numbers.
     *
     * @return If the entity moved successfully, it will return true.
     */
    public boolean moveCollisionEntityBy(CollisionEntity entity, float xMoveSpeed, float yMoveSpeed) {
        final float x = entity.getX();
        final float y = entity.getY();
        final float width = entity.getWidth();
        final float height = entity.getHeight();

        final Rectangle futureXRectangle = new Rectangle(x + xMoveSpeed, y, width, height);

        if (ctx.collisionChecker.collisions(entity, futureXRectangle).size > 0) {
            xMoveSpeed = 0;
        }

        final Rectangle futureYRectangle = new Rectangle(x, y + yMoveSpeed, width, height);

        if (ctx.collisionChecker.collisions(entity, futureYRectangle).size > 0) {
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
