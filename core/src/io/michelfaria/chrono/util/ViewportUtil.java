package io.michelfaria.chrono.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public final class ViewportUtil {
    /**
     * Calculate the scale of the viewport in relation to the game's size.
     *
     * @param vp            The viewport
     * @param virtualWidth  Virtual width of the game
     * @param virtualHeight Virtual height of the game
     * @return The X and Y scales of the viewport
     */
    public static Vector2 viewportScale(Viewport vp, int virtualWidth, int virtualHeight) {
        return new Vector2((float) vp.getScreenWidth() / virtualWidth, (float) vp.getScreenHeight() / virtualHeight);
    }
}
