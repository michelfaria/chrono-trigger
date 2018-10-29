/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:38 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.actor;

import static io.michelfaria.chrono.animation.AnimationId.IDLE_EAST;
import static io.michelfaria.chrono.animation.AnimationId.IDLE_NORTH;
import static io.michelfaria.chrono.animation.AnimationId.IDLE_SOUTH;
import static io.michelfaria.chrono.animation.AnimationId.IDLE_WEST;
import static io.michelfaria.chrono.animation.AnimationId.RUN_EAST;
import static io.michelfaria.chrono.animation.AnimationId.RUN_NORTH;
import static io.michelfaria.chrono.animation.AnimationId.RUN_SOUTH;
import static io.michelfaria.chrono.animation.AnimationId.RUN_WEST;
import static io.michelfaria.chrono.animation.AnimationId.WALK_EAST;
import static io.michelfaria.chrono.animation.AnimationId.WALK_NORTH;
import static io.michelfaria.chrono.animation.AnimationId.WALK_SOUTH;
import static io.michelfaria.chrono.animation.AnimationId.WALK_WEST;
import static io.michelfaria.chrono.controller.Ctrl.isButtonPressed;
import static io.michelfaria.chrono.logic.CollisionEntityMover.moveCollisionEntityBy;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import io.michelfaria.chrono.items.WoodenSword;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import io.michelfaria.chrono.animation.AnimationId;
import io.michelfaria.chrono.animation.AnimationManager;
import io.michelfaria.chrono.controller.Buttons;
import io.michelfaria.chrono.events.ButtonEvent;
import io.michelfaria.chrono.events.ButtonEventType;
import io.michelfaria.chrono.events.Event;
import io.michelfaria.chrono.events.EventDispatcher;
import io.michelfaria.chrono.events.EventListener;
import io.michelfaria.chrono.events.HudPauseEvent;
import io.michelfaria.chrono.interfaces.CollisionEntity;
import io.michelfaria.chrono.interfaces.Combatant;
import io.michelfaria.chrono.interfaces.Interactible;
import io.michelfaria.chrono.interfaces.Weapon;
import io.michelfaria.chrono.logic.CollisionContext;
import io.michelfaria.chrono.logic.CombatStats;
import io.michelfaria.chrono.logic.FloatPair;
import io.michelfaria.chrono.logic.MoveHistory;
import io.michelfaria.chrono.logic.Party;
import io.michelfaria.chrono.util.ActorUtil;

import java.util.Objects;

/**
 * This class represents a Party character from Chrono Trigger.
 * <p>
 * A party character:
 * <p>
 * 1. Has a set of mandatory animations that all subclasses must provide. Currently, these are the idle, isMoving and
 * isRunning animations in all four cardinal directions.
 * <p>
 * 2. Can be either controllable or a follower. When "isMainChar" is true, then this character will be controllable by
 * the player. If the character is a follower and is the only member in the party, the class will throw a
 * NullPointerException. This is fail-fast behavior.
 * <p>
 * 3. Should never be without a "weapon", as it is a requirement to calculate damage.
 */
public abstract class PartyCharacter extends Actor implements CollisionEntity, EventListener, Combatant {

    @NotNull
    protected final AnimationManager animationManager;
    @NotNull
    protected final EventDispatcher eventDispatcher;
    @NotNull
    protected final CollisionContext collisionContext;
    @NotNull
    protected final Party party;

    protected Direction facing = Direction.SOUTH;

    protected boolean isMainChar;
    protected boolean isBattling;
    protected boolean isPaused;
    protected boolean isMoving;
    protected boolean isRunning;

    protected float walkSpeed = 1f;
    protected float runSpeedMultiplier = 2f;
    protected float stateTime = 0f;

    @NotNull
    protected MoveHistory moveHistory;
    @NotNull
    protected CombatStats combatStats = new CombatStats();
    @NotNull
    protected Weapon weapon = new WoodenSword();

