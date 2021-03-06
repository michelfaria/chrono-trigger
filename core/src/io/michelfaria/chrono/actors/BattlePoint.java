/*
 * Developed by Michel Faria on 10/29/18 8:31 PM.
 * Last modified 10/29/18 4:51 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.interfaces.ActorFactory;
import io.michelfaria.chrono.interfaces.Positionable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.michelfaria.chrono.MapConstants.*;

/**
 * A battle point describes a position for one actor in a battle.
 * <p>
 * Battles take place in the same screen as the characters move around and talk to people. In the battles,
 * every party member and enemy will be appointed a position to stand in. These positions are defined by BattlePoints.
 * <p>
 * 1. Each BattlePoint belongs to a group (appointed by "groupId".) A group holds all the BattlePoints for a specific
 * battle. Therefore, all BattlePoints belonging to the same battle should have the same "groupId".
 * <p>
 * 1. b. A BattlePoint group needs to have at least 3 BattlePoints of the type PARTY.
 * <p>
 * 2. Each BattlePoint should appoint a position to either a party member or an enemy. This is definition is designated
 * by the "type" of the BattlePoint, which can be either "ENEMY" or "PARTY". In the case that this is a "PARTY"
 * BattlePoint, then this will be appointing the position for a party member.
 * <p>
 * 3. The party member or enemy that this BattlePoint is appointed to will be chosen by the "subId".
 * <p>
 * 3. a. In the case that a BattlePoint is assigned to a party member, then if "subId" is 0, this BattlePoint should be
 * assigned to the 0th party member. If "subId" is 1, to the 1st party member, and so on.
 * <p>
 * 3. b. In the case that a BattlePoint is assigned to an enemy, then the "subId" should be equal to that enemy's game
 * id.
 */
public class BattlePoint extends Actor implements Disposable, Positionable {

    public static float BATTLE_MOVE_DURATION = 1.5f;

    private Game.Context ctx;

    public final int groupId;
    public final int subId;
    public final Type type;

    private BattlePoint(Game.Context ctx, int groupId, int subId, Type type) {
        this.ctx = ctx;
        this.groupId = groupId;
        this.subId = subId;
        this.type = type;

        ctx.battlePoints.add(this);

        Gdx.app.debug(BattlePoint.class.getName(), this.toString());
    }

    public List<BattlePoint> getAllInTheSameGroup() {
        List<BattlePoint> battlePointGroup = new ArrayList<>();
        for (BattlePoint battlePoint : ctx.battlePoints) {
            if (battlePoint.groupId == groupId) {
                battlePointGroup.add(battlePoint);
            }
        }
        return battlePointGroup;
    }

    public static BattlePoint findCameraForGroup(Game.Context ctx, int battleGroup) {
        for (BattlePoint battlePoint : ctx.battlePoints) {
            if (battlePoint.groupId == battleGroup && battlePoint.type == Type.CAMERA) {
                return battlePoint;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "BattlePoint{" +
                "groupId=" + groupId +
                ", subId=" + subId +
                ", type=" + type +
                ", x=" + getX() +
                ", y=" + getY() +
                '}';
    }

    @Override
    public void dispose() {
        boolean wasInCollection = ctx.battlePoints.remove(this);
        assert wasInCollection;
    }

    public enum Type {
        PARTY, ENEMY, CAMERA
    }

    public static class Factory implements ActorFactory<BattlePoint> {

        private Game.Context ctx;

        public Factory(Game.Context ctx) {
            this.ctx = ctx;
        }

        @Override
        public BattlePoint make(MapProperties props) {
            int groupId = (Integer) Objects.requireNonNull(props.get(PROP_BATTLEPT_BATTLEID), "Group id not found");
            int subId = (Integer) Objects.requireNonNull(props.get(PROP_BATTLEPT_SUBID), "Sub id not found");
            Type type = Type.valueOf(
                    Objects.requireNonNull(
                            (String) props.get(PROP_BATTLEPT_TYPE), "Battle point type not found"));
            return new BattlePoint(ctx, groupId, subId, type);
        }

        @Override
        public Class<BattlePoint> actorClass() {
            return BattlePoint.class;
        }
    }
}
