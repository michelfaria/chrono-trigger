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

    static List<BattlePoint> findMatchingBattlePoints(Combatant requesterCombatant, Game.Context ctx) {
        List<BattlePoint> matching = new ArrayList<>();
        for (BattlePoint battlePoint : ctx.battlePoints) {
            if (battlePoint.subId == requesterCombatant.getId()) {
                matching.add(battlePoint);
            }
        }
        return matching;
    }

    static BattlePoint findClosestBattlePointFromList(Combatant requesterCombatant, List<BattlePoint> battlePoints) {
        if (battlePoints.size() == 0) {
            throw new IllegalArgumentException("No BattlePoints provided");
        }
        BattlePoint closest = null;
        if (battlePoints.size() == 1) {
            return battlePoints.get(0);
        } else {
            assert battlePoints.size() > 0;
            float closestDistance = Float.MAX_VALUE;
            for (BattlePoint battlePoint : battlePoints) {
                // Calculate the distance between the BattlePoint and the Combatant
                float distance = Vector2.dst(battlePoint.getX(), battlePoint.getY(), requesterCombatant.getX(), requesterCombatant.getY());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closest = battlePoint;
                }
            }
        }
        assert closest != null;
        return closest;
    }
}
