package io.michelfaria.chrono.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import io.michelfaria.chrono.animation.AnimationId;
import io.michelfaria.chrono.animation.AnimationManager;
import io.michelfaria.chrono.controller.Buttons;
import io.michelfaria.chrono.controller.Ctrl;
import io.michelfaria.chrono.events.*;
import io.michelfaria.chrono.interfaces.CollisionEntity;
import io.michelfaria.chrono.interfaces.Interactible;
import io.michelfaria.chrono.logic.CollisionContext;
import io.michelfaria.chrono.logic.CollisionEntityMover;
import io.michelfaria.chrono.util.ActorUtil;

import static io.michelfaria.chrono.animation.AnimationId.*;

public class PartyCharacter extends Actor implements CollisionEntity, EventListener {

    protected final AnimationManager animationManager;
    protected final EventDispatcher eventDispatcher;
    protected final CollisionContext collisionContext;

    protected Direction facing = Direction.SOUTH;

    protected boolean paused;

    protected boolean moving;
    protected boolean running;
    protected boolean handleInput;
    protected boolean isCollisionEnabled = true;

    protected float walkSpeed = 1f;
    protected float runSpeedMultiplier = 2f;
    protected float stateTime = 0f;


    /**
     * Describes a generic Chrono Trigger playable/party member
     */
    public PartyCharacter(CollisionContext collisionContext, EventDispatcher eventDispatcher) {
        this.collisionContext = collisionContext;
        this.eventDispatcher = eventDispatcher;
        this.animationManager = new AnimationManager();
        setWidth(16);
        setHeight(16);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        animationManager.draw(batch, getX(), getY(), stateTime);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        updateAnimations();
    }

    protected void handleInput(float delta) {
        assert handleInput;
        if (!paused) {
            handleMovementInput(delta);
        }
    }

    protected void handleMovementInput(float delta) {
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
            CollisionEntityMover.moveEntityBy(this, xMoveSpeed, yMoveSpeed);
        }
    }

    public void updateAnimations() {
        if (animationManager.getCurrentAnimation() == null) {
            animationManager.setCurrentAnimation(IDLE_SOUTH);
        }
        if (running && moving && !paused) {
            updateRunningAnimation(RUN_NORTH, RUN_SOUTH, RUN_WEST, RUN_EAST);
        } else if (moving && !paused) {
            assert !running;
            updateRunningAnimation(WALK_NORTH, WALK_SOUTH, WALK_WEST, WALK_EAST);
        } else {
            updateRunningAnimation(IDLE_NORTH, IDLE_SOUTH, IDLE_WEST, IDLE_EAST);
        }
    }

    private void updateRunningAnimation(AnimationId north, AnimationId south,
                                        AnimationId west, AnimationId east) {
        if (facing == Direction.NORTH) {
            animationManager.setCurrentAnimation(north);

        } else if (facing == Direction.SOUTH) {
            animationManager.setCurrentAnimation(south);

        } else if (facing == Direction.WEST) {
            animationManager.setCurrentAnimation(west);

        } else if (facing == Direction.EAST) {
            animationManager.setCurrentAnimation(east);

        }
    }

    protected void handleAPress(ButtonEvent event) {
        if (event.getButton() == Buttons.A && event.getEventType() == ButtonEventType.PRESS) {
            if (!paused) {
                emitInteraction();
            }
        }
    }

    protected void emitInteraction() {
        Rectangle interactionRegion = ActorUtil.getActorRectangle(this);
        if (facing == Direction.NORTH) {
            interactionRegion.y += getHeight() / 2;

        } else if (facing == Direction.SOUTH) {
            interactionRegion.y -= getHeight() / 2;

        } else if (facing == Direction.WEST) {
            interactionRegion.x -= getWidth() / 2;

        } else if (facing == Direction.EAST) {
            interactionRegion.x += getWidth() / 2;
        }
        interactWithRegion(interactionRegion);
    }

    private void interactWithRegion(Rectangle region) {
        Array<CollisionEntity> collisionEntities
                = collisionContext.collisionChecker.entityCollisions(this, region);
        for (CollisionEntity collisionEntity : collisionEntities) {
            if (collisionEntity instanceof Interactible) {
                Interactible interactible = (Interactible) collisionEntity;
                interactible.interact();
                break;
            }
        }
    }

    public void setHandleInput(boolean handleInput) {
        this.handleInput = handleInput;
    }

    @Override
    public boolean handleEvent(Event event) {
        if (event instanceof ButtonEvent) {
            if (((ButtonEvent) event).getButton() == Buttons.A) {
                handleAPress((ButtonEvent) event);
                return true;
            }
        }
        if (event instanceof HudPauseEvent) {
            paused = ((HudPauseEvent) event).isPaused();
        }
        return false;
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
