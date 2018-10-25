/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:44 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public final class ActorUtil {

    private ActorUtil() {
    }

    public static Rectangle getActorRectangle(Actor actor) {
        return new Rectangle(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
    }
}
