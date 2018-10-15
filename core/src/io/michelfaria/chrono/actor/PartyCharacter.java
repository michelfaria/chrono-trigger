package io.michelfaria.chrono.actor;

import static io.michelfaria.chrono.util.ActorUtil.getActorRectangle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.controller.Buttons;
import io.michelfaria.chrono.controller.Ctrl;
import io.michelfaria.chrono.logic.CollisionChecker;

public abstract class PartyCharacter extends Actor {

	protected Core core;

	protected float walkSpeed = 1f;
	protected float runSpeedMultiplier = 2f;

	protected Animation<?> idleNorth;
	protected Animation<?> idleSouth;
	protected Animation<?> idleWest;
	protected Animation<?> idleEast;
	protected Animation<?> walkNorth;
	protected Animation<?> walkSouth;
	protected Animation<?> walkWest;
	protected Animation<?> walkEast;
	protected Animation<?> runNorth;
	protected Animation<?> runSouth;
	protected Animation<?> runWest;
	protected Animation<?> runEast;

	protected Direction facing = Direction.SOUTH;
	protected boolean moving = false;
	protected boolean running = false;
	protected float stateTime = 0f;
	protected Animation<?> animation = null;
	protected boolean handleInput = false;
	
	protected CollisionChecker collisionChecker;
	protected boolean enableCollision = true;

	// Runnables that run in the act() method
	protected Array<Runnable> actionRunnables = new Array<>();

	/**
	 * Describes a generic Chrono Trigger playable/party member
	 */
	public PartyCharacter(Core core, TiledMap tiledMap) {
		this.core = core;
		collisionChecker = new CollisionChecker(tiledMap);
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

	protected void handleInput(float delta) {
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
			moveCharacter(xMoveSpeed, yMoveSpeed);
		}
	}

	/**
	 * Moves the character by the specified numbers.
	 * If the character moved successfully, it will return true.
	 * 
	 * @param xMoveSpeed
	 * @param yMoveSpeed
	 * @return
	 */
	private boolean moveCharacter(float xMoveSpeed, float yMoveSpeed) {
		Rectangle futureX = new Rectangle(getX() + xMoveSpeed, getY(), getWidth(), getHeight());
		if (collisionChecker.collidesWithMap(futureX)) {
			xMoveSpeed = 0;
		}
		Rectangle futureY = new Rectangle(getX(), getY() + yMoveSpeed, getWidth(), getHeight());
		if (collisionChecker.collidesWithMap(futureY)) {
			yMoveSpeed = 0;
		}
		if (xMoveSpeed != 0 || yMoveSpeed != 0) {
			addAction(Actions.moveBy(xMoveSpeed, yMoveSpeed));
			return true;
		}
		return false;
	}

	protected void updateAnimations() {
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
}
