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

    protected Direction facing = Direction.SOUTH;

    protected boolean handleInput;
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
        if (handleInput) {
            handleInput(delta);
        } else {
            followNextPartyMember(delta);
            updateFollowerStatus();
        }
        updateAnimation();
        updateMoveHistory();
    }

    protected void handleInput(float delta) {
        assert isHandleInput();
        if (!paused) {
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
    }

    protected void followNextPartyMember(float delta) {
        PartyCharacter nextPartyMember = getNextPartyMember();
        if (nextPartyMember.moveHistory.size() > 0) {
            FloatPair last = nextPartyMember.moveHistory.getLast();
            setX(last.a);
            setY(last.b);
        }
    }

    protected void updateFollowerStatus() {
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
        if (getX() != prevX && getY() != prevX) {
            this.moving = true;
            this.running = getNextPartyMember().running;
        } else {
            this.moving = false;
            this.running = false;
        }
    }

    protected void updateAnimation() {
        if (animationManager.getCurrentAnimation() == null) {
            animationManager.setCurrentAnimation(IDLE_SOUTH);
        }
        if (running && moving && !paused) {
            updateAnimationWith(RUN_NORTH, RUN_SOUTH, RUN_WEST, RUN_EAST);
        } else if (moving && !paused) {
            updateAnimationWith(WALK_NORTH, WALK_SOUTH, WALK_WEST, WALK_EAST);
        } else {
            updateAnimationWith(IDLE_NORTH, IDLE_SOUTH, IDLE_WEST, IDLE_EAST);
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

    public boolean isHandleInput() {
        return handleInput;
    }

    private @Nullable PartyCharacter getNextPartyMember() {
        if (party.getCharacters().size < 2) {
            return null;
        }
        return party.getCharacters().get(party.indexOf(this) - 1);
    }

    private void updateAnimationWith(AnimationId north, AnimationId south, AnimationId west, AnimationId east) {
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

    public void setHandleInput(boolean handleInput) {
        this.handleInput = handleInput;
    }

    @Override
    public boolean handleEvent(Event event) {
        if (event instanceof ButtonEvent) {
            ButtonEvent be = (ButtonEvent) event;
            if (be.getButton() == Buttons.A) {
                // A-Button pressed
                if (isHandleInput()) {
                    handleAPress(be);
                }
                return true;
            }
        }
        if (event instanceof HudPauseEvent) {
            paused = ((HudPauseEvent) event).isPaused();
        }
        return false;
    }

    protected void handleAPress(ButtonEvent event) {
        assert isHandleInput();
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

    protected void interactWithRegion(Rectangle region) {
        Array<CollisionEntity> arr = collisionContext.collisionChecker.entityCollisions(this, region);
        for (CollisionEntity ce : arr) {
            if (ce instanceof Interactible) {
                Interactible in = (Interactible) ce;
                in.interact();
                break;
            }
        }
    }

    protected final boolean isPartyLeader() {
        return party.getLeader() == this;
    }

    @Override
    public CollisionContext getCollisionContext() {
        return collisionContext;
    }

    @Override
    public boolean isCollisionEnabled() {
        return isHandleInput();
    }
}
