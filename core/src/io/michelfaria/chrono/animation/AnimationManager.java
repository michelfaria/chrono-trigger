package io.michelfaria.chrono.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.michelfaria.chrono.values.TextureRegionDescriptor.FlipData;

import java.util.HashMap;
import java.util.Map;

/**
 * Container for animation map and current playing animation.
 * Draws animation on coordinates.
 */
public class AnimationManager {

    public Map<AnimationType, AnimationData<TextureRegion>> animations = new HashMap<>();
    public AnimationType currentAnimation = null;

    public AnimationManager() {
    }

    /**
     * Draws the current animation at the X and Y location.
     */
    public void draw(Batch batch, float x, float y, float stateTime) {
        draw(batch, x, y, stateTime, 0);
    }

    /**
     * Draws the current animation at the X and Y location.
     */
    public void draw(Batch batch, float x, float y, float stateTime, float parentAlpha) {
        stateTime += Gdx.graphics.getDeltaTime();
        if (currentAnimation == null) {
            throw new IllegalStateException("No animation");
        }
        AnimationData<TextureRegion> animationData = animations.get(currentAnimation);
        int keyFrameIndex = animationData.animation.getKeyFrameIndex(stateTime);
        TextureRegion keyFrame = animationData.animation.getKeyFrames()[keyFrameIndex];

        configureFlipping(animationData, keyFrame, keyFrameIndex);
        batch.draw(keyFrame, x, y);
    }

    private void configureFlipping(AnimationData<TextureRegion> animationData,
                                   TextureRegion keyFrame, int keyFrameIndex) {
        FlipData flipData = animationData.textureRegionDescriptor.flipData;
        if (flipData == null) {
            keyFrame.flip(false, false);
        } else {
            setFlipValues(keyFrame, flipInstructionForFrame(keyFrameIndex, flipData));
        }
    }

    private void setFlipValues(TextureRegion keyFrame, byte flipInstruction) {
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

    private byte flipInstructionForFrame(int keyFrameIndex, FlipData flipData) {
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
}
