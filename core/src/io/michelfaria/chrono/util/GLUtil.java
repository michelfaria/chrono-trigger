package io.michelfaria.chrono.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public final class GLUtil {
    /**
     * Calculates the real width and height of the Rectangle on the computer screen.
     *
     * @param vp            Game viewport, provides size of the window
     * @param rectangle     Rectangle to calculate
     * @param virtualWidth  Virtual Width of the game
     * @param virtualHeight Virtual Height of the game
     * @return Rectangle scaled to real size
     */
    public static Rectangle getRealSize(Viewport vp, Rectangle rectangle, int virtualWidth, int virtualHeight) {
        Vector2 viewportScale = ViewportUtil.viewportScale(vp, virtualWidth, virtualHeight);
        return new Rectangle(rectangle.getX() * viewportScale.x, rectangle.getY() * viewportScale.y,
                rectangle.getWidth() * viewportScale.x, rectangle.getHeight() * viewportScale.y);
    }

}
