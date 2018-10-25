package io.michelfaria.chrono.consts;

import java.util.HashMap;
import java.util.Map;

import io.michelfaria.chrono.actor.Nu;

/**
 * This class contains constants used in the Tiled maps.
 */
public final class MapConstants {
	public static String LAYER_FG_1 = "fg1";
	public static String LAYER_FG_2 = "fg2";
	public static String LAYER_COLLISION = "collision";
	public static String LAYER_BATTLEPT = "battlept";

	public static String PROP_NPC_TYPE = "npc-type";
	
	public static Map<String, Class<?>> PROP_NPC_TYPE_NPCS = new HashMap<>();
	{
		PROP_NPC_TYPE_NPCS.put("nu", Nu.class);
	}
	
	private MapConstants() {
	}
}
