package io.michelfaria.chrono.consts;

import java.util.HashMap;
import java.util.Map;

import io.michelfaria.chrono.actor.Nu;

/**
 * This class contains constants used in the Tiled maps.
 */
public final class MapConstants {
    /*===============================*
                LAYER NAMES
     *===============================*/

    // Foreground layer (drawn above entities)
    public static String LAYER_FG_1 = "fg1";

    // Foreground layer (drawn below entities)
    public static String LAYER_FG_2 = "fg2";

    // Map collision rectangles
    public static String LAYER_COLLISION = "collision";

    // Layer that contains positions of battle
    public static String LAYER_BATTLEPT = "battlept";

    // Entity points (where to spawn entities)
    public static String LAYER_ENTITY = "entity";

    /*===============================*
           ENTITY LAYER PROPERTIES
     *===============================*/
    // Property that contains the name of the NPC to be spawned
    public static String PROP_NPC_TYPE = "npc_type";

    /*===============================*
          ENTITY POINT NPC CLASSES
     *===============================*/
    // Map of NPC identifiers and Java class objects
    public static Map<String, Class<?>> PROP_NPC_TYPE_NPCS = new HashMap<>();
    static {
        PROP_NPC_TYPE_NPCS.put("nu", Nu.class);
    }

    private MapConstants() {
    }
}
