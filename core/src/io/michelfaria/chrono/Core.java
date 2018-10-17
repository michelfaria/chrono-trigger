package io.michelfaria.chrono;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import io.michelfaria.chrono.hud.MenuBoxes;
import io.michelfaria.chrono.screen.WalkScreen;
import io.michelfaria.chrono.util.TextureTools;
import io.michelfaria.chrono.values.Assets;

public class Core extends Game {

    private State state;

    private AssetManager assetMan;
    private TextureAtlas atlas;
    private SpriteBatch batch;

    private TextureTools txTools;
    private MenuBoxes menuBoxes;
    private TmxMapLoader tmxMapLoader;

    @Override
    public void create() {
        state = new State();
        if (state.isDebug()) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        }

        // Load assets
        assetMan = new AssetManager();
        assetMan.load(Assets.CHRONO_ATLAS, TextureAtlas.class);
        assetMan.load(Assets.FONT, BitmapFont.class);

        // Wait until done loading assets
        assetMan.finishLoading();

        // Done loading assets

        atlas = assetMan.get(Assets.CHRONO_ATLAS, TextureAtlas.class);
        batch = new SpriteBatch();

        txTools = new TextureTools(atlas);
        menuBoxes = new MenuBoxes(this);
        tmxMapLoader = new TmxMapLoader();

        setScreen(new WalkScreen(this));
    }

    public TmxMapLoader getTmxMapLoader() {
        return tmxMapLoader;
    }

    @Override
    public void dispose() {
        assetMan.dispose();
        atlas.dispose();
        batch.dispose();
    }

    public int getVirtualWidth() {
        return 256;
    }

    public int getVirtualHeight() {
        return 224;
    }

    public AssetManager getAssetMan() {
        return assetMan;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public MenuBoxes getMenuBoxes() {
        return menuBoxes;
    }

    public State getState() {
        return state;
    }

    public TextureTools getTxTools() {
        return txTools;
    }
}
