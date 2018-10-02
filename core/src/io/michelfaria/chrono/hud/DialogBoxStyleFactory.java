package io.michelfaria.chrono.hud;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.util.TxUtil;

import static io.michelfaria.chrono.values.TxRegions.HudTxRegions.*;

public final class DialogBoxStyleFactory {
    public static TextButtonStyle getStyle(Game game) {
        TextureRegion dialogBoxTxReg = TxUtil.findRegion(game.atlas, DIALOG_BOX_1);
        NinePatch np = new NinePatch(dialogBoxTxReg, DIALOG_BOX_1_PATCH_1, DIALOG_BOX_1_PATCH_2, DIALOG_BOX_1_PATCH_3, DIALOG_BOX_1_PATCH_4);
        NinePatchDrawable npDrawable = new NinePatchDrawable(np);
        return new TextButtonStyle(npDrawable, npDrawable, npDrawable, new BitmapFont());
    }
}
