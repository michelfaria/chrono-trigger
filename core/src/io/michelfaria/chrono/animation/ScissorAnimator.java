package io.michelfaria.chrono.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.util.GLUtil;

import static io.michelfaria.chrono.animation.ScissorAnimator.AnimationState.*;

/**
 * Creates a scissoring animation for a Scene2D group.
 */
public class ScissorAnimator {

    private static final int SCISSOR_MULTIPLE = 12;
    public Rectangle rectangle;
    public Viewport vp;
    private int scissor;
    private AnimationState spriteState;
    public ScissorAnimator(Rectangle rectangle, Viewport vp) {
        this.rectangle = rectangle;
        this.vp = vp;

        spriteState = AnimationState.CLOSED;
    }

    public void scissor(Runnable draw) {
        // Update the view port
        Gdx.gl.glViewport(0, 0, vp.getScreenWidth(), vp.getScreenHeight());

        if (spriteState.equals(OPENING)) {
            /*
            Open dialog box
             */
            if (scissor < GLUtil.getRealSize(vp, rectangle).height / 2) {
                scissor += SCISSOR_MULTIPLE;
            } else {
                // Scissor has covered the sprite
                spriteState = AnimationState.OPENED;
            }
        } else if (spriteState.equals(CLOSING)) {
            /*
            Close dialog box
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
        scissor = (int) GLUtil.getRealSize(vp, rectangle).height / 2;
    }

    /**
     * Returns the real (non-virtual) Y coordinate of the center of the group.
     */
    private int getRealYCenter() {
        return (int) (GLUtil.getRealSize(vp, rectangle).height / 2);
    }

    public enum AnimationState {
        OPENED, CLOSED, OPENING, CLOSING
    }
}
