/*
 * Developed by Michel Faria on 11/2/18 5:14 PM.
 * Last modified 11/2/18 4:55 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.logic.battle;

import io.michelfaria.chrono.interfaces.Combatant;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class BattleStatus {
    @Nullable
    private Integer battleGroup;

    public Set<Combatant> combatantsInBattle = new HashSet<>();
    public AtomicInteger combatantsReady = new AtomicInteger(0);

    /**
     * To be engaged: is to be in a battle state, but not necessarily ready to fight.
     */
    public boolean isEngaged() {
        return battleGroup != null;
    }

    /**
     * To be battling: is to be in a battle state and ready to fight.
     */
    public boolean isBattling() {
        return isEngaged() && combatantsInBattle.size() == combatantsReady.get();
    }

    public void setBattleGroup(@Nullable Integer battleGroup) {
        this.battleGroup = battleGroup;
    }

    public Integer getBattleGroup() {
        return battleGroup;
    }

    public void reset() {
        battleGroup = null;
        combatantsInBattle = new HashSet<>();
        combatantsReady = new AtomicInteger(0);
    }
}
