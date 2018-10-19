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
