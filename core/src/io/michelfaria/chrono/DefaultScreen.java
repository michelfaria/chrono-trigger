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
import io.michelfaria.chrono.actors.BattlePoint;
import io.michelfaria.chrono.actors.Crono;
import io.michelfaria.chrono.actors.PartyCharacter;
import io.michelfaria.chrono.control.Buttons;
import io.michelfaria.chrono.control.GameInput;
import io.michelfaria.chrono.interfaces.Positionable;
import io.michelfaria.chrono.logic.zindex.ActorZIndexUpdater;
import io.michelfaria.chrono.ui.DefaultHud;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static io.michelfaria.chrono.MapConstants.LAYER_FG_1;
import static io.michelfaria.chrono.MapConstants.LAYER_FG_2;
import static io.michelfaria.chrono.util.TiledMapUtil.mapPixelHeight;
import static io.michelfaria.chrono.util.TiledMapUtil.mapPixelWidth;

public final class DefaultScreen implements Screen {

    private Game.Context ctx;
    private DefaultHud hud;

    /*
    Scene2D
     */

    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private Positionable cameraTarget;

    private GameInput.GameInputObserverAdapter gameInputObserver;

    public DefaultScreen(Game.Context ctx) {
        this.ctx = ctx;
        this.hud = new DefaultHud(ctx);

        ctx.map = ctx.tmxMapLoader.load(Assets.EXAMPLE_TMX);
        ctx.mapRenderer = new OrthogonalTiledMapRenderer(ctx.map);

        camera = new OrthographicCamera();
        viewport = new FitViewport(ctx.VRESX, ctx.VRESY, camera);
        stage = new Stage(viewport, ctx.batch);

        gameInputObserver = new GameInput.GameInputObserverAdapter() {
            @Override
            public void buttonPressed(int controller, Buttons button) {
                if (button == Buttons.X) {
                    Crono crono = new Crono(ctx);
                    stage.addActor(crono);
                }
            }
        };
        ctx.gameInput.addObserver(gameInputObserver);
    }

    @Override
    public void show() {
        stage.addActor(new Crono(ctx));
        ctx.tiledMapStagePopulator.populateStage(ctx.map, stage);
        ctx.battlePointsValidator.validateBattlePoints(stage.getActors());
    }

    @Override
    public void render(float delta) {
        ctx.fpsLogger.log();

        //
        ctx.gameInput.tick();
        hud.update();

        //
        stage.act();

        //
        updateCameraTarget();
        updateCamera();
        ctx.mapRenderer.setView(camera);

        //
        ActorZIndexUpdater.updateActorsZIndex(stage.getActors());

        // Clear
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the foreground that goes behind the sprites
        ctx.tileLayerRenderer.renderTileLayer(LAYER_FG_2);

        // Draw the stage
        // (Stages open the batch and close it again)
        ctx.batch.setProjectionMatrix(camera.combined);
        stage.draw();

        // Render the foreground that goes in front of the sprites
        ctx.tileLayerRenderer.renderTileLayer(LAYER_FG_1);

        // Draw HUD last
        hud.draw();
    }

    private void updateCameraTarget() {
        if (ctx.battleStatus.isEngaged() && !(cameraTarget instanceof BattlePoint)) {
            Integer battleGroup = ctx.battleStatus.getBattleGroup();

            // Because if BattleStatus.isEngaged returns true, then there _should_ be a battleGroup
            assert battleGroup != null;

            BattlePoint cameraPoint = BattlePoint.findCameraForGroup(ctx, battleGroup);
            assert cameraPoint != null;

            cameraTarget = cameraPoint;
        } else if (!ctx.battleStatus.isEngaged() && !(cameraTarget instanceof PartyCharacter)) {
            cameraTarget = ctx.party.get(0);
        }
    }

    private void updateCamera() {
        assert cameraTarget != null;
        if (ctx.battleStatus.isEngaged()) {
            assert cameraTarget instanceof BattlePoint;
            assert ((BattlePoint) cameraTarget).type == BattlePoint.Type.CAMERA;
        } else {
            assert cameraTarget instanceof PartyCharacter;
        }

        camera.position.x = cameraTarget.getX() + cameraTarget.getWidth() / 2;
        camera.position.y = cameraTarget.getY() + cameraTarget.getHeight() / 2;

        int mapWidth = mapPixelWidth(ctx.map);
        int mapHeight = mapPixelHeight(ctx.map);
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
        hud.resize(width, height);
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
        assert ctx.map != null;
        assert ctx.mapRenderer != null;
        ctx.map.dispose();
        ctx.mapRenderer.dispose();
        ctx.map = null;
        ctx.mapRenderer = null;
        for (Actor actor : stage.getActors()) {
            if (actor instanceof Disposable) {
                ((Disposable) actor).dispose();
            }
        }
        stage.dispose();
        ctx.gameInput.removeObserver(gameInputObserver);
    }
}
