/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:44 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.logic;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import io.michelfaria.chrono.actor.BattlePoint;
import io.michelfaria.chrono.actor.Nu;
import io.michelfaria.chrono.events.EventDispatcher;
import io.michelfaria.chrono.interfaces.ActorFactory;


import java.util.ArrayList;
import java.util.List;

import static io.michelfaria.chrono.consts.MapConstants.*;

public class TiledMapStagePopulator {

    private List<ActorFactory<?>> actorFactoryList = new ArrayList<>();

    public TiledMapStagePopulator(CollisionContext collisionContext, EventDispatcher eventDispatcher, TextureAtlas textureAtlas) {
        actorFactoryList.add(new Nu.NuFactory(collisionContext, eventDispatcher, textureAtlas));
        actorFactoryList.add(new BattlePoint.Factory());
    }

    public void populate(TiledMap map, Stage stage) {
        MapLayers layers = map.getLayers();

        for (MapLayer layer : layers) {
            if (layer.getName().equals(LAYER_ENTITY)) {
                populateEntityLayer(layer, stage);
            }
        }
    }

    private void populateEntityLayer(MapLayer layer, Stage stage) {
        Array<RectangleMapObject> objects = layer.getObjects().getByType(RectangleMapObject.class);

        for (RectangleMapObject object : objects) {

            MapProperties props = object.getProperties();
            String npcType = (String) props.get(PROP_ACTOR_TYPE);
            if (npcType == null) {
                throw new IllegalStateException("Object in the entity layer does not have the " + PROP_ACTOR_TYPE
                        + " property. This is required!");
            }

            Class<?> actorClass = PROP_ACTOR_TYPE_ACTORS.get(npcType);
            if (actorClass == null) {
                throw new IllegalStateException(npcType + " does not exist in the actor/class map.");
            }

            Actor actor = null;

            for (ActorFactory<?> actorFactory : actorFactoryList) {
                if (actorFactory.actorClass().equals(actorClass)) {
                    actor = actorFactory.make(props);
                }
            }

            if (actor == null) {
                throw new IllegalStateException("No factory for " + actorClass);
            }

            actor.setX(object.getRectangle().x);
            actor.setY(object.getRectangle().y);
            stage.addActor(actor);
        }
    }
}
