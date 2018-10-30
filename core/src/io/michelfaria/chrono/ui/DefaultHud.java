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

    private static Viewport viewport;
    private static Stage stage;
    private static DialogBoxGroup dialogBoxGroup;
    private static ScissorAnimator scissorAnimator;

    private static GameInput.GameInputObserverAdapter gameInputObserver = new GameInput.GameInputObserverAdapter() {
        @Override
        public void buttonPressed(int controller, Buttons button) {
            if (button == Buttons.A) {
                closeDialogBox();
            }
        }
    };

    static {
        viewport = new FitViewport(Game.VRESX, Game.VRESY);
        stage = new Stage(viewport, Game.batch);
        dialogBoxGroup = new DialogBoxGroup();
        scissorAnimator =
                new ScissorAnimator(
                        new Rectangle(
                                dialogBoxGroup.getX(),
                                dialogBoxGroup.getY(),
                                GroupUtil.getWidth(dialogBoxGroup),
                                GroupUtil.getHeight(dialogBoxGroup)),
                        viewport);
        GameInput.addObserver(gameInputObserver);
    }

    private DefaultHud() {
    }

    public static void draw() {
        Game.batch.setProjectionMatrix(stage.getCamera().combined);
        stage.draw();

        Game.batch.begin();
        scissorAnimator.scissor(() -> dialogBoxGroup.draw(Game.batch, 1));
        Game.batch.end();
    }

    public static void update() {
        stage.act();
        dialogBoxGroup.act(Gdx.graphics.getDeltaTime());
    }

    public static void openDialogBox(String text) {
        if (text != null) {
            dialogBoxGroup.text = text;
        }
        Game.paused.incrementAndGet();
        scissorAnimator.open();
    }

    public static boolean isDialogBoxOpen() {
        return scissorAnimator.getSpriteState() == ScissorAnimator.AnimationState.OPENED;
    }

    public static void closeDialogBox() {
        if (isDialogBoxOpen()) {
            Game.paused.decrementAndGet();
            scissorAnimator.close();
        }
    }

    public static void resize(int width, int height) {
        viewport.update(width, height);
    }

    public static void dispose() {
        GameInput.removeObserver(gameInputObserver);
    }
}
