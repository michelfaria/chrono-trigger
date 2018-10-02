package io.michelfaria.chrono;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.actor.Crono;
import io.michelfaria.chrono.hud.PlayHud;

public class PlayScreen implements Screen {

    private Game game;

    private InputMultiplexer multiplexer;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    private PlayHud hud;

    public PlayScreen(Game game) {
        this.game = game;

        multiplexer = new InputMultiplexer();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Game.V_WIDTH, Game.V_HEIGHT, camera);
        stage = new Stage(viewport);

        hud = new PlayHud(game);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(stage);

        // Add the character (Testing)
        Crono crono = new Crono(game, hud);
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
        game.batch.setProjectionMatrix(camera.combined);
        stage.draw();

        game.batch.begin();
        // All screen's drawing activities below


        // End drawing
        game.batch.end();

        // PlayHud always drawn on top
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    private void update(float dt) {
        hud.update(dt);

        stage.act();
        camera.update();
    }

    @Override
    public void resize(int width, int height) {
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
