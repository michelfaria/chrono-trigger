package io.michelfaria.chrono;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import io.michelfaria.chrono.events.EventDispatcher;
import io.michelfaria.chrono.hud.MenuBoxes;
import io.michelfaria.chrono.screen.WalkScreen;
import io.michelfaria.chrono.values.Assets;

public class Game extends com.badlogic.gdx.Game {

    State state;
    AssetManager assetManager;
    TextureAtlas atlas;
    SpriteBatch batch;
    TmxMapLoader tmxMapLoader;
    MenuBoxes menuBoxes;
    EventDispatcher eventDispatcher;

    @Override
    public void create() {
        state = new State();
        if (state.debug) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        }

        batch = new SpriteBatch();
        tmxMapLoader = new TmxMapLoader();
        eventDispatcher = new EventDispatcher();

        // Load assets
        assetManager = new AssetManager();
        assetManager.load(Assets.CHRONO_ATLAS);
        assetManager.load(Assets.FONT);

        // Wait until done loading assets
        assetManager.finishLoading();

        // Done loading assets

        atlas = assetManager.get(Assets.CHRONO_ATLAS);
        menuBoxes = new MenuBoxes(atlas);

        setScreen(new WalkScreen(batch, menuBoxes, assetManager, state, tmxMapLoader, atlas, eventDispatcher));
    }

    public TmxMapLoader getTmxMapLoader() {
        return tmxMapLoader;
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        atlas.dispose();
        batch.dispose();
    }

}
