package io.michelfaria.chrono.hud;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.util.TextureTools;
import org.jetbrains.annotations.NotNull;

import static io.michelfaria.chrono.values.TextureRegionDescriptor.UI_DIALOGBOX_0;

public class MenuBoxes {

    private final TextureAtlas atlas;

    // Dialog box cache (indexes is type)
    public UiElement[] dialogBoxes = new UiElement[10];

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
        if (ue.txReg == null) {
            ue.txReg = TextureTools.findRegion(atlas, ue.regionName);
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