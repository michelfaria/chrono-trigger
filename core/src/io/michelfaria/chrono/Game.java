/*
 * Developed by Michel Faria on 10/29/18 6:44 PM.
 * Last modified 10/29/18 6:44 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import io.michelfaria.chrono.actors.BattlePoint;
import io.michelfaria.chrono.actors.PartyCharacter;
import io.michelfaria.chrono.control.GameInput;
import io.michelfaria.chrono.graphics.TileLayerRenderer;
import io.michelfaria.chrono.interfaces.CollisionEntity;
import io.michelfaria.chrono.interfaces.Combatant;
import io.michelfaria.chrono.logic.BattlePointsValidator;
import io.michelfaria.chrono.logic.collision.CollisionChecker;
import io.michelfaria.chrono.logic.collision.CollisionEntityMover;
import io.michelfaria.chrono.ui.MenuBoxes;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public final class Game extends com.badlogic.gdx.Game {

    public static class Context {
        /*
         * Constants
         */
        public final int VRESX = 256;
        public final int VRESY = 224;

        /*
         * General
         */
        public Batch batch;
        public TmxMapLoader tmxMapLoader;
        public AssetManager assetManager;
        public FPSLogger fpsLogger;
        public GameInput gameInput;
        public TiledMap map;
        public OrthogonalTiledMapRenderer mapRenderer;

        /*
         * State
         */
        public Set<CollisionEntity> collisionEntities = new HashSet<>();
        public Array<PartyCharacter> party = new Array<>(Actor.class);
        public Set<BattlePoint> battlePoints = new HashSet<>();
        public AtomicInteger paused = new AtomicInteger(0);

        /*
         * Dynamic Methods
         */
        public Consumer<String> openDialogBox = null;
        public Consumer<Combatant> beginBattle = null;

        /*
         * Strategies
         */
        public CollisionChecker collisionChecker = new CollisionChecker(this);
        public TileLayerRenderer tileLayerRenderer = new TileLayerRenderer(this);
        public CollisionEntityMover collisionEntityMover = new CollisionEntityMover(this);
        public TiledMapStagePopulator tiledMapStagePopulator = new TiledMapStagePopulator(this);
        public BattlePointsValidator battlePointsValidator = new BattlePointsValidator();
        public MenuBoxes menuBoxes = new MenuBoxes(this);

        private Context() {}

        public TextureAtlas getMainTextureAtlas() {
            return assetManager.get(Assets.CHRONO_ATLAS);
        }

        private void dispose() {
            batch.dispose();
            assetManager.dispose();

            assert map == null;
            assert mapRenderer == null;
            assert collisionEntities.size() == 0 : collisionEntities;
            assert battlePoints.size() == 0 : battlePoints;
            assert gameInput.observersSize() == 0 : gameInput.getObserversCopy();
        }
    }

    private Context ctx;
    private DefaultScreen screen;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        ctx = new Context();

        ctx.batch = new SpriteBatch();
        ctx.tmxMapLoader = new TmxMapLoader();
        ctx.assetManager = new AssetManager();
        ctx.fpsLogger = new FPSLogger();
        ctx.gameInput = new GameInput();

        ctx.assetManager.load(Assets.CHRONO_ATLAS);
        ctx.assetManager.load(Assets.FONT);

        ctx.assetManager.finishLoading();

        screen = new DefaultScreen(ctx);
        setScreen(screen);
    }

    @Override
    public void dispose() {
        screen.dispose();
        ctx.dispose();
    }
}
