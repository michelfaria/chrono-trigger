/*
 * Developed by Michel Faria on 10/29/18 8:57 PM.
 * Last modified 10/28/18 11:15 AM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.logic;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import io.michelfaria.chrono.actors.BattlePoint;
import io.michelfaria.chrono.interfaces.Identifiable;

import java.util.*;

public final class BattlePointsValidator {

    public static int MINIMUM_PARTY_BATTLEPOINTS_PER_GROUP = 3;

    public BattlePointsValidator() {
    }

    /**
     * This method validates every instance of BattlePoint in an Array of Actors.
     * <p>
     * To understand what constitutes a valid BattlePoint, see {@link BattlePoint}.
     */
    public void validateBattlePoints(Array<Actor> actors) {
        List<BattlePoint> battlePoints = new ArrayList<>();
        List<Identifiable> identifiables = new ArrayList<>();

        for (Actor actor : actors) {
            if (actor instanceof BattlePoint) {
                battlePoints.add((BattlePoint) actor);
            }
            if (actor instanceof Identifiable) {
                identifiables.add((Identifiable) actor);
            }
        }

        for (BattlePoint battlePoint : battlePoints) {
            ensureBattlePointHasMatchingIndentifiable(battlePoint, identifiables);
        }
        Map<Integer, List<BattlePoint>> groups = compileGroups(battlePoints);
        ensureGroupsHaveAtLeastThreePartyTypeBattlePoints(groups);
        ensureGroupsHaveUniqueSubIds(groups);
        ensureGroupsHasOneCameraBattlePoint(groups);
    }

    /**
     * Ensures the BattlePoint provided has exactly one Identifiable with an "id" that matches its "subId".
     */
    private void ensureBattlePointHasMatchingIndentifiable(BattlePoint battlePoint, List<Identifiable> identifiables) {
        if (battlePoint.type == BattlePoint.Type.ENEMY) {
            // Ensure each battle point has _only_ *one* actor where the BattlePoint.subId is equal to the Actor's id
            List<Identifiable> matches = new ArrayList<>();

            for (Identifiable identifiable : identifiables) {
                if (identifiable.getId() == battlePoint.subId) {
                    matches.add(identifiable);
                }
            }
            if (matches.size() == 0) {
                throw new IllegalStateException("BattlePoint " + battlePoint.toString() + " has no matching entity");
            }
            if (matches.size() > 1) {
                throw new IllegalStateException("BattlePoint " + battlePoint.toString() + " has more than one"
                        + " matching entity: " + matches.toString());
            }
        }
    }

    /**
     * Separates the provided BattlePoints into groups.
     */
    private Map<Integer, List<BattlePoint>> compileGroups(List<BattlePoint> battlePoints) {
        Map<Integer, List<BattlePoint>> map = new HashMap<>();

        for (BattlePoint battlePoint : battlePoints) {
            if (map.get(battlePoint.groupId) == null) {
                List<BattlePoint> group = new ArrayList<>();
                group.add(battlePoint);
                map.put(battlePoint.groupId, group);
            } else {
                map.put(battlePoint.groupId, battlePoints);
            }
        }

        return map;
    }

    /**
     * Ensures each individual BattlePoint group has at least 3 PARTY BattlePoints.
     */
    private void ensureGroupsHaveAtLeastThreePartyTypeBattlePoints(Map<Integer, List<BattlePoint>> groups) {
        for (Map.Entry<Integer, List<BattlePoint>> entry : groups.entrySet()) {
            int partyBattlePoints = 0;
            for (BattlePoint battlePoint : entry.getValue()) {
                if (battlePoint.type == BattlePoint.Type.PARTY) {
                    partyBattlePoints++;
                }
            }
            if (partyBattlePoints < MINIMUM_PARTY_BATTLEPOINTS_PER_GROUP) {
                throw new IllegalStateException("BattlePoint group id " + entry.getKey() + " only has "
                        + partyBattlePoints + " party battle points. The minimum required is "
                        + MINIMUM_PARTY_BATTLEPOINTS_PER_GROUP);
            }
        }
    }

    /**
     * Ensures each battle point in each group has an unique subId.
     */
    private void ensureGroupsHaveUniqueSubIds(Map<Integer, List<BattlePoint>> groups) {
        for (Map.Entry<Integer, List<BattlePoint>> entry : groups.entrySet()) {
            Set<Integer> subIdSet = new HashSet<>();
            for (BattlePoint battlePoint : entry.getValue()) {
                if (!subIdSet.add(battlePoint.subId)) {
                    throw new IllegalStateException("Duplicate subId " + battlePoint.subId + " in group "
                            + entry.getKey() + ". Violating BattlePoint: " + battlePoint);
                }
            }
        }
    }

    private void ensureGroupsHasOneCameraBattlePoint(Map<Integer, List<BattlePoint>> groups) {
        for (Map.Entry<Integer, List<BattlePoint>> entry : groups.entrySet()) {
            int cameraPoints = 0;
            for (BattlePoint battlePoint : entry.getValue()) {
                if (battlePoint.type == BattlePoint.Type.CAMERA) {
                    cameraPoints++;
                }
            }
            if (cameraPoints != 1) {
                throw new IllegalStateException("Group (id: " + entry.getKey() + ") has " + cameraPoints + " camera points, when there should be exactly 1.");
            }
        }
    }
}
