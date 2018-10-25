package io.michelfaria.chrono.logic;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import io.michelfaria.chrono.actor.Nu;
import io.michelfaria.chrono.events.EventDispatcher;
import io.michelfaria.chrono.interfaces.ActorFactory;
import io.michelfaria.chrono.util.PrintProperties;
import sun.plugin.javascript.navig4.Layer;

import java.util.ArrayList;
import java.util.List;

import static io.michelfaria.chrono.consts.MapConstants.*;

public class TiledMapStagePopulator {

    private List<ActorFactory<?>> actorFactoryList = new ArrayList<>();

    public TiledMapStagePopulator(CollisionContext collisionContext, EventDispatcher eventDispatcher, TextureAtlas textureAtlas) {
        actorFactoryList.add(new Nu.NuFactory(collisionContext, eventDispatcher, textureAtlas));
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

            MapProperties properties = object.getProperties();
            String npcType = (String) properties.get(PROP_NPC_TYPE);

            Class<?> actorClass = PROP_NPC_TYPE_NPCS.get(npcType);
            if (actorClass == null) {
                throw new IllegalStateException("Unknown \"" + PROP_NPC_TYPE + "\": " + npcType);
            }

            Actor actor = null;

            for (ActorFactory<?> actorFactory : actorFactoryList) {
                if (actorFactory.actorClass().equals(actorClass)) {
                    actor = actorFactory.make();
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
