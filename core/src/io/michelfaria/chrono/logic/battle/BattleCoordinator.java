/*
 * Developed by Michel Faria on 11/2/18 5:14 PM.
 * Last modified 11/2/18 5:14 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.logic.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.michelfaria.chrono.Assets;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.actors.BattlePoint;
import io.michelfaria.chrono.actors.PartyCharacter;
import io.michelfaria.chrono.interfaces.Combatant;

import java.util.List;
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

        List<BattlePoint> matching = Combatant.findMatchingBattlePoints(combatant, ctx);
        if (matching.isEmpty()) {
            throw new IllegalStateException("Cannot begin battle: No BattlePoints with SubID " + combatant.getId() + " found.");
        }

        BattlePoint closest = Combatant.findClosestBattlePointFromList(combatant, matching);
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
            int id;
            BattlePoint.Type type = BattlePoint.Type.ENEMY;

            if (combatant instanceof PartyCharacter) {
                final PartyCharacter partyCharacter = (PartyCharacter) combatant;
                id = ctx.party.indexOf(partyCharacter, true);
                type = BattlePoint.Type.PARTY;
                assert id >= 0;
            } else {
                id = combatant.getId();
            }

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
}
