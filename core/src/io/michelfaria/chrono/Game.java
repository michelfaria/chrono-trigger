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
import io.michelfaria.chrono.interfaces.CollisionEntity;
import io.michelfaria.chrono.ui.DefaultHud;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public final class Game extends com.badlogic.gdx.Game {

    public static final int VRESX = 256;
    public static final int VRESY = 224;

    public static Batch batch;
    public static TmxMapLoader tmxMapLoader;
    public static AssetManager assetManager;
    public static FPSLogger fpsLogger;

    public static TiledMap map;
    public static OrthogonalTiledMapRenderer mapRenderer;

    public static Set<CollisionEntity> collisionEntities = new HashSet<>();
    public static Array<PartyCharacter> party = new Array<>(Actor.class);
    public static Set<BattlePoint> battlePoints = new HashSet<>();

    public static AtomicInteger paused = new AtomicInteger(0);

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        batch = new SpriteBatch();
        tmxMapLoader = new TmxMapLoader();
        assetManager = new AssetManager();
        fpsLogger = new FPSLogger();

        assetManager.load(Assets.CHRONO_ATLAS);
        assetManager.load(Assets.FONT);

        assetManager.finishLoading();

        setScreen(DefaultScreen.getInstance().init());
    }

    @Override
    public void dispose() {
        batch.dispose();
        assetManager.dispose();
        DefaultScreen.disposeInstance();
        DefaultHud.dispose();

        assert map == null;
        assert mapRenderer == null;
        assert collisionEntities.size() == 0 : collisionEntities;
        assert battlePoints.size() == 0 : battlePoints;
        assert GameInput.observersSize() == 0 : GameInput.getObserversCopy();
    }

    public static TextureAtlas getMainTextureAtlas() {
        return assetManager.get(Assets.CHRONO_ATLAS);
    }
}
