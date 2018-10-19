package io.michelfaria.chrono.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

/**
 * Container for animation map and current playing animation.
 * Draws animation on coordinates.
 */
public class AnimationManager {

    public Map<AnimationType, Animation<?>> animations = new HashMap<>();
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
        TextureRegion currentFrame = (TextureRegion) animations.get(currentAnimation).getKeyFrame(stateTime);
        batch.draw(currentFrame, x , y);
    }
}
