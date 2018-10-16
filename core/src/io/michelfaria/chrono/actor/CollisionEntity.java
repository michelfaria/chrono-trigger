package io.michelfaria.chrono.actor;

import io.michelfaria.chrono.logic.CollisionChecker;

public interface CollisionEntity {

	CollisionChecker getCollisionChecker();

	float getX();
	
	float getY();
	
	float getWidth();
	
	float getHeight();
	
	void setX(float x);
	
	void setY(float y);
}