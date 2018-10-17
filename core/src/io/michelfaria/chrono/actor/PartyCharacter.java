package io.michelfaria.chrono.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.animation.AnimationManager;
import io.michelfaria.chrono.animation.AnimationType;
import io.michelfaria.chrono.controller.Buttons;
import io.michelfaria.chrono.controller.Ctrl;
import io.michelfaria.chrono.logic.CollisionContext;
import io.michelfaria.chrono.logic.CollisionEntityMover;

import static io.michelfaria.chrono.animation.AnimationType.*;

public class PartyCharacter extends Actor implements CollisionEntity, Positionable {

    protected Core core;

    // Runnables that run in the act() method
    public Array<Runnable> actionRunnables = new Array<>();

    protected Direction facing = Direction.SOUTH;
    protected boolean moving;
    protected boolean running;
    protected boolean handleInput;
    protected boolean isCollisionEnabled = true;

    protected float walkSpeed = 1f;
    protected float runSpeedMultiplier = 2f;

    protected AnimationManager aniMan;
    protected CollisionContext collisionContext;
    protected CollisionEntityMover entMover;

    /**
     * Describes a generic Chrono Trigger playable/party member
     */
    public PartyCharacter(Core core, CollisionContext collisionContext) {
        this.core = core;
        this.collisionContext = collisionContext;

        aniMan = new AnimationManager(this);
        entMover = new CollisionEntityMover(this);

        setWidth(16);
        setHeight(16);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
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

    protected void handleInput(float delta) {
        assert handleInput;
        if (!core.getState().isHudPause()) {
            handleMovingInput(delta);
        }
    }

    protected void handleMovingInput(float delta) {
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
            updateRunningAnimation(RUN_NORTH, RUN_SOUTH, RUN_WEST, RUN_EAST);

        } else if (moving && !core.getState().isHudPause()) {
            assert !running;
            updateRunningAnimation(WALK_NORTH, WALK_SOUTH, WALK_WEST, WALK_EAST);

        } else {
            updateRunningAnimation(IDLE_NORTH, IDLE_SOUTH, IDLE_WEST, IDLE_EAST);
        }
    }

    private void updateRunningAnimation(AnimationType north, AnimationType south, AnimationType west, AnimationType east) {
        switch (facing) {
            case NORTH:
                aniMan.anim = north;
                break;
            case SOUTH:
                aniMan.anim = south;
                break;
            case WEST:
                aniMan.anim = west;
                break;
            case EAST:
                aniMan.anim = east;
                break;
        }
    }

    public void setHandleInput(boolean handleInput) {
        this.handleInput = handleInput;
    }

    @Override
    public CollisionContext getCollisionContext() {
        return collisionContext;
    }

    @Override
    public boolean isCollisionEnabled() {
        return isCollisionEnabled;
    }
}
