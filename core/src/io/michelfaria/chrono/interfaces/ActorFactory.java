package io.michelfaria.chrono.interfaces;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.Actor;

public interface ActorFactory<T extends Actor> {
    T make(MapProperties props);
    Class<T> actorClass();
}
