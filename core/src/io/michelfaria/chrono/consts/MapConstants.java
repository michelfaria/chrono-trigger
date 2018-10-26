/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:38 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.consts;

import java.util.HashMap;
import java.util.Map;

import io.michelfaria.chrono.actor.BattlePoint;
import io.michelfaria.chrono.actor.EntryPoint;
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
    public static String PROP_ACTOR_TYPE = "actor_type";
    public static String PROP_ACTOR_ID = "actor_id";

    // -============ BattlePoints =============-
    // Id of the battle point group
    public static String PROP_BATTLEPT_BATTLEID = "battle_id";
    // Id of the point itself
    public static String PROP_BATTLEPT_SUBID = "battle_sub_id";

    // Type of the BattlePoint (has to be PROP_BATTLEPT_TYPE_PARTY or PROP_BATTLEPT_TYPE_ENEMY)
    public static String PROP_BATTLEPT_TYPE = "battlept_type";
    public static String PROP_BATTLEPT_TYPE_PARTY = "party";
    public static String PROP_BATTLEPT_TYPE_ENEMY = "enemy";

    // Entrypoint id
    public static String PROP_ENTRYPOINT_ID = "entry_point_id";

    /*===============================*
          ENTITY POINT NPC CLASSES
     *===============================*/
    // Map of NPC identifiers and Java class objects
    public static Map<String, Class<?>> PROP_ACTOR_TYPE_ACTORS = new HashMap<>();
    static {
        PROP_ACTOR_TYPE_ACTORS.put("entry_point", EntryPoint.class);
        PROP_ACTOR_TYPE_ACTORS.put("nu", Nu.class);
        PROP_ACTOR_TYPE_ACTORS.put("battlept", BattlePoint.class);
    }

    private MapConstants() {
    }
}
