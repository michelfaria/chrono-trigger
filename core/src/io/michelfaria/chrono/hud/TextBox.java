package io.michelfaria.chrono.hud;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import io.michelfaria.chrono.Game;

public class TextBox {

    private NinePatch patch;

    public TextBox(Game game) {
        TextureAtlas atlas = game.atlas;

        TextureAtlas.AtlasRegion txReg = atlas.findRegion("dialog-box-1-9");
        assert txReg != null;

        //patch = new NinePatch(txReg)
    }
}
