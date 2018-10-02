package io.michelfaria.chrono.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.michelfaria.chrono.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Game.V_WIDTH * 3;
		config.height = Game.V_HEIGHT * 3;
		new LwjglApplication(new Game(), config);
	}
}
