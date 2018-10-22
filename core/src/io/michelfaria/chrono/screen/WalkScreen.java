package io.michelfaria.chrono.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.actor.Crono;
import io.michelfaria.chrono.actor.Nu;
import io.michelfaria.chrono.actor.PartyCharacter;
import io.michelfaria.chrono.controller.ControllerEventEmitter;
import io.michelfaria.chrono.events.EventDispatcher;
import io.michelfaria.chrono.hud.MenuBoxes;
import io.michelfaria.chrono.hud.WalkHud;
import io.michelfaria.chrono.logic.ActorYPositionComparator;
import io.michelfaria.chrono.logic.CollisionContext;
import io.michelfaria.chrono.util.TiledMapUtil;
import io.michelfaria.chrono.values.Assets;

import java.util.Random;

import static io.michelfaria.chrono.values.LayerNames.FOREGROUND_1;
import static io.michelfaria.chrono.values.LayerNames.FOREGROUND_2;

public class WalkScreen implements Screen {

    private final EventDispatcher eventDispatcher;
    private final TextureAtlas atlas;
    private final Batch batch;
    private final ControllerEventEmitter controllerEventEmitter;

    /*
    LibGDX
     */
    private final InputMultiplexer multiplexer = new InputMultiplexer();
    private final FPSLogger fpsLogger = new FPSLogger();

    /*
    Scene2D
     */
    private final OrthographicCamera camera = new OrthographicCamera();
    private final Viewport viewport = new FitViewport(256, 224, camera);
    private final Stage stage = new Stage(viewport);

    /*
    Tiled
     */
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer tiledMapRenderer;

    /*
    Other
     */
    private final WalkHud hud;
    private final CollisionContext collisionContext;
    private final ActorYPositionComparator yPositionCmp = new ActorYPositionComparator();
    private final PartyCharacter mainCharacter;

    public WalkScreen(Batch batch, MenuBoxes menuBoxes, AssetManager assetManager,
                      TmxMapLoader tmxMapLoader, TextureAtlas atlas, EventDispatcher eventDispatcher,
                      ControllerEventEmitter controllerEventEmitter) {
        this.eventDispatcher = eventDispatcher;
        this.atlas = atlas;
        this.batch = batch;
        this.controllerEventEmitter = controllerEventEmitter;

        this.map = tmxMapLoader.load(Assets.EXAMPLE_TMX);
        this.tiledMapRenderer = new OrthogonalTiledMapRenderer(this.map);

        this.hud = new WalkHud(batch, menuBoxes, assetManager, eventDispatcher);
        eventDispatcher.addEventListener(hud);

        this.collisionContext = new CollisionContext(this.map);
        this.mainCharacter = new Crono(collisionContext, atlas, eventDispatcher);
        eventDispatcher.addEventListener(this.mainCharacter);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(hud.stage);

        mainCharacter.setHandleInput(true);
        stage.addActor(mainCharacter);
        collisionContext.addEntity(mainCharacter);

        addTestNus();
    }

    private void addTestNus() {
        for (int i = 0; i < 80; i++) {
            Nu nu = new Nu(collisionContext, eventDispatcher, atlas);
            stage.addActor(nu);
            Random random = new Random();
            int x, y;
            do {
                x = random.nextInt(400);
                y = random.nextInt(400);
            }
            while (collisionContext.collisionChecker.mapCollisions(new Rectangle(x, y, x + 16, y + 16)).size > 0);
            nu.setPosition(x, y);
            collisionContext.addEntity(nu);
        }
    }

    @Override
    public void render(float dt) {
        fpsLogger.log();
        update(dt);

        // Clean canvas
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the foreground that goes behind the Sprite(s)
        renderTileLayer(FOREGROUND_2);

        // Stage drawings
        // (Stages open the batch and close it again)
        batch.setProjectionMatrix(camera.combined);
        stage.draw();

        // Render the foreground that goes in front of the Sprite(s)
        renderTileLayer(FOREGROUND_1);

        // HUD always drawn on top
        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.draw();
    }

    private void update(float dt) {
        controllerEventEmitter.tick();

        hud.update(dt);
        stage.act();

        updateCamera();
        tiledMapRenderer.setView(camera);

        updateActorsZIndex();
    }

    private void updateCamera() {
        camera.position.x = mainCharacter.getX() + mainCharacter.getWidth() / 2;
        camera.position.y = mainCharacter.getY() + mainCharacter.getHeight() / 2;

        final int mapWidth = TiledMapUtil.mapPixelWidth(map);
        final int mapHeight = TiledMapUtil.mapPixelHeight(map);
        final float cameraHalfWidth = camera.viewportWidth / 2;
        final float cameraHalfHeight = camera.viewportHeight / 2;

        // Keep camera inside map
        camera.position.x = MathUtils.clamp(camera.position.x, cameraHalfWidth, mapWidth - cameraHalfWidth);
        camera.position.y = MathUtils.clamp(camera.position.y, cameraHalfHeight, mapHeight - cameraHalfHeight);

        camera.update();
    }

    private void updateActorsZIndex() {
        final Array<Actor> actors = stage.getActors();
        actors.sort(yPositionCmp);
        for (int i = 0; i < actors.size; i++) {
            actors.get(i).setZIndex(i);
        }
    }

    /**
     * Render a tiled tile layer.
     *
     * @param name Name of the layer to render
     * @throws IllegalStateException If the layer doesn't exist
     */
    private void renderTileLayer(String name) {
        final TiledMapTileLayer tileLayer = (TiledMapTileLayer) map.getLayers().get(name);
        if (tileLayer == null) {
            throw new IllegalStateException("Layer not found: " + name);
        }
        tiledMapRenderer.getBatch().begin();
        tiledMapRenderer.renderTileLayer(tileLayer);
        tiledMapRenderer.getBatch().end();
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
        map.dispose();
        tiledMapRenderer.dispose();
    }
}
