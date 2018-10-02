package io.michelfaria.chrono.hud;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.util.TxUtil;

import static io.michelfaria.chrono.values.TxRegs.DialogBoxTxRegs.*;

public final class DialogBoxes {

    public static final float STANDARD_HEIGHT = 80f;

    /*
     * Dialog box cache
     * */
    private static Table stdDialogBox = null;

    /**
     * Retrieves a dialog box.
     *
     * @apiNote Standard dialog boxes are cached!
     * @param height - use STANDARD_HEIGHT for default
     */
    public static Table get(float height) {
        if (height == STANDARD_HEIGHT && stdDialogBox != null) {
            return stdDialogBox;
        }
        // Table to hold the dialog box pieces
        Table table = new Table();
        if (Core.debug) {
            table.debugAll();
        }
        table.bottom(); // Align table to bottom of the screen
        table.setFillParent(true);

        // Add top-left
        TextureRegion topLeft = TxUtil.findRegion(Core.atlas, DIALOG_BOX_1_TOP_LEFT);
        table.add(new Image(topLeft));

        // Add top
        TextureRegion top = TxUtil.findRegion(Core.atlas, DIALOG_BOX_1_TOP);
        table.add(new Image(new TiledDrawable(top)));
        Cell topCell = table.getCells().peek();
        topCell.fill();

        // Add top-right
        TextureRegion topRight = TxUtil.findRegion(Core.atlas, DIALOG_BOX_1_TOP_RIGHT);
        table.add(new Image(topRight));


        table.row();


        // Add left
        TextureRegion left = TxUtil.findRegion(Core.atlas, DIALOG_BOX_1_LEFT);
        table.add(new Image(left));
        Cell leftCell = table.getCells().peek();
        leftCell.fill();

        // Add filling
        TextureRegion filling = TxUtil.findRegion(Core.atlas, DIALOG_BOX_1_FILLING);
        table.add(new Image(new TiledDrawable(filling)));
        Cell fillingCell = table.getCells().peek();
        fillingCell.expand(1, 0); // Expand horizontally
        fillingCell.height(height); // Set the height of the box
        fillingCell.fill();

        // Add right
        TextureRegion right = TxUtil.findRegion(Core.atlas, DIALOG_BOX_1_RIGHT);
        table.add(new Image(new TiledDrawable(right)));
        Cell rightCell = table.getCells().peek();
        rightCell.fill();


        table.row();


        // Add bottom-left
        TextureRegion bottomLeft = TxUtil.findRegion(Core.atlas, DIALOG_BOX_1_BOTTOM_LEFT);
        table.add(new Image(bottomLeft));

        // Add bottom
        TextureRegion bottom = TxUtil.findRegion(Core.atlas, DIALOG_BOX_1_BOTTOM);
        table.add(new Image(bottom));
        Cell bottomCell = table.getCells().peek();
        bottomCell.fill();

        // Add bottom-right
        TextureRegion bottomRight = TxUtil.findRegion(Core.atlas, DIALOG_BOX_1_BOTTOM_RIGHT);
        table.add(new Image(bottomRight));


        // Store in the cache
        stdDialogBox = table;

        return table;
    }
}
