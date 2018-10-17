package io.michelfaria.chrono.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.actor.Crono;
import io.michelfaria.chrono.actor.Nu;
import io.michelfaria.chrono.actor.PartyCharacter;
import io.michelfaria.chrono.hud.WalkHud;
import io.michelfaria.chrono.logic.ActorYPositionComparator;
import io.michelfaria.chrono.logic.CollisionContext;
import io.michelfaria.chrono.values.Assets;

import java.util.Random;

import static io.michelfaria.chrono.values.LayerNames.FOREGROUND_1;
import static io.michelfaria.chrono.values.LayerNames.FOREGROUND_2;

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
    private CollisionContext collisionContext;
    private ActorYPositionComparator yPositionCmp;
    private PartyCharacter mainCharacter;

    public WalkScreen(Core core) {
        this.core = core;

        multiplexer = new InputMultiplexer();
        camera = new OrthographicCamera();
        viewport = new FitViewport(core.getVirtualWidth(), core.getVirtualHeight(), camera);
        stage = new Stage(viewport);

        tiledMap = core.getTmxMapLoader().load(Assets.EXAMPLE_TMX);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        hud = new WalkHud(core);
        yPositionCmp = new ActorYPositionComparator();
        collisionContext = new CollisionContext(this.tiledMap);
        mainCharacter = new Crono(core, collisionContext);
    }

    @Override
    public void show() {
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(hud.stage);
        Gdx.input.setInputProcessor(multiplexer);

        mainCharacter.setHandleInput(true);
        stage.addActor(mainCharacter);
        collisionContext.addEntity(mainCharacter);

        for (int i = 0; i < 80; i++) {// TESTING
            Nu nu = new Nu(core, collisionContext);// TESTING
            stage.addActor(nu);// TESTING
            Random random = new Random();// TESTING
            int x;// TESTING
            int y;// TESTING
            do {// TESTING
                x = random.nextInt(400);// TESTING
                y = random.nextInt(400);// TESTING
            } while (collisionContext.collisionChecker.mapCollisions(new Rectangle(x, y, x + 16, y + 16)).size > 0);// TESTING
            nu.setPosition(x, y);// TESTING
            collisionContext.addEntity(nu);
        }// TESTING
    }

    @Override
    public void render(float dt) {
        update(dt);

        // Clean canvas
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the foreground that goes behind the Sprite(s)
        renderTileLayer(FOREGROUND_2);

        // Stage drawings
        // (Stages open the batch and close it again)
        core.getBatch().setProjectionMatrix(camera.combined);
        stage.draw();

        // Render the foreground that goes in front of the Sprite(s)
        renderTileLayer(FOREGROUND_1);

        // HUD always drawn on top
        core.getBatch().setProjectionMatrix(hud.stage.getCamera().combined);
        hud.draw();
    }

    private void update(float dt) {
        hud.update(dt);
        stage.act();

        updateCamera();
        tiledMapRenderer.setView(camera);

        updateActorsZIndex();
    }

    private void updateCamera() {
        camera.position.x = mainCharacter.getX();
        camera.position.y = mainCharacter.getY();
        camera.update();
    }

    private void updateActorsZIndex() {
        Array<Actor> actors = stage.getActors();
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
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(name);
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
        tiledMapRenderer.dispose();
    }
}
