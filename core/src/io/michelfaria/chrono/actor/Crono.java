package io.michelfaria.chrono.actor;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import static io.michelfaria.chrono.animation.AnimationType.*;
import static io.michelfaria.chrono.values.TxRegs.*;

import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;

import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.animation.AnimationType;
import io.michelfaria.chrono.util.TextureTools;

public class Crono extends PartyCharacter {

	public Crono(Core core, TiledMap tiledMap) {
		super(core, tiledMap);
		final TextureAtlas atlas = core.getAtlas();
		final Map<AnimationType, Animation<?>> animations = aniMan.anims;
		final TextureTools tools = core.getTxTools();
		/*
		 * Idle animations
		 */
		animations.put(IDLE_NORTH, tools.makeAnimation(CRONO_IDLE_NORTH));
		animations.put(IDLE_SOUTH, tools.makeAnimation(CRONO_IDLE_SOUTH));
		animations.put(IDLE_WEST, tools.makeAnimation(CRONO_IDLE_WEST));
		animations.put(IDLE_EAST, tools.makeAnimation(CRONO_IDLE_EAST));
		/*
		 * Walking animations
		 */
		animations.put(WALK_NORTH, tools.makeAnimation(CRONO_WALK_NORTH));
		animations.put(WALK_SOUTH, tools.makeAnimation(CRONO_WALK_SOUTH));
		animations.put(WALK_WEST, tools.makeAnimation(CRONO_WALK_WEST));
		animations.put(WALK_EAST, tools.makeAnimation(CRONO_WALK_EAST));
		/*
		 * Running animations
		 */
		animations.put(RUN_NORTH, tools.makeAnimation(CRONO_RUN_NORTH));
		animations.put(RUN_SOUTH, tools.makeAnimation(CRONO_RUN_SOUTH));
		animations.put(RUN_WEST, tools.makeAnimation(CRONO_RUN_WEST));
		animations.put(RUN_EAST, tools.makeAnimation(CRONO_RUN_EAST));

		// Temporary
		setX((float) Math.random() * 200);
		setY((float) Math.random() * 200);
	}
}