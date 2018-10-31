/*
 * Developed by Michel Faria on 10/31/18 7:17 PM.
 * Last modified 10/31/18 7:17 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.logic;

import org.jetbrains.annotations.Nullable;

public final class BattleStatus {
    @Nullable
    private static Integer battleGroup;

    public static boolean isBattling() {
        return battleGroup != null;
    }

    public static void setBattleGroup(@Nullable Integer battleGroup) {
        BattleStatus.battleGroup = battleGroup;
    }

    public static Integer getBattleGroup() {
        return battleGroup;
    }
}
