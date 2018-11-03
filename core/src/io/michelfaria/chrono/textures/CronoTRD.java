/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:44 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.textures;

import com.badlogic.gdx.graphics.g2d.Animation;

import static io.michelfaria.chrono.textures.TRD.flipHorizontal;

public final class CronoTRD {
    public static final TRD
            CRONO_IDLE_NORTH = new TRD("crono-idle-north", 1, 1),
            CRONO_IDLE_SOUTH = new TRD("crono-idle-south", 1, 1),
            CRONO_IDLE_WEST = new TRD("crono-idle-east", 1, 1, 0, null, flipHorizontal),
            CRONO_IDLE_EAST = new TRD("crono-idle-east", 1, 1),

    CRONO_WALK_NORTH = new TRD("crono-walk-north", 6, 1, 0.125f),
            CRONO_WALK_SOUTH = new TRD("crono-walk-south", 6, 1, 0.125f),
            CRONO_WALK_WEST = new TRD("crono-walk-east", 6, 1, 0.125f, null, flipHorizontal),
            CRONO_WALK_EAST = new TRD("crono-walk-east", 6, 1, 0.125f),

    CRONO_RUN_NORTH = new TRD("crono-run-north", 6, 1, 0.1f),
            CRONO_RUN_SOUTH = new TRD("crono-run-south", 6, 1, 0.1f),
            CRONO_RUN_WEST = new TRD("crono-run-east", 6, 1, 0.1f, null, flipHorizontal),
            CRONO_RUN_EAST = new TRD("crono-run-east", 6, 1, 0.1f),

    CRONO_BATTLE_NORTH = new TRD("crono-battle-north", 6, 1, 0.2f, null, null, Animation.PlayMode.NORMAL),
            CRONO_BATTLE_SOUTH = new TRD("crono-battle-south", 6, 1, 0.2f, null, null, Animation.PlayMode.NORMAL),
            CRONO_BATTLE_WEST = new TRD("crono-battle-east", 6, 1, 0.2f, null, flipHorizontal, Animation.PlayMode.NORMAL),
            CRONO_BATTLE_EAST = new TRD("crono-battle-east", 6, 1, 0.2f, null, null, Animation.PlayMode.NORMAL),

    CRONO_ATTACK_NORTH = new TRD("crono-attack-north", 6, 1, 0.2f, null, null, Animation.PlayMode.NORMAL),
            CRONO_ATTACK_SOUTH = new TRD("crono-attack-south", 6, 1, 0.2f, null, null, Animation.PlayMode.NORMAL),
            CRONO_ATTACK_WEST = new TRD("crono-attack-east", 6, 1, 0.2f, null, flipHorizontal, Animation.PlayMode.NORMAL),
            CRONO_ATTACK_EAST = new TRD("crono-attack-east", 6, 1, 0.2f, null, null, Animation.PlayMode.NORMAL);
}
