/*
 * Developed by Michel Faria on 11/2/18 5:14 PM.
 * Last modified 11/2/18 4:55 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.logic.battle;

import com.badlogic.gdx.utils.Array;
import io.michelfaria.chrono.interfaces.Combatant;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

public class BattleStatus {
    @Nullable
    private Integer battleGroup;

    public Array<Combatant> combatants = new Array<>(Combatant.class);
    public AtomicInteger combatantsReady = new AtomicInteger(0);

    public boolean isBattling() {
        return battleGroup != null;
    }

    public void setBattleGroup(@Nullable Integer battleGroup) {
        this.battleGroup = battleGroup;
    }

    public Integer getBattleGroup() {
        return battleGroup;
    }

    public void reset() {
        battleGroup = null;
        combatants = new Array<>(Combatant.class);
        combatantsReady = new AtomicInteger(0);
    }
}
