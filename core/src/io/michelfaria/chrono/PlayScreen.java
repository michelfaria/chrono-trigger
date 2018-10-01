package io.michelfaria.chrono;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.actor.Crono;

public class PlayScreen implements Screen {

    private Game game;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    public PlayScreen(Game game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(Game.V_WIDTH, Game.V_HEIGHT, camera);
        stage = new Stage(viewport);
    }

    private void update(float dt) {
        stage.act();
        camera.update();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.addActor(new Crono(game));
    }

    @Override
    public void render(float dt) {
        update(dt);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        stage.draw();

        game.batch.end();
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
        stage.dispose();
    }
}
