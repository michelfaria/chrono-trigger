package io.michelfaria.chrono.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.actor.Crono;
import io.michelfaria.chrono.hud.PlayHud;
import io.michelfaria.chrono.util.TxUtil;
import io.michelfaria.chrono.values.Assets;

public class PlayScreen implements Screen {

    private InputMultiplexer multiplexer;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    private PlayHud hud;

    private float stateTime;

    public PlayScreen() {
        multiplexer = new InputMultiplexer();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Core.V_WIDTH, Core.V_HEIGHT, camera);
        stage = new Stage(viewport);

        hud = new PlayHud();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(hud.stage);

        // Add the character (Testing)
        Crono crono = new Crono();
        stage.addActor(crono);
        crono.setHandleInput(true);
    }

    @Override
    public void render(float dt) {
        update(dt);

        // Clean canvas
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Stage drawings
        // (Unfortunately, Stages open the batch and close it again)
        Core.batch.setProjectionMatrix(camera.combined);
        stage.draw();

        Core.batch.begin();
        // All screen's drawing activities below


        // ...

        // End drawing
        Core.batch.end();

        // Hud always drawn on top
        Core.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.draw();
    }

    private void update(float dt) {
        stateTime += Gdx.graphics.getDeltaTime();
        hud.update();
        stage.act();
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
    }
}
