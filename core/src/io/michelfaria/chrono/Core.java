package io.michelfaria.chrono;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.michelfaria.chrono.screen.PlayScreen;
import io.michelfaria.chrono.values.Assets;

public class Core extends Game {

    public static final int V_WIDTH = 256;
    public static final int V_HEIGHT = 224;

    public static boolean debug = true;

    public static SpriteBatch batch;
    public static AssetManager asmgr;
    public static TextureAtlas atlas;

    @Override
    public void create() {
        if (debug) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        }
        asmgr = new AssetManager();
        // Load assets
        asmgr.load(Assets.CHRONO_ATLAS, TextureAtlas.class);

        asmgr.finishLoading();

        atlas = asmgr.get(Assets.CHRONO_ATLAS, TextureAtlas.class);
        batch = new SpriteBatch();

        setScreen(new PlayScreen());
    }

    @Override
    public void dispose() {
        asmgr.dispose();
        atlas.dispose();
        batch.dispose();
    }
}
