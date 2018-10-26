/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:38 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.actor;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.michelfaria.chrono.animation.AnimationData;
import io.michelfaria.chrono.animation.AnimationId;
import io.michelfaria.chrono.events.EventDispatcher;
import io.michelfaria.chrono.logic.CollisionContext;
import io.michelfaria.chrono.logic.Party;

import java.util.Map;

import static io.michelfaria.chrono.animation.AnimationId.*;
import static io.michelfaria.chrono.animation.AnimationMaker.makeAnimation;
import static io.michelfaria.chrono.textures.CronoTRD.*;

public class Crono extends PartyCharacter {

    public Crono(CollisionContext collisionContext, TextureAtlas atlas,
                 EventDispatcher eventDispatcher, Party party) {
        super(collisionContext, eventDispatcher, party);
        final Map<AnimationId, AnimationData<TextureRegion>> animations = animationManager.getAnimations();
        /*
         * Idle animations
         */
        animations.put(IDLE_NORTH, makeAnimation(atlas, CRONO_IDLE_NORTH));
        animations.put(IDLE_SOUTH, makeAnimation(atlas, CRONO_IDLE_SOUTH));
        animations.put(IDLE_WEST, makeAnimation(atlas, CRONO_IDLE_WEST));
        animations.put(IDLE_EAST, makeAnimation(atlas, CRONO_IDLE_EAST));
        /*
         * Walking animations
         */
        animations.put(WALK_NORTH, makeAnimation(atlas, CRONO_WALK_NORTH));
        animations.put(WALK_SOUTH, makeAnimation(atlas, CRONO_WALK_SOUTH));
        animations.put(WALK_WEST, makeAnimation(atlas, CRONO_WALK_WEST));
        animations.put(WALK_EAST, makeAnimation(atlas, CRONO_WALK_EAST));
        /*
         * Running animations
         */
        animations.put(RUN_NORTH, makeAnimation(atlas, CRONO_RUN_NORTH));
        animations.put(RUN_SOUTH, makeAnimation(atlas, CRONO_RUN_SOUTH));
        animations.put(RUN_WEST, makeAnimation(atlas, CRONO_RUN_WEST));
        animations.put(RUN_EAST, makeAnimation(atlas, CRONO_RUN_EAST));
    }

    @Override
    public String toString() {
        return "Crono{" +
                "x=" + getX() +
                ", y=" + getY() +
                '}';
    }
}