package io.michelfaria.chrono.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.michelfaria.chrono.Core;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        Core core = new Core();
        config.width = core.getVirtualWidth() * 3;
        config.height = core.getVirtualHeight() * 3;
        new LwjglApplication(core, config);
    }
}
