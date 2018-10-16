package io.michelfaria.chrono.actor;

import static io.michelfaria.chrono.animation.AnimationType.IDLE_EAST;
import static io.michelfaria.chrono.animation.AnimationType.IDLE_NORTH;
import static io.michelfaria.chrono.animation.AnimationType.IDLE_SOUTH;
import static io.michelfaria.chrono.animation.AnimationType.IDLE_WEST;
import static io.michelfaria.chrono.animation.AnimationType.RUN_EAST;
import static io.michelfaria.chrono.animation.AnimationType.RUN_NORTH;
import static io.michelfaria.chrono.animation.AnimationType.RUN_SOUTH;
import static io.michelfaria.chrono.animation.AnimationType.RUN_WEST;
import static io.michelfaria.chrono.animation.AnimationType.WALK_EAST;
import static io.michelfaria.chrono.animation.AnimationType.WALK_NORTH;
import static io.michelfaria.chrono.animation.AnimationType.WALK_SOUTH;
import static io.michelfaria.chrono.animation.AnimationType.WALK_WEST;
import static io.michelfaria.chrono.values.TxRegs.CRONO_IDLE_EAST;
import static io.michelfaria.chrono.values.TxRegs.CRONO_IDLE_NORTH;
import static io.michelfaria.chrono.values.TxRegs.CRONO_IDLE_SOUTH;
import static io.michelfaria.chrono.values.TxRegs.CRONO_IDLE_WEST;
import static io.michelfaria.chrono.values.TxRegs.CRONO_RUN_EAST;
import static io.michelfaria.chrono.values.TxRegs.CRONO_RUN_NORTH;
import static io.michelfaria.chrono.values.TxRegs.CRONO_RUN_SOUTH;
import static io.michelfaria.chrono.values.TxRegs.CRONO_RUN_WEST;
import static io.michelfaria.chrono.values.TxRegs.CRONO_WALK_EAST;
import static io.michelfaria.chrono.values.TxRegs.CRONO_WALK_NORTH;
import static io.michelfaria.chrono.values.TxRegs.CRONO_WALK_SOUTH;
import static io.michelfaria.chrono.values.TxRegs.CRONO_WALK_WEST;

import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.maps.tiled.TiledMap;

import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.animation.AnimationType;
import io.michelfaria.chrono.util.TextureTools;

public class Crono extends PartyCharacter {

	public Crono(Core core, TiledMap tiledMap) {
		super(core, tiledMap);
		final Map<AnimationType, Animation<?>> anims = aniMan.anims;
		final TextureTools tools = core.getTxTools();
		/*
		 * Idle animations
		 */
		anims.put(IDLE_NORTH, tools.makeAnimation(CRONO_IDLE_NORTH));
		anims.put(IDLE_SOUTH, tools.makeAnimation(CRONO_IDLE_SOUTH));
		anims.put(IDLE_WEST, tools.makeAnimation(CRONO_IDLE_WEST));
		anims.put(IDLE_EAST, tools.makeAnimation(CRONO_IDLE_EAST));
		/*
		 * Walking animations
		 */
		anims.put(WALK_NORTH, tools.makeAnimation(CRONO_WALK_NORTH));
		anims.put(WALK_SOUTH, tools.makeAnimation(CRONO_WALK_SOUTH));
		anims.put(WALK_WEST, tools.makeAnimation(CRONO_WALK_WEST));
		anims.put(WALK_EAST, tools.makeAnimation(CRONO_WALK_EAST));
		/*
		 * Running animations
		 */
		anims.put(RUN_NORTH, tools.makeAnimation(CRONO_RUN_NORTH));
		anims.put(RUN_SOUTH, tools.makeAnimation(CRONO_RUN_SOUTH));
		anims.put(RUN_WEST, tools.makeAnimation(CRONO_RUN_WEST));
		anims.put(RUN_EAST, tools.makeAnimation(CRONO_RUN_EAST));

		// Temporary
		setX((float) Math.random() * 200);
		setY((float) Math.random() * 200);
		this.actionRunnables.add(() -> System.out.println("x:" + getX() + ",y:" + getY()));
	}
}