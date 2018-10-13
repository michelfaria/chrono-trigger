package io.michelfaria.chrono.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.util.GLUtil;
import io.michelfaria.chrono.util.GroupUtil;

import static io.michelfaria.chrono.animation.ChronoOpenClosingAnimator.AnimationState.*;

public class ChronoOpenClosingAnimator {

    public enum AnimationState {
        OPENED, CLOSED, OPENING, CLOSING
    }

    public Group group;
    public Viewport vp;

    private static final int SCISSOR_MULTIPLE = 12;
    private int scissor;
    private AnimationState spriteState;

    public ChronoOpenClosingAnimator(Group group, Viewport vp) {
        this.group = group;
        this.vp = vp;

        spriteState = AnimationState.CLOSED;
    }

    public void draw() {
        updateGroupSize();

        // Update the view port
        Gdx.gl.glViewport(0, 0, vp.getScreenWidth(), vp.getScreenHeight());

        if (spriteState.equals(OPENING)) {
            /*
            Open dialog box
             */
            if (scissor < GLUtil.getRealSize(vp, group).y / 2) {
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
            Core.batch.begin();
            /*
            Draw dialog box
             */
            if (group == null) throw new IllegalStateException("No group set");
            group.draw(Core.batch, 1);

            Core.batch.end();
        }

        if (isAnimating) {
            Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
        }
    }

    private void updateGroupSize() {
        group.setWidth(GroupUtil.getWidth(group));
        group.setHeight(GroupUtil.getHeight(group));
    }

    public void open() {
        spriteState = OPENING;
        scissor = 0;
    }

    public void close() {
        spriteState = CLOSING;
        scissor = (int) GLUtil.getRealSize(vp, group).y / 2;
    }

    /**
     * Returns the real (non-virtual) Y coordinate of the center of the group.
     */
    private int getRealYCenter() {
        return (int) (GLUtil.getRealSize(vp, group).y / 2);
    }
}
