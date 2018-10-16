package io.michelfaria.chrono.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.util.GLUtil;

import static io.michelfaria.chrono.animation.ScissorAnimator.AnimationState.*;

public class ScissorAnimator {

    private static final int SCISSOR_MULTIPLE = 12;
    public Rectangle vRectangle;
    public Viewport vp;
    private Core core;
    private int scissor;
    private AnimationState spriteState;

    /**
     * @param core       Class containing the virtual width and virtual height of the game
     * @param vRectangle Virtual rectangle region to animate
     * @param vp         Viewport
     */
    public ScissorAnimator(Core core, Rectangle vRectangle, Viewport vp) {
        this.core = core;
        this.vRectangle = vRectangle;
        this.vp = vp;
        this.spriteState = AnimationState.CLOSED;
    }

    public void scissor(Runnable draw) {
        // Update the view port
        Gdx.gl.glViewport(0, 0, vp.getScreenWidth(), vp.getScreenHeight());

        if (spriteState.equals(OPENING)) {
            /*
             * Open dialog box
             */
            if (scissor < getRealYCenter()) {
                scissor += SCISSOR_MULTIPLE;
            } else {
                // Scissor has covered the sprite
                spriteState = AnimationState.OPENED;
            }
        } else if (spriteState.equals(CLOSING)) {
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

        boolean isAnimating = spriteState == OPENING || spriteState == CLOSING;

        if (isAnimating) {
            Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
            Gdx.gl.glScissor(0, getRealYCenter() - scissor, vp.getScreenWidth(), scissor * 2);
        }

        if (spriteState != CLOSED) {
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
        return (int) (GLUtil.getRealSize(vp, vRectangle, core.getVirtualWidth(), core.getVirtualHeight()).height / 2);
    }

    public enum AnimationState {
        OPENED, CLOSED, OPENING, CLOSING
    }
}
