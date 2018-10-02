package io.michelfaria.chrono.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.Core;

public final class ViewportUtil {
    /**
     * Calculate the scale of the viewport in relation to the game's size.
     */
    public static Vector2 viewportScale(Viewport vp) {
        return new Vector2((float) vp.getScreenWidth() / Core.V_WIDTH,
                (float) vp.getScreenHeight() / Core.V_HEIGHT);
    }
}
