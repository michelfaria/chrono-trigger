package io.michelfaria.chrono.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.michelfaria.chrono.Game;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        Game game = new Game();
        config.width = 256 * 3;
        config.height = 224 * 3;
        new LwjglApplication(game, config);
    }
}
