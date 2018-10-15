package io.michelfaria.chrono.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.Core;

public final class GLUtil {
	public static Rectangle getRealSize(Viewport vp, Rectangle rectangle) {
		float xMultiplier = (float) vp.getScreenWidth() / Core.V_WIDTH;
		float yMultiplier = (float) vp.getScreenHeight() / Core.V_HEIGHT;

		return new Rectangle(rectangle.getX() * xMultiplier, rectangle.getY() * yMultiplier,
				rectangle.getWidth() * xMultiplier, rectangle.getHeight() * yMultiplier);
	}

}
