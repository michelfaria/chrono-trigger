package io.michelfaria.chrono.hud;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.util.TxUtil;

import static io.michelfaria.chrono.values.TxRegs.UiBoxTxRegs.UI_DIALOGBOX_0;

public final class MenuBoxes {

	// Dialog box cache (index is type)
	public static UiElement[] dialogBoxes = new UiElement[10];
	// Type of UI for the game
	private static int type = 0;

	static {
		dialogBoxes[0] = new UiElement(UI_DIALOGBOX_0);
	}

	public static void setUiType(int type) {
		if (type < 0) {
			throw new IllegalArgumentException("type_ must be >= 0");
		}
		MenuBoxes.type = type;
	}

	public static TextureRegion getDialogBox() {
		if (dialogBoxes[type] == null) {
			throw new IllegalStateException("No dialog box for type " + type);
		}
		UiElement ue = dialogBoxes[type];
		if (ue.txReg == null) {
			ue.txReg = TxUtil.findRegion(Core.atlas, ue.regionName);
		}
		return ue.txReg;
	}

	private static class UiElement {
		public String regionName;
		public TextureRegion txReg;

		public UiElement(String regionName) {
			this.regionName = regionName;
		}
	}
}

/*
 * 
 * This was the function used to make dynamic menu boxes. It was
 * over-engineered, so ...
 * 
 * public static Table makeBox(float width, float height) { // Table to hold the
 * dialog box pieces Table table = new Table(); if (Core.debug) {
 * table.debugAll(); } table.bottom(); // Align table to bottom of the screen
 * table.setFillParent(true);
 * 
 * // Add top-left TextureRegion topLeft = TxUtil.findRegion(Core.atlas,
 * UI_BOX_0_TOP_LEFT); table.add(new Image(topLeft));
 * 
 * // Add top TextureRegion top = TxUtil.findRegion(Core.atlas, UI_BOX_0_TOP);
 * table.add(new Image(new TiledDrawable(top))); Cell topCell =
 * table.getCells().peek(); topCell.fill();
 * 
 * // Add top-right TextureRegion topRight = TxUtil.findRegion(Core.atlas,
 * UI_BOX_0_TOP_RIGHT); table.add(new Image(topRight));
 * 
 * 
 * table.row();
 * 
 * 
 * // Add left TextureRegion left = TxUtil.findRegion(Core.atlas,
 * UI_BOX_0_LEFT); table.add(new Image(left)); Cell leftCell =
 * table.getCells().peek(); leftCell.fill();
 * 
 * // Add filling TextureRegion filling = TxUtil.findRegion(Core.atlas,
 * UI_BOX_0_FILLING); table.add(new Image(new TiledDrawable(filling))); Cell
 * fillingCell = table.getCells().peek(); // Set width of the box
 * fillingCell.width(width); // Set the height of the box
 * fillingCell.height(height); // Make the center texture fill
 * fillingCell.fill();
 * 
 * // Add right TextureRegion right = TxUtil.findRegion(Core.atlas,
 * UI_BOX_0_RIGHT); table.add(new Image(new TiledDrawable(right))); Cell
 * rightCell = table.getCells().peek(); rightCell.fill();
 * 
 * 
 * table.row();
 * 
 * 
 * // Add bottom-left TextureRegion bottomLeft = TxUtil.findRegion(Core.atlas,
 * UI_BOX_0_BOTTOM_LEFT); table.add(new Image(bottomLeft));
 * 
 * // Add bottom TextureRegion bottom = TxUtil.findRegion(Core.atlas,
 * UI_BOX_0_BOTTOM); table.add(new Image(bottom)); Cell bottomCell =
 * table.getCells().peek(); bottomCell.fill();
 * 
 * // Add bottom-right TextureRegion bottomRight = TxUtil.findRegion(Core.atlas,
 * UI_BOX_0_BOTTOM_RIGHT); table.add(new Image(bottomRight));
 * 
 * return table; }
 */
