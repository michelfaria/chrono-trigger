package io.michelfaria.chrono.actor;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import static io.michelfaria.chrono.animation.AnimationType.*;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_IDLE_EAST;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_IDLE_NORTH;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_IDLE_SOUTH;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_IDLE_WEST;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_RUN_EAST;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_RUN_EAST_COLS;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_RUN_EAST_ROWS;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_RUN_NORTH;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_RUN_NORTH_COLS;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_RUN_NORTH_ROWS;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_RUN_SOUTH;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_RUN_SOUTH_COLS;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_RUN_SOUTH_ROWS;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_RUN_WEST;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_RUN_WEST_COLS;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_RUN_WEST_ROWS;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_WALK_EAST;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_WALK_EAST_COLS;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_WALK_EAST_ROWS;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_WALK_NORTH;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_WALK_NORTH_COLS;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_WALK_NORTH_ROWS;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_WALK_SOUTH;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_WALK_SOUTH_COLS;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_WALK_SOUTH_ROWS;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_WALK_WEST;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_WALK_WEST_COLS;
import static io.michelfaria.chrono.values.TxRegs.CronoTxRegs.CRONO_WALK_WEST_ROWS;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;

import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.util.TxUtil;

public class Crono extends PartyCharacter {

	public Crono(Core core, TiledMap tiledMap) {
		super(core, tiledMap);
		TextureAtlas atlas = core.getAtlas();

		animations.put(IDLE_NORTH, new Animation<>(0, atlas.findRegion(CRONO_IDLE_NORTH)));
		animations.put(IDLE_SOUTH, new Animation<>(0, atlas.findRegion(CRONO_IDLE_SOUTH)));
		animations.put(IDLE_WEST, new Animation<>(0, atlas.findRegion(CRONO_IDLE_WEST)));
		animations.put(IDLE_EAST, new Animation<>(0, atlas.findRegion(CRONO_IDLE_EAST)));
		
		animations.put(WALK_NORTH, new Animation<>(0.125f,
				TxUtil.splitTextureRegion(atlas, CRONO_WALK_NORTH, CRONO_WALK_NORTH_COLS, CRONO_WALK_NORTH_ROWS), LOOP));
		animations.put(WALK_SOUTH, new Animation<>(0.125f,
				TxUtil.splitTextureRegion(atlas, CRONO_WALK_SOUTH, CRONO_WALK_SOUTH_COLS, CRONO_WALK_SOUTH_ROWS), LOOP));
		animations.put(WALK_WEST, new Animation<>(0.125f,
				TxUtil.splitTextureRegion(atlas, CRONO_WALK_EAST, CRONO_WALK_EAST_COLS, CRONO_WALK_EAST_ROWS), LOOP));
		animations.put(WALK_EAST, new Animation<>(0.125f,
				TxUtil.splitTextureRegion(atlas, CRONO_WALK_WEST, CRONO_WALK_WEST_COLS, CRONO_WALK_WEST_ROWS), LOOP));
		
		animations.put(RUN_NORTH, new Animation<>(0.1f,
				TxUtil.splitTextureRegion(atlas, CRONO_RUN_NORTH, CRONO_RUN_NORTH_COLS, CRONO_RUN_NORTH_ROWS), LOOP));
		animations.put(RUN_SOUTH, new Animation<>(0.1f,
				TxUtil.splitTextureRegion(atlas, CRONO_RUN_SOUTH, CRONO_RUN_SOUTH_COLS, CRONO_RUN_SOUTH_ROWS), LOOP));
		animations.put(RUN_WEST, new Animation<>(0.1f,
				TxUtil.splitTextureRegion(atlas, CRONO_RUN_WEST, CRONO_RUN_WEST_COLS, CRONO_RUN_WEST_ROWS), LOOP));
		animations.put(RUN_EAST, new Animation<>(0.1f,
				TxUtil.splitTextureRegion(atlas, CRONO_RUN_EAST, CRONO_RUN_EAST_COLS, CRONO_RUN_EAST_ROWS), LOOP));

		// Temporary
		setX((float) Math.random() * 200);
		setY((float) Math.random() * 200);
	}
}