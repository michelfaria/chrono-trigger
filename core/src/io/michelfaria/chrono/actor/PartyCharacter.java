package io.michelfaria.chrono.actor;

import static io.michelfaria.chrono.animation.AnimationType.IDLE_EAST;
import static io.michelfaria.chrono.animation.AnimationType.IDLE_NORTH;
import static io.michelfaria.chrono.animation.AnimationType.IDLE_SOUTH;
import static io.michelfaria.chrono.animation.AnimationType.IDLE_WEST;
import static io.michelfaria.chrono.animation.AnimationType.RUN_EAST;
import static io.michelfaria.chrono.animation.AnimationType.RUN_NORTH;
import static io.michelfaria.chrono.animation.AnimationType.RUN_SOUTH;
import static io.michelfaria.chrono.animation.AnimationType.RUN_WEST;
import static io.michelfaria.chrono.animation.AnimationType.WALK_EAST;
import static io.michelfaria.chrono.animation.AnimationType.WALK_NORTH;
import static io.michelfaria.chrono.animation.AnimationType.WALK_SOUTH;
import static io.michelfaria.chrono.animation.AnimationType.WALK_WEST;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.Positionable;
import io.michelfaria.chrono.animation.AnimationManager;
import io.michelfaria.chrono.controller.Buttons;
import io.michelfaria.chrono.controller.Ctrl;
import io.michelfaria.chrono.logic.CollisionChecker;
import io.michelfaria.chrono.logic.EntityMover;

public abstract class PartyCharacter extends Actor implements CollisionEntity, Positionable {

	protected Core core;

	protected Direction facing = Direction.SOUTH;
	protected float walkSpeed = 1f;
	protected float runSpeedMultiplier = 2f;
	protected boolean moving = false;
	protected boolean running = false;

	protected boolean handleInput = false;
	protected boolean enableCollision = true;

	protected AnimationManager aniMan;
	protected CollisionChecker colCheck;
	protected EntityMover entMover;

	// Runnables that run in the act() method
	public Array<Runnable> actionRunnables = new Array<>();

	/**
	 * Describes a generic Chrono Trigger playable/party member
	 */
	public PartyCharacter(Core core, TiledMap tiledMap) {
		this.core = core;
		this.aniMan = new AnimationManager(this);
		this.colCheck = new CollisionChecker(tiledMap);
		this.entMover = new EntityMover(this);
		setWidth(16);
		setHeight(16);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		aniMan.draw(batch, parentAlpha);
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
			entMover.moveBy(xMoveSpeed, yMoveSpeed);
		}
	}

	public void updateAnimations() {
		if (aniMan.anim == null) {
			aniMan.anim = IDLE_SOUTH;
		}
		if (running && moving && !core.getState().isHudPause()) {
			switch (facing) {
			case NORTH:
				aniMan.anim = RUN_NORTH;
				break;
			case SOUTH:
				aniMan.anim = RUN_SOUTH;
				break;
			case WEST:
				aniMan.anim = RUN_WEST;
				break;
			case EAST:
				aniMan.anim = RUN_EAST;
				break;
			}
		} else if (moving && !core.getState().isHudPause()) {
			assert !running;
			switch (facing) {
			case NORTH:
				aniMan.anim = WALK_NORTH;
				break;
			case SOUTH:
				aniMan.anim = WALK_SOUTH;
				break;
			case WEST:
				aniMan.anim = WALK_WEST;
				break;
			case EAST:
				aniMan.anim = WALK_EAST;
				break;
			}
		} else {
			switch (facing) {
			case NORTH:
				aniMan.anim = IDLE_NORTH;
				break;
			case SOUTH:
				aniMan.anim = IDLE_SOUTH;
				break;
			case WEST:
				aniMan.anim = IDLE_WEST;
				break;
			case EAST:
				aniMan.anim = IDLE_EAST;
				break;
			}
		}
	}

	public void setHandleInput(boolean handleInput) {
		this.handleInput = handleInput;
	}

	@Override
	public CollisionChecker getCollisionChecker() {
		return colCheck;
	}
}
