/*
 * Developed by Michel Faria on 10/29/18 8:21 PM.
 * Last modified 10/29/18 8:21 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.graphics;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import io.michelfaria.chrono.Game;

public final class TileLayerRender {

    private TileLayerRender() {
    }

    public static void renderTileLayer(String name) {
        final TiledMapTileLayer tileLayer = (TiledMapTileLayer) Game.map.getLayers().get(name);
        if (tileLayer == null) {
            throw new IllegalStateException("Layer not found: " + name);
        }
        Game.mapRenderer.getBatch().begin();
        Game.mapRenderer.renderTileLayer(tileLayer);
        Game.mapRenderer.getBatch().end();
    }
}
