/*
 * Developed by Michel Faria on 10/29/18 8:15 PM.
 * Last modified 10/27/18 9:29 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import io.michelfaria.chrono.actors.BattlePoint;
import io.michelfaria.chrono.actors.EntryPoint;
import io.michelfaria.chrono.actors.Nu;
import io.michelfaria.chrono.interfaces.ActorFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.michelfaria.chrono.MapConstants.*;

/**
 * This class is responsible for creating the Actor(s) for the Walk Screen.
 * <p>
 * It takes a TiledMap and a Stage, creates Actors from the data in the TiledMap, and then puts all of them in the Stage.
 * <p>
 * Actor creation is simple. This class finds all of the Points in the LAYER_ENTITY layer and iterates through each one
 * of them. To find out what kind of Actor the Point in the TiledMap represents, it uses the ACTORTYPE_ACTORCLASS_MAP.
 * <p>
 * As for Actor creation, this class uses a list of Actor-creating Factories to create the Actors. Each Factory should
 * be responsible for creating a single type of Actor. It uses the ActorFactory.getClass method to find out what kind of
 * Actor that Factory produces.
 */
public final class TiledMapStagePopulator {

    private Game.Context ctx;

    private List<ActorFactory<? extends Actor>> actorFactoryList = new ArrayList<>();

    public TiledMapStagePopulator(Game.Context ctx) {
        this.ctx = ctx;

        actorFactoryList.add(new Nu.NuFactory(ctx));
        actorFactoryList.add(new BattlePoint.Factory(ctx));
        actorFactoryList.add(new EntryPoint.Factory());
    }

    public void populateStage(TiledMap map, Stage stage) {
        MapLayers layers = map.getLayers();

        for (MapLayer layer : layers) {
            if (layer.getName().equals(LAYER_ENTITY)) {
                populateEntityLayer(layer, stage);
            }
        }
    }

    /**
     * Locates all of the Point objects in the specified MapLayer and creates Actor(s) from them.
     * The Actor(s) will be added to the provided Stage.
     *
     * @param layer The map layer to create Actors from
     * @param stage The stage to add Actor(s) to
     */
    private void populateEntityLayer(MapLayer layer, Stage stage) {
        Array<RectangleMapObject> objects = layer.getObjects()
                .getByType(RectangleMapObject.class); // In libGDX, Points are seen as zero-length rectangles

        for (RectangleMapObject object : objects) {
            MapProperties props = object.getProperties();

            // actorType => What kind of Actor are we going to make?
            String actorType = getActorType(props);

            // actorClass => What is the Java Class that represents the actorType provided above?
            Class<? extends Actor> actorClass = getActorClass(actorType);

            // Create the actor
            Actor actor;
            try {
                actor = createActor(actorClass, props);
            } catch (Exception ex) {
                throw new RuntimeException("An error occurred while creating an entity at x:"
                        + object.getRectangle().x + ", y:" + object.getRectangle().y + " in layer "
                        + layer.getName(), ex);
            }

            // Position the actor
            actor.setX(object.getRectangle().x);
            actor.setY(object.getRectangle().y);

            // Add it to the stage
            stage.addActor(actor);
        }
    }

    /**
     * Creates an Actor from an Actor Class and Map Properties.
     *
     * @param actorClass The Actor class to create from
     * @param props      The map properties pertaining to the Actor
     * @throws IllegalStateException If there is no Factory for the specified Actor Class in this instance's actorFactoryList
     */
    private Actor createActor(Class<? extends Actor> actorClass, MapProperties props) {
        Actor actor = null;
        for (ActorFactory<?> actorFactory : actorFactoryList) {
            if (actorFactory.actorClass().equals(actorClass)) {
                actor = actorFactory.make(props);
            }
        }
        if (actor == null) {
            throw new IllegalStateException("No factory for " + actorClass);
        }
        return actor;
    }

    /**
     * Returns the Class that represents the "actorType" String.
     * <p>
     * If there is no Class that represents the "actorType", it will throw an IllegalStateException.
     */
    @NotNull
    private Class<? extends Actor> getActorClass(String actorType) {
        Class<? extends Actor> actorClass = ACTORTYPE_ACTORCLASS_MAP.get(actorType);
        if (actorClass == null) {
            throw new IllegalStateException(actorType + " does not exist in the actor/class map.");
        }
        return actorClass;
    }

    /**
     * Returns the value of the "actor type" field of a Tiled Map Object's properties.
     */
    @NotNull
    private String getActorType(MapProperties props) {
        String actorType = (String) props.get(PROP_ACTOR_TYPE);
        if (actorType == null) {
            throw new IllegalStateException("Object in the entity layer does not have the " + PROP_ACTOR_TYPE
                    + " property. This is required!");
        }
        return actorType;
    }
}
