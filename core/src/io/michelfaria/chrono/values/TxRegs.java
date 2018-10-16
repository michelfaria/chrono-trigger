package io.michelfaria.chrono.values;

public enum TxRegs {

	// @formatter:off
	CRONO_IDLE_NORTH("crono-idle-north", 1, 1),
	CRONO_IDLE_SOUTH("crono-idle-south", 1, 1),
	CRONO_IDLE_WEST("crono-idle-west", 1, 1),
	CRONO_IDLE_EAST("crono-idle-east", 1, 1),
	CRONO_WALK_NORTH("crono-walk-north", 6, 1, 0.125f),
	CRONO_WALK_SOUTH("crono-walk-south", 6, 1, 0.125f),
	CRONO_WALK_WEST("crono-walk-west", 6, 1, 0.125f),
	CRONO_WALK_EAST("crono-walk-east", 6, 1, 0.125f),
	CRONO_RUN_NORTH("crono-run-north", 6, 1, 0.1f),
	CRONO_RUN_SOUTH("crono-run-south", 6, 1, 0.1f),
	CRONO_RUN_WEST("crono-run-west", 6, 1, 0.1f),
	CRONO_RUN_EAST("crono-run-east", 6, 1, 0.1f),
	
	UI_DIALOGBOX_0("ui-dialogbox-0", 1, 1),
	
	NU_IDLE_SOUTH("nu-idle-south", 3, 1);
	// @formatter:on

	public final String regionName;
	public final int columns;
	public final int rows;
	public float speed = 0;

	private TxRegs(String regionName, int columns, int rows) {
		this.regionName = regionName;
		this.columns = columns;
		this.rows = rows;
	}

	private TxRegs(String regionName, int columns, int rows, float speed) {
		this(regionName, columns, rows);
		this.speed = speed;
	}
}
