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
import io.michelfaria.chrono.values.Assets;

public class Core extends Game {

	private final int V_WIDTH = 256;
	private final int V_HEIGHT = 224;

	private AssetManager asmgr;
	private TextureAtlas atlas;
	private SpriteBatch batch;
	
	private State state;
	private MenuBoxes menuBoxes;
	private TmxMapLoader tmxMapLoader;
	
	@Override
	public void create() {
		state = new State();
		if (state.isDebug()) {
			Gdx.app.setLogLevel(Application.LOG_DEBUG);
		}
		
		// Load assets
		asmgr = new AssetManager();
		getAsmgr().load(Assets.CHRONO_ATLAS, TextureAtlas.class);
		getAsmgr().load(Assets.FONT, BitmapFont.class);

		// Wait until done loading assets
		getAsmgr().finishLoading();

		// Done loading assets
		
		atlas = asmgr.get(Assets.CHRONO_ATLAS, TextureAtlas.class);
		batch = new SpriteBatch();
		menuBoxes = new MenuBoxes(this);
		tmxMapLoader = new TmxMapLoader();

		setScreen(new WalkScreen(this));
	}

	public TmxMapLoader getTmxMapLoader() {
		return tmxMapLoader;
	}

	@Override
	public void dispose() {
		asmgr.dispose();
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
		return asmgr;
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
}
