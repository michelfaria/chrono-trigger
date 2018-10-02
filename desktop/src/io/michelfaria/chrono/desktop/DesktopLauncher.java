package io.michelfaria.chrono.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.michelfaria.chrono.Core;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Core.V_WIDTH * 3;
		config.height = Core.V_HEIGHT * 3;
		new LwjglApplication(new Core(), config);
	}
}
