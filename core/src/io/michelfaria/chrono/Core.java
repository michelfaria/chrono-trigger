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

    private final int V_WIDTH = 256;
    private final int V_HEIGHT = 224;

    private State state;

    private AssetManager assetMan;
    private TextureAtlas atlas;
    private SpriteBatch batch;

    private TextureTools txTools;
    private MenuBoxes menuBoxes;
    private TmxMapLoader tmxMapLoader;

    @Override
    public void create() {
        this.state = new State();
        if (state.isDebug()) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        }

        // Load assets
        this.assetMan = new AssetManager();
        getAsmgr().load(Assets.CHRONO_ATLAS, TextureAtlas.class);
        getAsmgr().load(Assets.FONT, BitmapFont.class);

        // Wait until done loading assets
        getAsmgr().finishLoading();

        // Done loading assets

        this.atlas = assetMan.get(Assets.CHRONO_ATLAS, TextureAtlas.class);
        this.batch = new SpriteBatch();

        this.txTools = new TextureTools(atlas);
        this.menuBoxes = new MenuBoxes(this);
        this.tmxMapLoader = new TmxMapLoader();

        setScreen(new WalkScreen(this));
    }

    public TmxMapLoader getTmxMapLoader() {
        return this.tmxMapLoader;
    }

    @Override
    public void dispose() {
        assetMan.dispose();
        atlas.dispose();
        batch.dispose();
    }

    public int getVirtualWidth() {
        return V_WIDTH;
    }

    public int getVirtualHeight() {
        return V_HEIGHT;
    }

    public AssetManager getAsmgr() {
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