    public PartyCharacter(CollisionContext collisionContext, EventDispatcher eventDispatcher, Party party) {
        this.collisionContext = collisionContext;
        this.eventDispatcher = eventDispatcher;
        this.party = party;

        this.animationManager = new AnimationManager();
        this.animationManager.setCurrentAnimation(AnimationId.IDLE_SOUTH);

        this.moveHistory = new MoveHistory(getX(), getY(), MoveHistory.STANDARD_LIMIT);

        setWidth(16);
        setHeight(16);

        collisionContext.addEntity(this);
        eventDispatcher.addEventListener(this);
        party.add(this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        animationManager.draw(batch, getX(), getY(), stateTime);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        if (isMainChar) {
            handleInput(delta);
        } else {
            actFollower(delta);
        }
        updateAnimation();
        moveHistory.add(getX(), getY());
    }

    protected void handleInput(float delta) {
        assert isMainChar();
        if (!isPaused) {
            float xMoveSpeed = 0;
            float yMoveSpeed = 0;

            if (isButtonPressed(0, Buttons.DPAD_LEFT)) {
                isMoving = true;
                xMoveSpeed = -walkSpeed;
                facing = Direction.WEST;
            }
            if (isButtonPressed(0, Buttons.DPAD_RIGHT)) {
                isMoving = true;
                xMoveSpeed = walkSpeed;
                facing = Direction.EAST;
            }
            if (isButtonPressed(0, Buttons.DPAD_UP)) {
                isMoving = true;
                yMoveSpeed = walkSpeed;
                facing = Direction.NORTH;
            }
            if (isButtonPressed(0, Buttons.DPAD_DOWN)) {
                isMoving = true;
                yMoveSpeed = -walkSpeed;
                facing = Direction.SOUTH;
            }
            isRunning = isButtonPressed(0, Buttons.B);

            if (isRunning) {
                xMoveSpeed *= runSpeedMultiplier;
                yMoveSpeed *= runSpeedMultiplier;
            }
            if (xMoveSpeed == 0 && yMoveSpeed == 0) {
                isMoving = false;
            } else {
                moveCollisionEntityBy(this, xMoveSpeed, yMoveSpeed);
            }
        }
    }

    protected void actFollower(float delta) {
        PartyCharacter nextPartyMember = Objects.requireNonNull(getNextPartyMember());
        if (nextPartyMember.moveHistory.size() > 0) {
            FloatPair last = nextPartyMember.moveHistory.getLast();
            setX(last.a);
            setY(last.b);
        }

        if (getX() > moveHistory.getPrevX()) {
            facing = Direction.EAST;
        } else if (getX() < moveHistory.getPrevX()) {
            facing = Direction.WEST;
        }
        if (getY() > moveHistory.getPrevY()) {
            facing = Direction.NORTH;
        } else if (getY() < moveHistory.getPrevY()) {
            facing = Direction.SOUTH;
        }

        if (getX() != moveHistory.getPrevX() || getY() != moveHistory.getPrevY()) {
            this.isMoving = true;
            this.isRunning = getNextPartyMember().isRunning;
        } else {
            this.isMoving = false;
            this.isRunning = false;
        }
    }

    protected void updateAnimation() {
        if (animationManager.getCurrentAnimation() == null) {
            animationManager.setCurrentAnimation(IDLE_SOUTH);
        }

        AnimationId north;
        AnimationId south;
        AnimationId west;
        AnimationId east;

        if (isRunning && isMoving && !isPaused) {
            north = RUN_NORTH;
            south = RUN_SOUTH;
            west = RUN_WEST;
            east = RUN_EAST;
        } else if (isMoving && !isPaused) {
            north = WALK_NORTH;
            south = WALK_SOUTH;
            west = WALK_WEST;
            east = WALK_EAST;
        } else {
            north = IDLE_NORTH;
            south = IDLE_SOUTH;
            west = IDLE_WEST;
            east = IDLE_EAST;
        }

        switch (facing) {
            case NORTH:
                animationManager.setCurrentAnimation(north);
                break;
            case SOUTH:
                animationManager.setCurrentAnimation(south);
                break;
            case WEST:
                animationManager.setCurrentAnimation(west);
                break;
            case EAST:
                animationManager.setCurrentAnimation(east);
                break;
        }
    }

    public boolean isMainChar() {
        return isMainChar;
    }

    private @Nullable PartyCharacter getNextPartyMember() {
        if (party.getCharacters().size < 2) {
            return null;
        }
        return party.getCharacters().get(party.indexOf(this) - 1);
    }

    public void setMainChar(boolean mainChar) {
        this.isMainChar = mainChar;
    }

    @Override
    public boolean handleEvent(Event event) {
        if (event instanceof ButtonEvent) {
            ButtonEvent be = (ButtonEvent) event;
            if (be.getButton() == Buttons.A) {
                // A-Button pressed
                if (isMainChar()) {
                    handleAPress(be);
                }
                return true;
            }
        }
        if (event instanceof HudPauseEvent) {
            isPaused = ((HudPauseEvent) event).isPaused();
        }
        return false;
    }

    protected void handleAPress(ButtonEvent event) {
        assert isMainChar();
        if (event.getButton() == Buttons.A && event.getEventType() == ButtonEventType.PRESS) {
            if (!isPaused) {
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
        return isMainChar();
    }

    @Override
    public int calculateAttackDamage() {
        return Math.round((combatStats.power * 4f / 3f) + (weapon.getAttackDamage() * 5f / 9f));
    }

    @Override
    public CombatStats getCombatStats() {
        return this.combatStats;
    }

    @Override
    public void goToBattle(BattlePoint battlePoint) {
        addAction(Actions.sequence(
                Actions.moveTo(battlePoint.getX(), battlePoint.getY(), BattlePoint.BATTLE_MOVE_DURATION),
                Actions.run(() -> {

                })
                ));
    }
}
