package io.michelfaria.chrono.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.State;
import io.michelfaria.chrono.animation.ScissorAnimator;
import io.michelfaria.chrono.events.APressEvent;
import io.michelfaria.chrono.events.Event;
import io.michelfaria.chrono.events.EventListener;
import io.michelfaria.chrono.events.OpenDialogBoxEvent;
import io.michelfaria.chrono.hud.actor.DialogBox;
import io.michelfaria.chrono.util.GroupUtil;

public class WalkHud implements Disposable, EventListener {

    private State state;
    private Batch batch;

    public OrthographicCamera camera;
    public Viewport viewport;
    public Stage stage;
    public DialogBox dialogBox;
    public ScissorAnimator scissorAnimator;

    public WalkHud(Batch batch, MenuBoxes menuBoxes, AssetManager assetManager, State state) {
        this.batch = batch;
        this.state = state;

        camera = new OrthographicCamera();
        viewport = new FitViewport(256, 224);
        stage = new Stage(viewport, batch);

        // Set up dialog box
        menuBoxes.setUiType(0);
        dialogBox = new DialogBox(assetManager, menuBoxes);

        // Set up animator for the dialog box
        scissorAnimator = new ScissorAnimator(new Rectangle(dialogBox.getX(),
                dialogBox.getY(), GroupUtil.getWidth(dialogBox), GroupUtil.getHeight(dialogBox)),
                viewport);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void draw() {
        stage.draw();
        batch.begin();

        scissorAnimator.scissor(() -> dialogBox.draw(batch, 1));

        batch.end();
    }

    public void update(float delta) {
        stage.act(delta);
        dialogBox.act(delta);
    }

    @Override
    public boolean handleEvent(Event event) {
        if (event instanceof OpenDialogBoxEvent) {
            openDialogBox(((OpenDialogBoxEvent) event).text);
            return true;
        } else if (event instanceof APressEvent) {
            if (isDialogBoxOpen()) {
                closeDialogBox();
            }
        }
        return false;
    }

    private boolean isDialogBoxOpen() {
        return scissorAnimator.spriteState == ScissorAnimator.AnimationState.OPENED;
    }

    private void openDialogBox(String text) {
        dialogBox.setText(text);
        state.hudPause = true;
        scissorAnimator.open();
    }

    private void closeDialogBox() {
        state.hudPause = false;
        scissorAnimator.close();
    }
}
