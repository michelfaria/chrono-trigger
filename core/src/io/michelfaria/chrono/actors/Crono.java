/*
 * Developed by Michel Faria on 10/29/18 8:50 PM.
 * Last modified 10/26/18 5:39 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.actors;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.animation.AnimationData;
import io.michelfaria.chrono.animation.GenericAnimationId;

import java.util.Map;

import static io.michelfaria.chrono.animation.GenericAnimationId.*;
import static io.michelfaria.chrono.animation.AnimationMaker.makeAnimation;
import static io.michelfaria.chrono.textures.CronoTRD.*;

public class Crono extends PartyCharacter {

    public Crono(Game.Context ctx) {
        super(ctx);
        final TextureAtlas atlas = ctx.getMainTextureAtlas();
        final Map<GenericAnimationId, AnimationData<TextureRegion>> animations = animationManager.getAnimations();
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
        /*
         * Battle animations
         */
        animations.put(BATTLE_NORTH, makeAnimation(atlas, CRONO_BATTLE_NORTH));
        animations.put(BATTLE_SOUTH, makeAnimation(atlas, CRONO_BATTLE_SOUTH));
        animations.put(BATTLE_WEST, makeAnimation(atlas, CRONO_BATTLE_WEST));
        animations.put(BATTLE_EAST, makeAnimation(atlas, CRONO_BATTLE_EAST));
    }

    @Override
    public String toString() {
        return "Crono{" +
                "x=" + getX() +
                ", y=" + getY() +
                '}';
    }
}