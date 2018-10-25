/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/20/18 4:40 PM.
 * Copyright (c) 2018. All rights reserved.
 */

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
