package io.michelfaria.chrono.actor;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
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

		// Idle
		idleNorth = new Animation<>(0, atlas.findRegion(CRONO_IDLE_NORTH));
		idleSouth = new Animation<>(0, atlas.findRegion(CRONO_IDLE_SOUTH));
		idleWest = new Animation<>(0, atlas.findRegion(CRONO_IDLE_WEST));
		idleEast = new Animation<>(0, atlas.findRegion(CRONO_IDLE_EAST));

		// Walk
		walkNorth = new Animation<>(0.125f,
				TxUtil.splitTextureRegion(atlas, CRONO_WALK_NORTH, CRONO_WALK_NORTH_COLS, CRONO_WALK_NORTH_ROWS), LOOP);
		walkSouth = new Animation<>(0.125f,
				TxUtil.splitTextureRegion(atlas, CRONO_WALK_SOUTH, CRONO_WALK_SOUTH_COLS, CRONO_WALK_SOUTH_ROWS), LOOP);
		walkEast = new Animation<>(0.125f,
				TxUtil.splitTextureRegion(atlas, CRONO_WALK_EAST, CRONO_WALK_EAST_COLS, CRONO_WALK_EAST_ROWS), LOOP);
		walkWest = new Animation<>(0.125f,
				TxUtil.splitTextureRegion(atlas, CRONO_WALK_WEST, CRONO_WALK_WEST_COLS, CRONO_WALK_WEST_ROWS), LOOP);

		// Run
		runNorth = new Animation<>(0.1f,
				TxUtil.splitTextureRegion(atlas, CRONO_RUN_NORTH, CRONO_RUN_NORTH_COLS, CRONO_RUN_NORTH_ROWS), LOOP);
		runSouth = new Animation<>(0.1f,
				TxUtil.splitTextureRegion(atlas, CRONO_RUN_SOUTH, CRONO_RUN_SOUTH_COLS, CRONO_RUN_SOUTH_ROWS), LOOP);
		runWest = new Animation<>(0.1f,
				TxUtil.splitTextureRegion(atlas, CRONO_RUN_WEST, CRONO_RUN_WEST_COLS, CRONO_RUN_WEST_ROWS), LOOP);
		runEast = new Animation<>(0.1f,
				TxUtil.splitTextureRegion(atlas, CRONO_RUN_EAST, CRONO_RUN_EAST_COLS, CRONO_RUN_EAST_ROWS), LOOP);

		// Temporary
		setX((float) Math.random() * 200);
		setY((float) Math.random() * 200);
	}
}