package io.michelfaria.chrono.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

import java.lang.reflect.Field;
import java.util.NavigableMap;
import java.util.TreeMap;

public class ExtendedAnimation<T> extends Animation<T> {

	private final NavigableMap<Float, Integer> durationFrameMap;
	private final float totalTime;

	// You know what? It would be fucking **wonderful** if Animation was an
	// interface.
	// Then I wouldn't have to do this reflection crap.
	private final Field lastFrameNumberField;
	private final Field lastStateTimeField;

	{
		try {
			lastFrameNumberField = Animation.class.getDeclaredField("lastFrameNumber");
			lastStateTimeField = Animation.class.getDeclaredField("lastStateTime");
			lastFrameNumberField.setAccessible(true);
			lastStateTimeField.setAccessible(true);
		} catch (NoSuchFieldException ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	public ExtendedAnimation(Array<T> keyFrames, PlayMode playMode, float[] frameDurations) {
		super(0f, keyFrames);
		setPlayMode(playMode);
		if (frameDurations.length != keyFrames.size) {
			throw new IllegalArgumentException(
					"Key frame durations array length does not match key frame array length");
		}

		durationFrameMap = new TreeMap<Float, Integer>();
		float sum = 0;
		for (int i = 0; i < frameDurations.length; i++) {
			durationFrameMap.put(sum, i);
			sum += frameDurations[i];
		}

		// Calculate the total duration of the animation
		sum = 0;
		for (Float e : frameDurations) {
			sum += e;
		}
		totalTime = sum;
	}

	@Override
	public int getKeyFrameIndex(float stateTime) {
		if (super.getKeyFrames().length == 1) {
			return 0;
		}
		int frameNumber;
		switch (getPlayMode()) {
		case NORMAL:
			frameNumber = stateTime > totalTime ? getKeyFrames().length - 1 // Last keyframe
					: durationFrameMap.get(durationFrameMap.floorKey(stateTime));
			break;
		case LOOP:
			frameNumber = durationFrameMap.get(durationFrameMap.floorKey(stateTime % totalTime));
			break;
		default:
			throw new RuntimeException("Not implemented: " + getPlayMode());
		}

		try {
			lastFrameNumberField.set(this, frameNumber);
			lastStateTimeField.set(this, stateTime);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}

		return frameNumber;
	}
}
