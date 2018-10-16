package io.michelfaria.chrono.actor;

import com.badlogic.gdx.graphics.g2d.Animation;
import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.animation.AnimationType;
import io.michelfaria.chrono.logic.CollisionContext;
import io.michelfaria.chrono.util.TextureTools;

import java.util.Map;

import static io.michelfaria.chrono.animation.AnimationType.*;
import static io.michelfaria.chrono.values.TxRegs.*;

public class Crono extends PartyCharacter {

    public Crono(Core core, CollisionContext collisionContext) {
        super(core, collisionContext);
        final Map<AnimationType, Animation<?>> anims = aniMan.anims;
        final TextureTools tools = core.getTxTools();
        /*
         * Idle animations
         */
        anims.put(IDLE_NORTH, tools.makeAnimation(CRONO_IDLE_NORTH));
        anims.put(IDLE_SOUTH, tools.makeAnimation(CRONO_IDLE_SOUTH));
        anims.put(IDLE_WEST, tools.makeAnimation(CRONO_IDLE_WEST));
        anims.put(IDLE_EAST, tools.makeAnimation(CRONO_IDLE_EAST));
        /*
         * Walking animations
         */
        anims.put(WALK_NORTH, tools.makeAnimation(CRONO_WALK_NORTH));
        anims.put(WALK_SOUTH, tools.makeAnimation(CRONO_WALK_SOUTH));
        anims.put(WALK_WEST, tools.makeAnimation(CRONO_WALK_WEST));
        anims.put(WALK_EAST, tools.makeAnimation(CRONO_WALK_EAST));
        /*
         * Running animations
         */
        anims.put(RUN_NORTH, tools.makeAnimation(CRONO_RUN_NORTH));
        anims.put(RUN_SOUTH, tools.makeAnimation(CRONO_RUN_SOUTH));
        anims.put(RUN_WEST, tools.makeAnimation(CRONO_RUN_WEST));
        anims.put(RUN_EAST, tools.makeAnimation(CRONO_RUN_EAST));
    }
}