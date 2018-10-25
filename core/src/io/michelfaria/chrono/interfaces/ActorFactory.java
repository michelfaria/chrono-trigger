package io.michelfaria.chrono.interfaces;

import com.badlogic.gdx.scenes.scene2d.Actor;

public interface ActorFactory<T extends Actor> {
    T make();
    Class<T> actorClass();
}
