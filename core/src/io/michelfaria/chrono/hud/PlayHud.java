package io.michelfaria.chrono.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.animation.ScissorAnimator;
import io.michelfaria.chrono.hud.actor.DialogBox;
import io.michelfaria.chrono.util.GroupUtil;

public class PlayHud implements Disposable {

    public OrthographicCamera camera;
    public Viewport viewport;
    public Stage stage;

    private DialogBox dialogBox;
    private ScissorAnimator scissorAnimator;

    public PlayHud() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Core.V_WIDTH, Core.V_HEIGHT);
        stage = new Stage(viewport, Core.batch);

        // Set up dialog box
        setDialogBoxType(0);
        dialogBox = new DialogBox();

        // Set up animator for the dialog box
        scissorAnimator = new ScissorAnimator(
                new Rectangle(dialogBox.getX(), dialogBox.getY(),
                        GroupUtil.getWidth(dialogBox),
                        GroupUtil.getHeight(dialogBox)), viewport);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void draw() {
        stage.draw();
        Core.batch.begin();


        scissorAnimator.scissor(() -> {
            dialogBox.draw(Core.batch, 1);
        });


        Core.batch.end();
    }

    public void update(float delta) {
        stage.act(delta);
        dialogBox.act(delta);

        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            scissorAnimator.open();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
            scissorAnimator.close();
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
    }
}
