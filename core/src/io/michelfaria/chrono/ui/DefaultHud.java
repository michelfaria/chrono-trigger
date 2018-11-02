/*
 * Developed by Michel Faria on 10/29/18 7:33 PM.
 * Last modified 10/29/18 7:33 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.animation.ScissorAnimator;
import io.michelfaria.chrono.control.Buttons;
import io.michelfaria.chrono.control.GameInput;
import io.michelfaria.chrono.util.GroupUtil;


@SuppressWarnings("FieldCanBeLocal")
public final class DefaultHud {

    private Game.Context ctx;

    private Viewport viewport;
    private Stage stage;
    private DialogBoxGroup dialogBoxGroup;
    private ScissorAnimator scissorAnimator;

    private GameInput.GameInputObserverAdapter gameInputObserver = new GameInput.GameInputObserverAdapter() {
        @Override
        public void buttonPressed(int controller, Buttons button) {
            if (button == Buttons.A) {
                closeDialogBox();
            }
        }
    };

    public DefaultHud(Game.Context ctx) {
        this.ctx = ctx;

        viewport = new FitViewport(ctx.VRESX, ctx.VRESY);
        stage = new Stage(viewport, ctx.batch);
        dialogBoxGroup = new DialogBoxGroup(ctx);
        scissorAnimator =
                new ScissorAnimator(
                        new Rectangle(
                                dialogBoxGroup.getX(),
                                dialogBoxGroup.getY(),
                                GroupUtil.getWidth(dialogBoxGroup),
                                GroupUtil.getHeight(dialogBoxGroup)),
                        viewport);
        ctx.gameInput.addObserver(gameInputObserver);

        ctx.openDialogBox = this::openDialogBox;
    }

    public void draw() {
        ctx.batch.setProjectionMatrix(stage.getCamera().combined);
        stage.draw();

        ctx.batch.begin();
        scissorAnimator.scissor(() -> dialogBoxGroup.draw(ctx.batch, 1));
        ctx.batch.end();
    }

    public void update() {
        stage.act();
        dialogBoxGroup.act(Gdx.graphics.getDeltaTime());
    }

    public void openDialogBox(String text) {
        if (text != null) {
            dialogBoxGroup.text = text;
        }
        ctx.paused.incrementAndGet();
        scissorAnimator.open();
    }

    public boolean isDialogBoxOpen() {
        return scissorAnimator.getSpriteState() == ScissorAnimator.AnimationState.OPENED;
    }

    public void closeDialogBox() {
        if (isDialogBoxOpen()) {
            ctx.paused.decrementAndGet();
            scissorAnimator.close();
        }
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void dispose() {
        ctx.gameInput.removeObserver(gameInputObserver);
        ctx.openDialogBox = null;
    }
}
