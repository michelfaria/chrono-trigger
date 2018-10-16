package io.michelfaria.chrono.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.controller.Buttons;
import io.michelfaria.chrono.controller.Ctrl;
import io.michelfaria.chrono.logic.CollisionChecker;
import io.michelfaria.chrono.logic.EntityMover;

public abstract class PartyCharacter extends Actor implements CollisionEntity {
	
	public Core core;

	public float walkSpeed = 1f;
	public float runSpeedMultiplier = 2f;

	public Animation<?> idleNorth;
	public Animation<?> idleSouth;
	public Animation<?> idleWest;
	public Animation<?> idleEast;
	public Animation<?> walkNorth;
	public Animation<?> walkSouth;
	public Animation<?> walkWest;
	public Animation<?> walkEast;
	public Animation<?> runNorth;
	public Animation<?> runSouth;
	public Animation<?> runWest;
	public Animation<?> runEast;

	public Direction facing = Direction.SOUTH;
	public boolean moving = false;
	public boolean running = false;
	public float stateTime = 0f;
	public Animation<?> animation = null;
	public boolean handleInput = false;
	
	public CollisionChecker collisionChecker;
	public boolean enableCollision = true;
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
		TextureRegion currentFrame = (TextureRegion) animation.getKeyFrame(stateTime);

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
			animation = idleSouth;
		}
		if (running && moving && !core.getState().isHudPause()) {
			// Running
			if (facing == Direction.NORTH) {
				animation = runNorth;
			} else if (facing == Direction.SOUTH) {
				animation = runSouth;
			} else if (facing == Direction.WEST) {
				animation = runWest;

			} else if (facing == Direction.EAST) {
				animation = runEast;

			}
		} else if (moving && !core.getState().isHudPause()) {
			assert !running;
			// Walking
			if (facing == Direction.NORTH) {
				animation = walkNorth;

			} else if (facing == Direction.SOUTH) {
				animation = walkSouth;

			} else if (facing == Direction.WEST) {
				animation = walkWest;

			} else if (facing == Direction.EAST) {
				animation = walkEast;
			}
		} else {
			// Standing still
			if (facing == Direction.NORTH) {
				animation = idleNorth;

			} else if (facing == Direction.SOUTH) {
				animation = idleSouth;

			} else if (facing == Direction.WEST) {
				animation = idleWest;

			} else if (facing == Direction.EAST) {
				animation = idleEast;
			}
		}
	}

	public void setHandleInput(boolean handleInput) {
		this.handleInput = handleInput;
	}

	/* (non-Javadoc)
	 * @see io.michelfaria.chrono.actor.CollisionEntity#getCollisionChecker()
	 */
	@Override
	public CollisionChecker getCollisionChecker() {
		return collisionChecker;
	}
}
