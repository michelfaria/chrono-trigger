package io.michelfaria.chrono.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import io.michelfaria.chrono.animation.AnimationId;
import io.michelfaria.chrono.animation.AnimationManager;
import io.michelfaria.chrono.controller.Buttons;
import io.michelfaria.chrono.events.*;
import io.michelfaria.chrono.interfaces.CollisionEntity;
import io.michelfaria.chrono.interfaces.Interactible;
import io.michelfaria.chrono.logic.CollisionContext;
import io.michelfaria.chrono.util.ActorUtil;
import org.jetbrains.annotations.Nullable;

import static io.michelfaria.chrono.animation.AnimationId.*;
import static io.michelfaria.chrono.controller.Ctrl.isButtonPressed;
import static io.michelfaria.chrono.logic.CollisionEntityMover.moveEntityBy;

public class PartyCharacter extends Actor implements CollisionEntity, EventListener {

    protected final AnimationManager animationManager;
    protected final EventDispatcher eventDispatcher;
    protected final CollisionContext collisionContext;

    @Nullable
    protected PartyCharacter.InputHandler inputHandler;
    protected Direction facing = Direction.SOUTH;

    protected boolean paused;
    protected boolean moving;
    protected boolean running;
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

        this.animationManager.setCurrentAnimation(AnimationId.IDLE_SOUTH);

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
        handleInput(delta);
    }

    protected void handleInput(float delta) {
        if (inputHandler != null) {
            inputHandler.handleMovement();
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

    @Override
    public boolean handleEvent(Event event) {
        if (event instanceof ButtonEvent) {
            ButtonEvent buttonEvent = (ButtonEvent) event;
            if (buttonEvent.getButton() == Buttons.A) {
                // A-Button pressed
                if (inputHandler != null) {
                    inputHandler.handleAPress(buttonEvent);
                }
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

    @Nullable
    public PartyCharacter.InputHandler getInputHandler() {
        return inputHandler;
    }

    public void setInputHandler(@Nullable PartyCharacter.InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    public class InputHandler {
        public void handleMovement() {
            float xMoveSpeed = 0;
            float yMoveSpeed = 0;

            if (isButtonPressed(0, Buttons.DPAD_LEFT)) {
                moving = true;
                xMoveSpeed = -walkSpeed;
                facing = Direction.WEST;
            }
            if (isButtonPressed(0, Buttons.DPAD_RIGHT)) {
                moving = true;
                xMoveSpeed = walkSpeed;
                facing = Direction.EAST;
            }
            if (isButtonPressed(0, Buttons.DPAD_UP)) {
                moving = true;
                yMoveSpeed = walkSpeed;
                facing = Direction.NORTH;
            }
            if (isButtonPressed(0, Buttons.DPAD_DOWN)) {
                moving = true;
                yMoveSpeed = -walkSpeed;
                facing = Direction.SOUTH;
            }
            running = isButtonPressed(0, Buttons.B);

            if (running) {
                xMoveSpeed *= runSpeedMultiplier;
                yMoveSpeed *= runSpeedMultiplier;
            }
            if (xMoveSpeed == 0 && yMoveSpeed == 0) {
                moving = false;
            } else {
                moveEntityBy(PartyCharacter.this, xMoveSpeed, yMoveSpeed);
            }
        }

        public void handleAPress(ButtonEvent event) {
            if (event.getButton() == Buttons.A && event.getEventType() == ButtonEventType.PRESS) {
                if (!paused) {
                    emitInteraction();
                }
            }
        }
    }
}
