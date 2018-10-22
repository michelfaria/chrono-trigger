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
import io.michelfaria.chrono.logic.FloatPair;
import io.michelfaria.chrono.logic.Party;
import io.michelfaria.chrono.util.ActorUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;

import static io.michelfaria.chrono.animation.AnimationId.*;
import static io.michelfaria.chrono.controller.Ctrl.isButtonPressed;
import static io.michelfaria.chrono.logic.CollisionEntityMover.moveEntityBy;

public class PartyCharacter extends Actor implements CollisionEntity, EventListener {

    public static final int MOVE_HISTORY_LIMIT = 15;

    protected final AnimationManager animationManager;
    protected final EventDispatcher eventDispatcher;
    protected final CollisionContext collisionContext;

    protected final Party party;

    @Nullable
    protected PartyCharacter.InputHandler inputHandler;
    protected Direction facing = Direction.SOUTH;

    protected boolean paused;
    protected boolean moving;
    protected boolean running;

    protected float walkSpeed = 1f;
    protected float runSpeedMultiplier = 2f;
    protected float stateTime = 0f;

    protected ArrayDeque<FloatPair> moveHistory = new ArrayDeque<>();
    protected float prevX;
    protected float prevY;

    /**
     * Describes a generic Chrono Trigger playable/party member
     */
    public PartyCharacter(CollisionContext collisionContext, EventDispatcher eventDispatcher, Party party) {
        this.collisionContext = collisionContext;
        this.eventDispatcher = eventDispatcher;
        this.party = party;

        this.animationManager = new AnimationManager();
        this.animationManager.setCurrentAnimation(AnimationId.IDLE_SOUTH);
        prevX = getX();
        prevY = getY();
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
        if (inputHandler == null) {
            followNextPartyMember(delta);
        } else {
            handleInput(delta);
        }
        updateDirection();
        updateAnimation();
        updateMoveHistory();
    }

    protected void updateDirection() {
        if (getX() > prevX) {
            facing = Direction.EAST;
        } else if (getX() < prevX) {
            facing = Direction.WEST;
        }
        if (getY() > prevY) {
            facing = Direction.NORTH;
        } else if (getY() < prevY) {
            facing = Direction.SOUTH;
        }
    }

    protected void updateMoveHistory() {
        if (prevX != getX() || prevY != getY()) {
            moveHistory.addFirst(new FloatPair(getX(), getY()));
            while (moveHistory.size() > MOVE_HISTORY_LIMIT) {
                moveHistory.removeLast();
            }
            prevX = getX();
            prevY = getY();
        }
    }

    protected void handleInput(float delta) {
        if (inputHandler != null && !paused) {
            inputHandler.handleMovement();
        }
    }

    protected void followNextPartyMember(float delta) {
        int nextPartyMemberIndex = party.indexOf(this) - 1;
        assert nextPartyMemberIndex >= 0;
        PartyCharacter nextPartyMember = party.getCharacters().get(nextPartyMemberIndex);

        this.moving = nextPartyMember.moving;
        this.running = nextPartyMember.running;

        if (nextPartyMember.moveHistory.size() > 0) {
            FloatPair last = nextPartyMember.moveHistory.getLast();
            setX(last.a);
            setY(last.b);
        }
    }

    protected void updateAnimation() {
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
        return inputHandler != null;
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
            }
            if (isButtonPressed(0, Buttons.DPAD_RIGHT)) {
                moving = true;
                xMoveSpeed = walkSpeed;
            }
            if (isButtonPressed(0, Buttons.DPAD_UP)) {
                moving = true;
                yMoveSpeed = walkSpeed;
            }
            if (isButtonPressed(0, Buttons.DPAD_DOWN)) {
                moving = true;
                yMoveSpeed = -walkSpeed;
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
