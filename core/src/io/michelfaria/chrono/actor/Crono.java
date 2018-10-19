package io.michelfaria.chrono.actor;

import com.badlogic.gdx.graphics.g2d.Animation;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.animation.AnimationType;
import io.michelfaria.chrono.logic.CollisionContext;

import java.util.Map;

import static io.michelfaria.chrono.animation.AnimationType.*;
import static io.michelfaria.chrono.util.TextureTools.makeAnimation;
import static io.michelfaria.chrono.values.TextureRegionDescriptor.*;

public class Crono extends PartyCharacter {

    public Crono(Game game, CollisionContext collisionContext) {
        super(game, collisionContext);
        final Map<AnimationType, Animation<?>> animations = animationManager.animations;
        /*
         * Idle animations
         */
        animations.put(IDLE_NORTH, makeAnimation(game.atlas, CRONO_IDLE_NORTH));
        animations.put(IDLE_SOUTH, makeAnimation(game.atlas, CRONO_IDLE_SOUTH));
        animations.put(IDLE_WEST, makeAnimation(game.atlas, CRONO_IDLE_WEST));
        animations.put(IDLE_EAST, makeAnimation(game.atlas, CRONO_IDLE_EAST));
        /*
         * Walking animations
         */
        animations.put(WALK_NORTH, makeAnimation(game.atlas, CRONO_WALK_NORTH));
        animations.put(WALK_SOUTH, makeAnimation(game.atlas, CRONO_WALK_SOUTH));
        animations.put(WALK_WEST, makeAnimation(game.atlas, CRONO_WALK_WEST));
        animations.put(WALK_EAST, makeAnimation(game.atlas, CRONO_WALK_EAST));
        /*
         * Running animations
         */
        animations.put(RUN_NORTH, makeAnimation(game.atlas, CRONO_RUN_NORTH));
        animations.put(RUN_SOUTH, makeAnimation(game.atlas, CRONO_RUN_SOUTH));
        animations.put(RUN_WEST, makeAnimation(game.atlas, CRONO_RUN_WEST));
        animations.put(RUN_EAST, makeAnimation(game.atlas, CRONO_RUN_EAST));
    }
}