package io.michelfaria.chrono.util;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.Core;


public final class GLUtil {
    public static Vector2 getRealSize(Viewport vp, TextureRegion txReg) {
        return new Vector2(
                txReg.getRegionWidth() * ((float) vp.getScreenWidth() / Core.V_WIDTH),
                txReg.getRegionHeight() * ((float) vp.getScreenHeight() / Core.V_HEIGHT));
    }

    public static Vector2 getRealSize(Viewport vp, Group group) {
        return new Vector2(
                group.getWidth() * ((float) vp.getScreenWidth() / Core.V_WIDTH),
                group.getHeight() * ((float) vp.getScreenHeight() / Core.V_HEIGHT));
    }
}
