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
import io.michelfaria.chrono.graphics.TileLayerRender;
import io.michelfaria.chrono.interfaces.Combatant;
import io.michelfaria.chrono.logic.BattlePointsValidator;
import io.michelfaria.chrono.logic.zindex.ActorZIndexUpdater;
import io.michelfaria.chrono.ui.DefaultHud;

import java.util.List;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static io.michelfaria.chrono.MapConstants.LAYER_FG_1;
import static io.michelfaria.chrono.MapConstants.LAYER_FG_2;
import static io.michelfaria.chrono.logic.BattlePointsValidator.MINIMUM_PARTY_BATTLEPOINTS_PER_GROUP;
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
        {
            priority = 1;
        }

        @Override
        public void buttonPressed(int controller, Buttons button) {
            if (button == Buttons.X) {
                Crono crono = new Crono();
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
        assert Game.map != null;
        assert Game.mapRenderer != null;
        Game.map.dispose();
        Game.mapRenderer.dispose();
        Game.map = null;
        Game.mapRenderer = null;
        for (Actor actor : stage.getActors()) {
            if (actor instanceof Disposable) {
                ((Disposable) actor).dispose();
            }
        }
        GameInput.removeObserver(gameInputObserver);
    }

    public void beginBattle(Combatant combatant) {
        List<BattlePoint> matching = Combatant.findMatchingBattlePoints(combatant);
        if (matching.isEmpty()) {
            throw new IllegalStateException("Cannot begin battle: No BattlePoints with SubID " + combatant.getId() + " found.");
        }

        BattlePoint closest = Combatant.findClosestBattlePointFromList(combatant, matching);
        assert closest != null;

        List<BattlePoint> battlePointGroup = closest.getAllInTheSameGroup();
        assert battlePointGroup.size() >= MINIMUM_PARTY_BATTLEPOINTS_PER_GROUP : battlePointGroup.size();

        this.callCombatantsToBattle(battlePointGroup);
    }

    private void callCombatantsToBattle(List<BattlePoint> battlePointGroup) {
        int called = 0;
        for (Actor actor : stage.getActors()) {
            if (actor instanceof Combatant) {
                if (actor instanceof PartyCharacter) {
                    PartyCharacter partyCharacter = (PartyCharacter) actor;
                    int partyCharacterIndex = Game.party.indexOf(partyCharacter, true);
                    assert partyCharacterIndex >= 0;
                    for (BattlePoint battlePoint : battlePointGroup) {
                        if (battlePoint.subId == partyCharacterIndex && battlePoint.type == BattlePoint.Type.PARTY) {
                            partyCharacter.goToBattle(battlePoint);
                            called++;
                        }
                    }
                } else {
                    Combatant combatant = (Combatant) actor;
                    for (BattlePoint battlePoint : battlePointGroup) {
                        if (battlePoint.subId == combatant.getId() && battlePoint.type == BattlePoint.Type.ENEMY) {
                            combatant.goToBattle(battlePoint);
                            called++;
                        }
                    }
                }
            }
        }
        assert called <= battlePointGroup.size() && called > 0
                : "called=" + called + ",battlePointGroup.size()=" + battlePointGroup.size();
    }
}
