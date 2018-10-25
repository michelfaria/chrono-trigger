/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:44 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.interfaces;

import io.michelfaria.chrono.logic.CombatStats;

public interface Combatant {

	CombatStats getCombatStats();

	int calculateAttackDamage();
}
