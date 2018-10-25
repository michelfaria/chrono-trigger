/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:44 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public final class ViewportUtil {

    private ViewportUtil() {
    }

    /**
     * Calculate the scale of the viewport in relation to the game's size.
     *
     * @param vp            The viewport
     * @param virtualWidth  Virtual mapWidth of the game
     * @param virtualHeight Virtual height of the game
     * @return The X and Y scales of the viewport
     */
    public static Vector2 viewportScale(Viewport vp, int virtualWidth, int virtualHeight) {
        return new Vector2((float) vp.getScreenWidth() / virtualWidth, (float) vp.getScreenHeight() / virtualHeight);
    }
}
