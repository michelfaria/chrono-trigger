package io.michelfaria.chrono.logic;

import java.util.ArrayDeque;

public class MoveHistory {
	
	public static final int STANDARD_LIMIT = 20;
	
	private ArrayDeque<FloatPair> deque = new ArrayDeque<>();
	private float prevX;
	private float prevY;

	private int limit;

	public MoveHistory(float startX, float startY, int limit) {
		this.prevX = startX;
		this.prevY = startY;
		this.limit = limit;
	}

	public void add(float x, float y) {
		if (prevX != x || prevY != y) {
			deque.addFirst(new FloatPair(x, y));
			while (deque.size() > limit) {
				deque.removeLast();
			}
			prevX = x;
			prevY = y;
		}
	}

	public float getPrevX() {
		return prevX;
	}

	public float getPrevY() {
		return prevY;
	}

	public int size() {
		return deque.size();
	}

	public FloatPair getLast() {
		return deque.getLast();
	}
}
