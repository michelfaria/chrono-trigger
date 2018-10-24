package io.michelfaria.chrono.interfaces;

import io.michelfaria.chrono.logic.CombatStats;

public interface Combatant {

	CombatStats getCombatStats();

	int calculateAttackDamage();
}
