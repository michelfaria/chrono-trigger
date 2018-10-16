package io.michelfaria.chrono.actor;

import static io.michelfaria.chrono.animation.AnimationType.*;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.animation.AnimationType;
import io.michelfaria.chrono.controller.Buttons;
import io.michelfaria.chrono.controller.Ctrl;
import io.michelfaria.chrono.logic.CollisionChecker;
import io.michelfaria.chrono.logic.EntityMover;

public abstract class PartyCharacter extends Actor implements CollisionEntity {

	public Core core;

	public Map<AnimationType, Animation<?>> animations = new HashMap<>();
	public AnimationType animation;

	public Direction facing = Direction.SOUTH;
	public float walkSpeed = 1f;
	public float runSpeedMultiplier = 2f;
	public boolean moving = false;
	public boolean running = false;
	public float stateTime = 0f;

	public boolean handleInput = false;
	public boolean enableCollision = true;

	public CollisionChecker collisionChecker;
	public EntityMover entityMover;

	// Runnables that run in the act() method
	public Array<Runnable> actionRunnables = new Array<>();

	/**
	 * Describes a generic Chrono Trigger playable/party member
	 */
	public PartyCharacter(Core core, TiledMap tiledMap) {
		this.core = core;
		collisionChecker = new CollisionChecker(tiledMap);
		entityMover = new EntityMover(this);
		setWidth(16);
		setHeight(16);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		stateTime += Gdx.graphics.getDeltaTime();

		if (animation == null) {
			throw new IllegalStateException("No animation");
			
		}
		TextureRegion currentFrame = (TextureRegion) animations.get(animation).getKeyFrame(stateTime);

		batch.draw(currentFrame, getX(), getY());
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		for (Runnable runnable : actionRunnables) {
			runnable.run();
			
		}
		if (handleInput) {
			handleInput(delta);
			
		}
		updateAnimations();
	}

	public void handleInput(float delta) {
		assert handleInput;

		if (!core.getState().isHudPause()) {
			handleMovingInput(delta);
			
		}
	}

	private void handleMovingInput(float delta) {
		float xMoveSpeed = 0;
		float yMoveSpeed = 0;

		if (Ctrl.isButtonPressed(0, Buttons.DPAD_LEFT)) {
			moving = true;
			xMoveSpeed = -walkSpeed;
			facing = Direction.WEST;
			
		}
		if (Ctrl.isButtonPressed(0, Buttons.DPAD_RIGHT)) {
			moving = true;
			xMoveSpeed = walkSpeed;
			facing = Direction.EAST;
			
		}
		if (Ctrl.isButtonPressed(0, Buttons.DPAD_UP)) {
			moving = true;
			yMoveSpeed = walkSpeed;
			facing = Direction.NORTH;
			
		}
		if (Ctrl.isButtonPressed(0, Buttons.DPAD_DOWN)) {
			moving = true;
			yMoveSpeed = -walkSpeed;
			facing = Direction.SOUTH;
			
		}
		running = Ctrl.isButtonPressed(0, Buttons.B);
		
		if (running) {
			xMoveSpeed *= runSpeedMultiplier;
			yMoveSpeed *= runSpeedMultiplier;
			
		}
		if (xMoveSpeed == 0 && yMoveSpeed == 0) {
			moving = false;
			
		} else {
			entityMover.moveBy(xMoveSpeed, yMoveSpeed);
			
		}
	}

	public void updateAnimations() {
		if (animation == null) {
			animation = IDLE_SOUTH;
			
		}
		if (running && moving && !core.getState().isHudPause()) {
			switch (facing) {
			case NORTH:
				animation = RUN_NORTH;
				break;
			case SOUTH:
				animation = RUN_SOUTH;
				break;
			case WEST:
				animation = RUN_WEST;
				break;
			case EAST:
				animation = RUN_EAST;
				break;
			}
		} else if (moving && !core.getState().isHudPause()) {
			assert !running;
			switch (facing) {
			case NORTH:
				animation = WALK_NORTH;
				break;
			case SOUTH:
				animation = WALK_SOUTH;
				break;
			case WEST:
				animation = WALK_WEST;
				break;
			case EAST:
				animation = WALK_EAST;
				break;
			}
		} else {
			switch (facing) {
			case NORTH:
				animation = IDLE_NORTH;
				break;
			case SOUTH:
				animation = IDLE_SOUTH;
				break;
			case WEST:
				animation = IDLE_WEST;
				break;
			case EAST:
				animation = IDLE_EAST;
				break;
			}
		}
	}

	public void setHandleInput(boolean handleInput) {
		this.handleInput = handleInput;
	}

	@Override
	public CollisionChecker getCollisionChecker() {
		return collisionChecker;
	}
}
