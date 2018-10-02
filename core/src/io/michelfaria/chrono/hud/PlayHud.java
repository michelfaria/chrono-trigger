package io.michelfaria.chrono.hud;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.Core;

public class PlayHud implements Disposable {

    public OrthographicCamera camera;
    public Viewport viewport;
    public Stage stage;

    private Table stdDialogBox;

    public PlayHud() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Core.V_WIDTH, Core.V_HEIGHT);
        stage = new Stage(viewport, Core.batch);

        stdDialogBox = DialogBoxes.get(DialogBoxes.STANDARD_HEIGHT);
        stage.addActor(stdDialogBox);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void update() {
        stage.act();
    }
}
