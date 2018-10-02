package io.michelfaria.chrono.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.Core;

public class PlayHud implements Disposable {

    public OrthographicCamera camera;
    public Viewport viewport;
    public Stage stage;

    private TextureRegion dialogBox;
    private boolean showDialogBox;
    private int dialogBoxGLScissor1;
    private int dialogBoxGLScissor2;

    public PlayHud() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Core.V_WIDTH, Core.V_HEIGHT);
        stage = new Stage(viewport, Core.batch);

        setDialogBoxType(0);
        showDialogBox = true;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void draw() {
        stage.draw();

        if (showDialogBox) {
            /*
            Draw dialog box
             */
            Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
            // Update the view port
            int screenWidth = viewport.getScreenWidth();
            int screenHeight = viewport.getScreenHeight();
            Gdx.gl.glViewport(0, 0, screenWidth, screenHeight);

            Gdx.gl.glScissor(0, (int)(viewport.getScreenHeight() / 5.5) - dialogBoxGLScissor1, viewport.getScreenWidth(), dialogBoxGLScissor2);
            dialogBoxGLScissor1 += 2;
            dialogBoxGLScissor2 += 4;

            Core.batch.begin();


            Core.batch.draw(dialogBox, 0, 0);


            Core.batch.end();
            Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
        }
    }

    public void update() {
        stage.act();
    }

    public void setDialogBoxType(int type) {
        if (type < 0) {
            throw new IllegalArgumentException("type must be >= 0");
        }
        MenuBoxes.setUiType(type);
        dialogBox = MenuBoxes.getDialogBox();
    }
}
