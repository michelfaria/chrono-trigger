/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:44 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.hud;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.animation.ScissorAnimator;
import io.michelfaria.chrono.controller.Buttons;
import io.michelfaria.chrono.events.*;
import io.michelfaria.chrono.hud.actor.DialogBox;
import io.michelfaria.chrono.util.GroupUtil;

public class WalkHud implements Disposable, EventListener {

    private final Batch batch;
    private final EventDispatcher eventDispatcher;

    public final OrthographicCamera camera;
    public final Viewport viewport;
    public final Stage stage;
    public final DialogBox dialogBox;
    public final ScissorAnimator scissorAnimator;

    public WalkHud(Batch batch, MenuBoxes menuBoxes, AssetManager assetManager,
                   EventDispatcher eventDispatcher) {
        this.batch = batch;
        this.eventDispatcher = eventDispatcher;

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(256, 224);
        this.stage = new Stage(viewport, batch);

        // Set up dialog box
        menuBoxes.setUiType(0);
        this.dialogBox = new DialogBox(assetManager, menuBoxes);

        // Set up animator for the dialog box
        this.scissorAnimator = new ScissorAnimator(new Rectangle(dialogBox.getX(),
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

        } else if (event instanceof ButtonEvent) {
            ButtonEvent buttonEvent = (ButtonEvent) event;
            if (buttonEvent.getButton() == Buttons.A
                    && buttonEvent.getEventType() == ButtonEventType.PRESS) {
                if (isDialogBoxOpen()) {
                    closeDialogBox();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isDialogBoxOpen() {
        return scissorAnimator.getSpriteState() == ScissorAnimator.AnimationState.OPENED;
    }

    private void openDialogBox(String text) {
        dialogBox.setText(text);
        eventDispatcher.emitEvent(new HudPauseEvent(true));
        scissorAnimator.open();
    }

    private void closeDialogBox() {
        eventDispatcher.emitEvent(new HudPauseEvent(false));
        scissorAnimator.close();
    }

    @Override
    public int priority() {
        return 1;
    }
}
