package io.michelfaria.chrono.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.actor.Crono;
import io.michelfaria.chrono.hud.WalkHud;
import io.michelfaria.chrono.values.Assets;

public class WalkScreen implements Screen {

	private Core core;
	
	// Scene 2D
	private InputMultiplexer multiplexer;
	private OrthographicCamera camera;
	private Viewport viewport;
	private Stage stage;

	// Tiled
	private TiledMap tiledMap;
	private OrthogonalTiledMapRenderer tiledMapRenderer;
	
	// Other
	private WalkHud hud;
	private float stateTime;
	
	private Crono crono; // (TESTING ONLY)

	public WalkScreen(Core core) {
		this.core = core;
		
		multiplexer = new InputMultiplexer();
		camera = new OrthographicCamera();
		viewport = new FitViewport(core.getVirtualWidth(), core.getVirtualHeight(), camera);
		stage = new Stage(viewport);

		tiledMap = core.getTmxMapLoader().load(Assets.EXAMPLE_TMX);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		
		hud = new WalkHud(core);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(multiplexer);
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(hud.stage);

		// Add the character (Testing)
		this.crono = new Crono(core);
		stage.addActor(crono);
		crono.setHandleInput(true);
	}

	@Override
	public void render(float dt) {
		update(dt);

		// Clean canvas
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render the foreground that goes behind the Sprite(s)
		renderTileLayer("fg-2");
		
		// Stage drawings
		// (Stages open the batch and close it again)
		core.getBatch().setProjectionMatrix(camera.combined);
		stage.draw();

		// Render the foreground that goes in front of the Sprite(s)
		renderTileLayer("fg-1");
		
		// HUD always drawn on top
		core.getBatch().setProjectionMatrix(hud.stage.getCamera().combined);
		hud.draw();
	}

	/**
	 * Render a tiled tile layer.
	 * 
	 * @param name Name of the layer to render
	 * @throws IllegalStateException If the layer doesn't exist
	 */
	private void renderTileLayer(String name) {
		TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(name);
		if (tileLayer == null) {
			throw new IllegalStateException("Layer not found: " + name);
		}
		tiledMapRenderer.getBatch().begin();
		tiledMapRenderer.renderTileLayer(tileLayer);
		tiledMapRenderer.getBatch().end();
	}

	private void update(float dt) {
		stateTime += Gdx.graphics.getDeltaTime();
		hud.update(dt);
		stage.act();
		updateCamera();	
		tiledMapRenderer.setView(camera);
	}

	private void updateCamera() {
		camera.position.x = crono.getX();
		camera.position.y = crono.getY();
		camera.update();
	}

	@Override
	public void resize(int width, int height) {
		hud.viewport.update(width, height);
		viewport.update(width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		hud.dispose();
		stage.dispose();
		tiledMapRenderer.dispose();
	}
}
