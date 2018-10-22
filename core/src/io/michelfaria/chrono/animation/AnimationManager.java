package io.michelfaria.chrono.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.michelfaria.chrono.data.TRD.FlipData;

import java.util.HashMap;
import java.util.Map;

/**
 * Container for animation map and current playing animation.
 * Draws animation on coordinates.
 */
public class AnimationManager {

    private final Map<AnimationId, AnimationData<TextureRegion>> animations = new HashMap<>();

    private AnimationId currentAnimation = null;
    private float stateTime = 0;

    /**
     * Draws the current animation at the X and Y location.
     */
    public void draw(Batch batch, float x, float y) {
        draw(batch, x, y, 0);
    }

    /**
     * Draws the current animation at the X and Y location.
     */
    public void draw(Batch batch, float x, float y, float parentAlpha) {
        stateTime += Gdx.graphics.getDeltaTime();
        if (getCurrentAnimation() == null) {
            throw new IllegalStateException("No animation");
        }

        AnimationData<TextureRegion> animationData = animations.get(getCurrentAnimation());
        int keyFrameIndex = animationData.animation.getKeyFrameIndex(stateTime);
        TextureRegion keyFrame = animationData.animation.getKeyFrames()[keyFrameIndex];
        FlipData flipData = null;
        if (animationData.trd != null) {
            flipData = animationData.trd.flipData;
        }

        if (flipData == null) {
            keyFrame.flip(false, false);
        } else {
            flipKeyFrame(keyFrame, getFlipInstructionForKeyFrameIndex(keyFrameIndex, flipData));
        }
        batch.draw(keyFrame, x, y);
    }

    private void flipKeyFrame(TextureRegion keyFrame, byte flipInstruction) {
        boolean flipX = false;
        boolean flipY = false;

        switch (flipInstruction) {
            case FlipData.FLIP_HORZ:
                flipX = true;
                break;
            case FlipData.FLIP_VERT:
                flipY = true;
                break;
            case FlipData.FLIP_BOTH:
                flipX = true;
                flipY = true;
                break;
            case FlipData.FLIP_NONE:
                break;
            default:
                throw new IllegalStateException("Unknown byte");
        }
        keyFrame.flip(!keyFrame.isFlipX() && flipX, !keyFrame.isFlipY() && flipY);
    }

    private byte getFlipInstructionForKeyFrameIndex(int keyFrameIndex, FlipData flipData) {
        int flipIndex = 0;
        for (int i = 0; i < flipData.indexes.length; i++) {
            if (flipData.indexes[i] <= keyFrameIndex) {
                flipIndex = i;
            }
            if (flipData.indexes[i] > keyFrameIndex) {
                break;
            }
        }
        return flipData.flip[flipIndex];
    }

    public Map<AnimationId, AnimationData<TextureRegion>> getAnimations() {
        return animations;
    }

    public AnimationId getCurrentAnimation() {
        return currentAnimation;
    }

    public void setCurrentAnimation(AnimationId currentAnimation) {
        this.currentAnimation = currentAnimation;
    }
}
