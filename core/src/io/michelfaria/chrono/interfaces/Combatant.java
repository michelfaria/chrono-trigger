/*
 * Developed by Michel Faria on 10/29/18 8:50 PM.
 * Last modified 10/29/18 4:46 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.interfaces;

import io.michelfaria.chrono.actors.BattlePoint;
import io.michelfaria.chrono.logic.CombatStats;

public interface Combatant extends Identifiable {

	CombatStats getCombatStats();

	int calculateAttackDamage();

	void goToBattle(BattlePoint battlePoint);
}
