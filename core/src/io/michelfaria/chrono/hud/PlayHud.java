package io.michelfaria.chrono.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.animation.ChronoOpenClosingAnimator;
import io.michelfaria.chrono.util.ViewportUtil;

public class PlayHud implements Disposable {

    public OrthographicCamera camera;
    public Viewport viewport;
    public Stage stage;

    private Sprite dialogBox;
    private ChronoOpenClosingAnimator dialogBoxAnimator;

    public PlayHud() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Core.V_WIDTH, Core.V_HEIGHT);
        stage = new Stage(viewport, Core.batch);

        setDialogBoxType(0);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void draw() {
        stage.draw();
        dialogBoxAnimator.draw();
    }

    public void update() {
        stage.act();

        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            dialogBoxAnimator.open();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
            dialogBoxAnimator.close();
        }
    }

    /**
     * Sets the dialog box for the Hud.
     */
    public void setDialogBoxType(int type) {
        if (type < 0) {
            throw new IllegalArgumentException("type must be >= 0");
        }
        MenuBoxes.setUiType(type);
        dialogBox = new Sprite(MenuBoxes.getDialogBox());
        dialogBoxAnimator = new ChronoOpenClosingAnimator(dialogBox, viewport);
    }



}
