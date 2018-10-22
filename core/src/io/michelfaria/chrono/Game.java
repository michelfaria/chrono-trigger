package io.michelfaria.chrono;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import io.michelfaria.chrono.controller.ControllerEventEmitter;
import io.michelfaria.chrono.events.EventDispatcher;
import io.michelfaria.chrono.hud.MenuBoxes;
import io.michelfaria.chrono.screen.WalkScreen;
import io.michelfaria.chrono.values.Assets;

public class Game extends com.badlogic.gdx.Game {

    private AssetManager assetManager;
    private TextureAtlas atlas;
    private Batch batch;

    @Override
    public void create() {
        final State state = new State();
        if (state.debug) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        }

        this.batch = new SpriteBatch();
        TmxMapLoader tmxMapLoader = new TmxMapLoader();
        EventDispatcher eventDispatcher = new EventDispatcher();
        ControllerEventEmitter controllerEventEmitter = new ControllerEventEmitter(eventDispatcher);

        // Load assets
        this.assetManager = new AssetManager();
        this.assetManager.load(Assets.CHRONO_ATLAS);
        this.assetManager.load(Assets.FONT);

        // Wait until done loading assets
        this.assetManager.finishLoading();

        // Done loading assets

        this.atlas = this.assetManager.get(Assets.CHRONO_ATLAS);
        MenuBoxes menuBoxes = new MenuBoxes(this.atlas);

        setScreen(new WalkScreen(this.batch, menuBoxes, this.assetManager, state, tmxMapLoader,
                this.atlas, eventDispatcher, controllerEventEmitter));
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        atlas.dispose();
        batch.dispose();
    }

}
