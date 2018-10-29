/*
 * Developed by Michel Faria on 10/29/18 8:31 PM.
 * Last modified 10/26/18 5:39 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.actors;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.michelfaria.chrono.interfaces.ActorFactory;

import java.util.Objects;

import static io.michelfaria.chrono.MapConstants.PROP_ENTRYPOINT_ID;

public class EntryPoint extends Actor {
    public int id;

    public EntryPoint(int id) {
        this.id = id;
        System.out.println(toString());
    }

    public static class Factory implements ActorFactory<EntryPoint> {

        @Override
        public EntryPoint make(MapProperties props) {
            int id = (int) Objects.requireNonNull(props.get(PROP_ENTRYPOINT_ID), "Property " + PROP_ENTRYPOINT_ID + " not found");
            return new EntryPoint(id);
        }

        @Override
        public Class<EntryPoint> actorClass() {
            return EntryPoint.class;
        }
    }

    @Override
    public String toString() {
        return "EntryPoint{" +
                "id=" + id +
                ", x=" + getX() +
                ", y=" + getY() +
                '}';
    }
}
