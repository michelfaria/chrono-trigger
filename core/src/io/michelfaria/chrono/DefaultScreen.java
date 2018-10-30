/*
 * Developed by Michel Faria on 10/29/18 7:07 PM.
 * Last modified 10/29/18 7:07 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.actors.Crono;
import io.michelfaria.chrono.actors.PartyCharacter;
import io.michelfaria.chrono.control.Buttons;
import io.michelfaria.chrono.control.GameInput;
import io.michelfaria.chrono.graphics.TileLayerRender;
import io.michelfaria.chrono.logic.BattlePointsValidator;
import io.michelfaria.chrono.logic.zindex.ActorZIndexUpdater;
import io.michelfaria.chrono.ui.DefaultHud;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static io.michelfaria.chrono.MapConstants.LAYER_FG_1;
import static io.michelfaria.chrono.MapConstants.LAYER_FG_2;
import static io.michelfaria.chrono.util.TiledMapUtil.mapPixelHeight;
import static io.michelfaria.chrono.util.TiledMapUtil.mapPixelWidth;

public final class DefaultScreen implements Screen {

    private static DefaultScreen defaultScreen = null;

    private DefaultScreen() {
    }

    public static DefaultScreen getInstance() {
        if (defaultScreen == null) {
            defaultScreen = new DefaultScreen();
        }
        return defaultScreen;
    }

    public static void disposeInstance() {
        if (defaultScreen != null) {
            defaultScreen.dispose();
            defaultScreen = null;
        }
    }

    /*
    Scene2D
     */
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    private GameInput.GameInputObserverAdapter gameInputObserver = new GameInput.GameInputObserverAdapter() {
        @Override
        public void buttonPressed(int controller, Buttons button) {
            if (button == Buttons.X) {
                Crono crono = new Crono();
                Game.party.add(crono);
                stage.addActor(crono);
            }
        }
    };

    public DefaultScreen init() {
        Game.map = Game.tmxMapLoader.load(Assets.EXAMPLE_TMX);
        Game.mapRenderer = new OrthogonalTiledMapRenderer(Game.map);

        camera = new OrthographicCamera();
        viewport = new FitViewport(Game.VRESX, Game.VRESY, camera);
        stage = new Stage(viewport, Game.batch);

        GameInput.addObserver(gameInputObserver);

        return this;
    }

    @Override
    public void show() {
        stage.addActor(new Crono());
        TiledMapStagePopulator.populateStage(Game.map, stage);
        BattlePointsValidator.validateBattlePoints(stage.getActors());
    }

    @Override
    public void render(float delta) {
        Game.fpsLogger.log();

        //
        GameInput.tick();
        DefaultHud.update();

        //
        stage.act();

        // Update camera
        updateCamera();
        Game.mapRenderer.setView(camera);

        //
        ActorZIndexUpdater.updateActorsZIndex(stage.getActors());

        // Clear
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the foreground that goes behind the sprites
        TileLayerRender.renderTileLayer(LAYER_FG_2);

        // Draw the stage
        // (Stages open the batch and close it again)
        Game.batch.setProjectionMatrix(camera.combined);
        stage.draw();

        // Render the foreground that goes in front of the sprites
        TileLayerRender.renderTileLayer(LAYER_FG_1);

        // Draw HUD last
        DefaultHud.draw();
    }

    private void updateCamera() {
        final PartyCharacter leader = Game.party.get(0);
        camera.position.x = leader.getX() + leader.getWidth() / 2;
        camera.position.y = leader.getY() + leader.getHeight() / 2;

        int mapWidth = mapPixelWidth(Game.map);
        int mapHeight = mapPixelHeight(Game.map);
        float cameraHalfWidth = camera.viewportWidth / 2;
        float cameraHalfHeight = camera.viewportHeight / 2;

        // Keep camera inside map
        camera.position.x = clamp(camera.position.x, cameraHalfWidth, mapWidth - cameraHalfWidth);
        camera.position.y = clamp(camera.position.y, cameraHalfHeight, mapHeight - cameraHalfHeight);

        camera.update();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        DefaultHud.resize(width, height);
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
        Game.map.dispose();
        Game.map = null;
        Game.mapRenderer = null;
        for (Actor actor : stage.getActors()) {
            if (actor instanceof Disposable) {
                ((Disposable) actor).dispose();
            }
        }
        GameInput.removeObserver(gameInputObserver);
    }
}
