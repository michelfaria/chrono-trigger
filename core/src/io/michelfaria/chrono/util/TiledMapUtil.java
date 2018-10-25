/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:44 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.util;

import com.badlogic.gdx.maps.tiled.TiledMap;

public final class TiledMapUtil {
    private TiledMapUtil() {
    }

    public static int mapWidth(TiledMap map) {
        return map.getProperties().get("width", Integer.class);
    }

    public static int mapHeight(TiledMap map) {
        return map.getProperties().get("height", Integer.class);
    }

    public static int tilePixelWidth(TiledMap map) {
        return map.getProperties().get("tilewidth", Integer.class);
    }

    public static int tilePixelHeight(TiledMap map) {
        return map.getProperties().get("tileheight", Integer.class);
    }

    public static int mapPixelWidth(TiledMap map) {
        return mapWidth(map) * tilePixelWidth(map);
    }
    
    public static int mapPixelHeight(TiledMap map) {
        return mapHeight(map) * tilePixelHeight(map);
    }
}
