package io.michelfaria.chrono;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ChronoTrigger extends Game {

    public static final int V_WIDTH = 256;
    public static final int V_HEIGHT = 224;

    public SpriteBatch batch;
    public AssetManager asmgr;

    @Override
    public void create() {
        asmgr = new AssetManager();
        asmgr.load(Assets.ASSET_TXATLAS, TextureAtlas.class);
        batch = new SpriteBatch();

       asmgr.finishLoading();

        setScreen(new PlayScreen(this));
    }

    @Override
    public void dispose() {
        asmgr.dispose();
        batch.dispose();
    }
}
