/*
 * Developed by Michel Faria on 10/29/18 8:50 PM.
 * Last modified 10/29/18 4:46 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.interfaces;

import com.badlogic.gdx.math.Vector2;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.actors.BattlePoint;
import io.michelfaria.chrono.logic.battle.CombatStats;

import java.util.ArrayList;
import java.util.List;

public interface Combatant extends Identifiable, Positionable {

    CombatStats getCombatStats();

	int calculateAttackDamage();

	void goToBattle(BattlePoint battlePoint, Runnable done);

	default BattlePoint.Type getBattlePointType() {
	    return BattlePoint.Type.ENEMY;
    }
}
