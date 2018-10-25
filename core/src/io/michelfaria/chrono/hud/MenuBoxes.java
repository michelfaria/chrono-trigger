/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:44 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.hud;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.michelfaria.chrono.util.TextureTools;

import static io.michelfaria.chrono.textures.UITRD.UI_DIALOGBOX_0;

import org.jetbrains.annotations.Nullable;

public class MenuBoxes {

    private final TextureAtlas atlas;

    // Dialog box cache (indexes is type)
    public final UiElement[] dialogBoxes = new UiElement[10];

    // Current type of UI
    private int type = 0;

    public MenuBoxes(TextureAtlas atlas) {
        this.atlas = atlas;
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
        if (ue.textureRegion == null) {
            ue.textureRegion = TextureTools.findRegion(atlas, ue.regionName);
        }
        return ue.textureRegion;
    }

    private class UiElement {
        public final String regionName;
        @Nullable
        public TextureRegion textureRegion;

        public UiElement(String regionName) {
            this.regionName = regionName;
        }
    }
}