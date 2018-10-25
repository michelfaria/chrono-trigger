/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:38 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.util.GLUtil;

import static io.michelfaria.chrono.animation.ScissorAnimator.AnimationState.*;

public class ScissorAnimator {

    private static final int SCISSOR_MULTIPLE = 12;
    private final Rectangle vRectangle;
    private final Viewport vp;

    private AnimationState spriteState;
    private int scissor;

    /**
     * @param vRectangle Virtual rectangle region to animate
     * @param vp         Viewport
     */
    public ScissorAnimator(Rectangle vRectangle, Viewport vp) {
        this.vRectangle = vRectangle;
        this.vp = vp;
        this.spriteState = AnimationState.CLOSED;
    }

    public void scissor(Runnable draw) {
        // Update the view port
        Gdx.gl.glViewport(0, 0, vp.getScreenWidth(), vp.getScreenHeight());

        if (getSpriteState().equals(OPENING)) {
            /*
             * Open dialog box
             */
            if (scissor < getRealYCenter()) {
                scissor += SCISSOR_MULTIPLE;
            } else {
                // Scissor has covered the sprite
                spriteState = AnimationState.OPENED;
            }
        } else if (getSpriteState().equals(CLOSING)) {
            /*
             * Close dialog box
             */
            if (scissor > 0) {
                scissor -= SCISSOR_MULTIPLE;
            }
            if (scissor <= 0) {
                scissor = 0;
                spriteState = AnimationState.CLOSED;
            }
        }

        boolean isAnimating = getSpriteState() == OPENING || getSpriteState() == CLOSING;

        if (isAnimating) {
            Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
            Gdx.gl.glScissor(0, getRealYCenter() - scissor, vp.getScreenWidth(), scissor * 2);
        }

        if (getSpriteState() != CLOSED) {
            draw.run();
        }

        if (isAnimating) {
            Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
        }
    }

    public void open() {
        spriteState = OPENING;
        scissor = 0;
    }

    public void close() {
        spriteState = CLOSING;
        scissor = getRealYCenter();
    }

    /**
     * Returns the real (non-virtual) Y coordinate of the center of the rectangle.
     */
    private int getRealYCenter() {
        return (int) (GLUtil.getRealSize(vp, vRectangle, 256, 224).height / 2);
    }

    public AnimationState getSpriteState() {
        return spriteState;
    }

    public enum AnimationState {
        OPENED, CLOSED, OPENING, CLOSING
    }
}
