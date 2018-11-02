/*
 * Developed by Michel Faria on 11/2/18 5:14 PM.
 * Last modified 11/2/18 5:14 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.logic.battle;

import com.badlogic.gdx.audio.Music;
import io.michelfaria.chrono.Assets;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.actors.BattlePoint;
import io.michelfaria.chrono.actors.PartyCharacter;
import io.michelfaria.chrono.interfaces.Combatant;
import io.michelfaria.chrono.interfaces.Positionable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static io.michelfaria.chrono.logic.battle.BattlePointsValidator.MINIMUM_PARTY_BATTLEPOINTS_PER_GROUP;

public class BattleCoordinator {

    private Game.Context ctx;

    public BattleCoordinator(Game.Context ctx) {
        this.ctx = ctx;
    }

    public void beginBattle(Combatant combatant) {
        assert !ctx.battleStatus.isBattling();
        assert ctx.battleStatus.combatantsReady.get() == 0;
        assert ctx.battleStatus.combatants.size == 0;

        List<BattlePoint> matching = findMatchingBattlePoints(combatant, ctx);
        if (matching.isEmpty()) {
            throw new IllegalStateException("Cannot begin battle: No BattlePoints with SubID " + combatant.getId() + " found.");
        }

        BattlePoint closest = Positionable.findClosest(combatant, matching);
        assert closest != null;

        List<BattlePoint> battlePointGroup = closest.getAllInTheSameGroup();
        assert battlePointGroup.size() >= MINIMUM_PARTY_BATTLEPOINTS_PER_GROUP : battlePointGroup.size();

        this.callCombatantsToBattle(battlePointGroup);
    }

    private void callCombatantsToBattle(List<BattlePoint> battlePointGroup) {
        // Needs to be Atomic because otherwise the 'done' lambda won't be able to use its value
        final AtomicInteger called = new AtomicInteger(0);

        // This is for debugging purposes
        int enemiesCalled = 0;
        int partyCharsCalled = 0;

        final Runnable done = () -> {
            if (ctx.battleStatus.combatantsReady.incrementAndGet() == called.get()) {
                Music battleMusic = ctx.assetManager.get(Assets.BATTLE_MUSIC);
                ctx.musicManager.setMusic(battleMusic);
                ctx.musicManager.playMusic(true);
            }
        };

        for (Combatant combatant : ctx.combatants) {
            final int id = combatant.getId();
            final BattlePoint.Type type = combatant.getBattlePointType();

            for (BattlePoint battlePoint : battlePointGroup) {
                if (battlePoint.subId == id && battlePoint.type == type) {
                    ctx.battleStatus.combatants.add(combatant);
                    combatant.goToBattle(battlePoint, done);
                    called.incrementAndGet();

                    switch (type) {
                        case ENEMY:
                            enemiesCalled++;
                            break;
                        case PARTY:
                            partyCharsCalled++;
                            break;
                        default:
                            throw new IllegalStateException("Called something that isn't an ENEMY or a PARTY character");
                    }
                }
            }
        }

        // Do validation
        if (Game.debug) {
            int enemyPoints = 0;
            int partyPoints = 0;
            for (BattlePoint battlePoint : battlePointGroup) {
                switch (battlePoint.type) {
                    case PARTY:
                        partyPoints++;
                        break;
                    case ENEMY:
                        enemyPoints++;
                        break;
                    case CAMERA:
                        break;
                }
            }
            assert enemiesCalled == enemyPoints : enemiesCalled;
            assert partyCharsCalled <= partyPoints : partyCharsCalled;
            assert ctx.party.size == partyCharsCalled : partyCharsCalled;
        }
    }

    public Combatant getClosestEnemy(PartyCharacter partyChar) {
        assert ctx.battleStatus.isBattling();
        assert ctx.battleStatus.getBattleGroup() != null;

        BattlePoint partyCharBp = findBattlePoint(partyChar, ctx.battleStatus.getBattleGroup());
        assert partyCharBp != null;

        Set<Combatant> allEnemiesInBattle = getAllEnemiesInBattle();
        assert allEnemiesInBattle.size() > 0;

        Combatant closest = Positionable.findClosest(partyChar, allEnemiesInBattle);
        assert closest != null;
        return closest;
    }

    private Set<Combatant> getAllEnemiesInBattle() {
        assert ctx.battleStatus.isBattling();
        assert ctx.battleStatus.getBattleGroup() != null;

        Set<Combatant> result = new HashSet<>();
        for (Combatant combatant : ctx.combatants) {
            if (combatant.getBattlePointType() != BattlePoint.Type.ENEMY) {
                continue;
            }
            for (BattlePoint battlePoint : ctx.battlePoints) {
                if (battlePoint.groupId != ctx.battleStatus.getBattleGroup() || battlePoint.subId != combatant.getId()) {
                    continue;
                }
                boolean add = result.add(combatant);
                if (!add) {
                    throw new IllegalStateException("Tried adding a duplicate enemy to the in-battle list");
                }
                break;
            }
        }
        return result;
    }

    /**
     * Find the BattlePoint for a Combatant, but filter the specified battle group.
     */
    private BattlePoint findBattlePoint(Combatant combatant, int battleGroup) {
        for (BattlePoint el : ctx.battlePoints) {
            final int lookForId = combatant.getId();
            final BattlePoint.Type type = combatant.getBattlePointType();

            if (el.groupId == battleGroup && el.subId == lookForId && el.type == type) {
                return el;
            }
        }
        return null;
    }

    /**
     * Returns the BattlePoints that the specified Combatant is qualified for.
     */
    public static List<BattlePoint> findMatchingBattlePoints(Combatant requesterCombatant, Game.Context ctx) {
        List<BattlePoint> matching = new ArrayList<>();
        for (BattlePoint battlePoint : ctx.battlePoints) {
            if (battlePoint.subId == requesterCombatant.getId()) {
                matching.add(battlePoint);
            }
        }
        return matching;
    }

}
