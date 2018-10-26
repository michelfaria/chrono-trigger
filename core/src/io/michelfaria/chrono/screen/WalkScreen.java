/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:44 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.screen;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.actor.Crono;
import io.michelfaria.chrono.consts.Assets;
import io.michelfaria.chrono.controller.Buttons;
import io.michelfaria.chrono.controller.ControllerEventEmitter;
import io.michelfaria.chrono.events.*;
import io.michelfaria.chrono.hud.MenuBoxes;
import io.michelfaria.chrono.hud.WalkHud;
import io.michelfaria.chrono.logic.*;
import io.michelfaria.chrono.util.TiledMapUtil;

import static io.michelfaria.chrono.consts.MapConstants.LAYER_FG_1;
import static io.michelfaria.chrono.consts.MapConstants.LAYER_FG_2;

public class WalkScreen implements Screen, EventListener {

    private final EventDispatcher eventDispatcher;
    private final TextureAtlas atlas;
    private final Batch batch;
    private final ControllerEventEmitter controllerEventEmitter;

    /*
    LibGDX
     */
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
    private final TiledMapStagePopulator tiledMapStagePopulator;

    /*
    Other
     */
    private final WalkHud hud;
    private final CollisionContext collisionContext;
    private final ActorYPositionComparator yPositionCmp = new ActorYPositionComparator();

    private final Party party = new Party();

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
        this.tiledMapStagePopulator = new TiledMapStagePopulator(collisionContext, eventDispatcher, atlas);
    }

    @Override
    public void show() {
        addParty();
        tiledMapStagePopulator.populate(map, stage);
        BattlePointsValidator.validateBattlePoints(stage.getActors());
    }

    private void addParty() {
        Crono crono = new Crono(collisionContext, atlas, eventDispatcher, party);
        stage.addActor(crono);
    }

    @Override
    public void render(float dt) {
        fpsLogger.log();
        update(dt);

        // Clean canvas
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the foreground that goes behind the Sprite(s)
        renderTileLayer(LAYER_FG_2);

        // Stage drawings
        // (Stages open the batch and close it again)
        batch.setProjectionMatrix(camera.combined);
        stage.draw();

        // Render the foreground that goes in front of the Sprite(s)
        renderTileLayer(LAYER_FG_1);

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
        camera.position.x = party.getLeader().getX() + party.getLeader().getWidth() / 2;
        camera.position.y = party.getLeader().getY() + party.getLeader().getHeight() / 2;

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

    @Override
    public boolean handleEvent(Event event) {
        if (event instanceof ButtonEvent) {
            ButtonEvent buttonEvent = (ButtonEvent) event;
            if (buttonEvent.getButton() == Buttons.Y && buttonEvent.getEventType() == ButtonEventType.PRESS) {
                addParty();
                return true;
            }
        }
        return false;
    }
}
