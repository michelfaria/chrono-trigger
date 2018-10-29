/*
 * Developed by Michel Faria on 10/29/18 7:42 PM.
 * Last modified 10/29/18 7:42 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.textures.TRD;
import io.michelfaria.chrono.textures.UITRD;
import io.michelfaria.chrono.util.TextureTools;

public final class MenuBoxes {

    public static TRD[] boxes = {UITRD.UI_DIALOGBOX_0};
    public static int currentBox = 0;

    private MenuBoxes() {
    }

    public static void setBox(int box) {
        if (box > boxes.length) {
            throw new IllegalArgumentException("Index " + box + " not available in array of size " + boxes.length);
        }
        currentBox = box;
    }

    public static TextureRegion getDialogBoxGraphic() {
        assert currentBox < boxes.length;
        assert boxes[currentBox] != null;
        return TextureTools.findRegion(Game.getMainTextureAtlas(), boxes[currentBox].regionName);
    }
}
