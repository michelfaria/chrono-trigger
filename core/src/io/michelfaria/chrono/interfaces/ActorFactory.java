/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:44 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.interfaces;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.Actor;

public interface ActorFactory<T extends Actor> {
    T make(MapProperties props);
    Class<T> actorClass();
}
