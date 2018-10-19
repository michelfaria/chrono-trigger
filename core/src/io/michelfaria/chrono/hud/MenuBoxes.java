package io.michelfaria.chrono.hud;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.util.TextureTools;

import static io.michelfaria.chrono.values.TextureRegionDescriptor.UI_DIALOGBOX_0;

public class MenuBoxes {

    // Dialog box cache (indexes is type)
    public UiElement[] dialogBoxes = new UiElement[10];
    private Game game;
    // Current type of UI
    private int type = 0;

    public MenuBoxes(Game game) {
        this.game = game;
        dialogBoxes[0] = new UiElement(UI_DIALOGBOX_0.regionName);
    }

    public void setUiType(int type) {
        if (type < 0) {
            throw new IllegalArgumentException("type must be >= 0");
        }
        this.type = type;
    }

    public TextureRegion getDialogBox() {
        if (dialogBoxes[type] == null) {
            throw new IllegalStateException("No dialog box for type " + type);
        }
        UiElement ue = dialogBoxes[type];
        if (ue.txReg == null) {
            ue.txReg = TextureTools.findRegion(game.atlas, ue.regionName);
        }
        return ue.txReg;
    }

    private class UiElement {
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
 * dialog box pieces Table table = new Table(); if (Game.debug) {
 * table.debugAll(); } table.bottom(); // Align table to bottom of the screen
 * table.setFillParent(true);
 *
 * // Add top-left TextureRegion topLeft = TxUtil.findRegion(Game.atlas,
 * UI_BOX_0_TOP_LEFT); table.addEntity(new Image(topLeft));
 *
 * // Add top TextureRegion top = TxUtil.findRegion(Game.atlas, UI_BOX_0_TOP);
 * table.addEntity(new Image(new TiledDrawable(top))); Cell topCell =
 * table.getCells().peek(); topCell.fill();
 *
 * // Add top-right TextureRegion topRight = TxUtil.findRegion(Game.atlas,
 * UI_BOX_0_TOP_RIGHT); table.addEntity(new Image(topRight));
 *
 *
 * table.row();
 *
 *
 * // Add left TextureRegion left = TxUtil.findRegion(Game.atlas,
 * UI_BOX_0_LEFT); table.addEntity(new Image(left)); Cell leftCell =
 * table.getCells().peek(); leftCell.fill();
 *
 * // Add filling TextureRegion filling = TxUtil.findRegion(Game.atlas,
 * UI_BOX_0_FILLING); table.addEntity(new Image(new TiledDrawable(filling))); Cell
 * fillingCell = table.getCells().peek(); // Set width of the box
 * fillingCell.width(width); // Set the height of the box
 * fillingCell.height(height); // Make the center texture fill
 * fillingCell.fill();
 *
 * // Add right TextureRegion right = TxUtil.findRegion(Game.atlas,
 * UI_BOX_0_RIGHT); table.addEntity(new Image(new TiledDrawable(right))); Cell
 * rightCell = table.getCells().peek(); rightCell.fill();
 *
 *
 * table.row();
 *
 *
 * // Add bottom-left TextureRegion bottomLeft = TxUtil.findRegion(Game.atlas,
 * UI_BOX_0_BOTTOM_LEFT); table.addEntity(new Image(bottomLeft));
 *
 * // Add bottom TextureRegion bottom = TxUtil.findRegion(Game.atlas,
 * UI_BOX_0_BOTTOM); table.addEntity(new Image(bottom)); Cell bottomCell =
 * table.getCells().peek(); bottomCell.fill();
 *
 * // Add bottom-right TextureRegion bottomRight = TxUtil.findRegion(Game.atlas,
 * UI_BOX_0_BOTTOM_RIGHT); table.addEntity(new Image(bottomRight));
 *
 * return table; }
 */
