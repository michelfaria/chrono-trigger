package io.michelfaria.chrono.animation;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.michelfaria.chrono.Positionable;

/**
 * Container for animation map and current playing animation.
 * Draws animation on Positionable's coordinates.
 */
public class AnimationManager {
	
	/**
	 * Animations
	 */
	public Map<AnimationType, Animation<?>> anims = new HashMap<>();
	/**
	 * Current playing animation
	 */
	public AnimationType anim = null;
	
	private Positionable pos;
	
	private float stateTime = 0f;
		
	/**
	 * @param pos Positionable entity to animate
	 */
	public AnimationManager(Positionable pos) {
		this.pos = pos;
	}

	/**
	 * Draws the current animation on the Positionable's X and Y location.
	 */
	public void draw(Batch batch, float parentAlpha) {
		stateTime += Gdx.graphics.getDeltaTime();
		if (anim == null) {
			throw new IllegalStateException("No animation");
		}
		TextureRegion currentFrame = (TextureRegion) anims.get(anim).getKeyFrame(stateTime);
		batch.draw(currentFrame, pos.getX(), pos.getY());
	}
}
