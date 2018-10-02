package io.michelfaria.chrono.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.Core;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayHud implements Disposable {

    private enum DialogBoxState {
        OPENING, OPENED, CLOSING, CLOSED
    }

    public OrthographicCamera camera;
    public Viewport viewport;
    public Stage stage;

    @Nullable
    private TextureRegion dialogBox;
    @NotNull
    private DialogBoxState dialogBoxState;

    private int dialogBoxGLScissor;

    private static final int SCISSOR1_MULTIPLE = 9;

    public PlayHud() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Core.V_WIDTH, Core.V_HEIGHT);
        stage = new Stage(viewport, Core.batch);

//        dialogBoxState = DialogBoxState.CLOSED;
        dialogBoxState = DialogBoxState.OPENING;
        setDialogBoxType(0);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void draw() {
        stage.draw();

        boolean doScissoring = false;

        // Update the view port
        Gdx.gl.glViewport(0, 0, viewport.getScreenWidth(), viewport.getScreenHeight());

        if (dialogBoxState.equals(DialogBoxState.OPENING)) {
            /*
            Open dialog box
             */
            doScissoring = true;

            assert dialogBoxGLScissor == 0;

            if (dialogBoxGLScissor < viewport.getScreenHeight()) {
                // Scissor hasn't covered entire screen yet
                // Increase scissor span
                dialogBoxGLScissor += SCISSOR1_MULTIPLE;
            } else {
                // Scissor has covered the entire screen
                dialogBoxState = DialogBoxState.OPENED;
            }
        } else if (dialogBoxState.equals(DialogBoxState.CLOSING)) {
            /*
            Close dialog box
             */
            doScissoring = true;

            assert dialogBoxGLScissor > 0;

            // Decrease scissor span
            dialogBoxGLScissor -= SCISSOR1_MULTIPLE;
        }

        if (doScissoring) {
            Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
            Gdx.gl.glScissor(0, (int) (viewport.getScreenHeight() / 5.5) - dialogBoxGLScissor, viewport.getScreenWidth(), dialogBoxGLScissor * 2);

            if (dialogBoxGLScissor == 0) {
                dialogBoxState = DialogBoxState.CLOSED;
            }
        }

        if (!dialogBoxState.equals(DialogBoxState.CLOSED)) {
            Core.batch.begin();
            /*
            Draw dialog box
             */
            if (dialogBox == null) {
                throw new IllegalStateException("No dialog box set");
            }
            Core.batch.draw(dialogBox, 0, 0);
            Core.batch.end();
        }

        if (doScissoring) {
            Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
        }
    }

    public void update() {
        stage.act();

        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            dialogBoxState = DialogBoxState.CLOSING;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
            dialogBoxState = DialogBoxState.OPENING;
        }
    }

    public void setDialogBoxType(int type) {
        if (type < 0) {
            throw new IllegalArgumentException("type must be >= 0");
        }
        MenuBoxes.setUiType(type);
        dialogBox = MenuBoxes.getDialogBox();
    }
}
