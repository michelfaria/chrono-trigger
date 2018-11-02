/*
 * Developed by Michel Faria on 10/29/18 8:50 PM.
 * Last modified 10/29/18 3:16 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Disposable;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.animation.AnimationData;
import io.michelfaria.chrono.animation.AnimationId;
import io.michelfaria.chrono.animation.AnimationManager;
import io.michelfaria.chrono.interfaces.*;
import io.michelfaria.chrono.logic.battle.CombatStats;

import java.util.Map;

import static io.michelfaria.chrono.MapConstants.PROP_ACTOR_ID;
import static io.michelfaria.chrono.animation.AnimationId.*;
import static io.michelfaria.chrono.animation.AnimationMaker.makeAnimation;
import static io.michelfaria.chrono.textures.NuTRD.*;

public class Nu extends Actor implements CollisionEntity, Interactible, Identifiable, Disposable, Combatant {

    private Game.Context ctx;
    private int id;
    private AnimationManager animationManager = new AnimationManager();
    private CombatStats combatStats = new CombatStats();

    private float stateTime = 0f;

    public Nu(Game.Context ctx, int id) {
        this.ctx = ctx;
        this.id = id;

        final TextureAtlas atlas = ctx.getMainTextureAtlas();
        final Map<AnimationId, AnimationData<TextureRegion>> animations = animationManager.getAnimations();

        animations.put(IDLE_NORTH, makeAnimation(atlas, NU_IDLE_NORTH));
        animations.put(IDLE_SOUTH, makeAnimation(atlas, NU_IDLE_SOUTH));
        animations.put(IDLE_WEST, makeAnimation(atlas, NU_IDLE_WEST));
        animations.put(IDLE_EAST, makeAnimation(atlas, NU_IDLE_EAST));
        animations.put(WALK_NORTH, makeAnimation(atlas, NU_WALK_NORTH));
        animations.put(WALK_SOUTH, makeAnimation(atlas, NU_WALK_SOUTH));
        animations.put(WALK_WEST, makeAnimation(atlas, NU_WALK_WEST));
        animations.put(WALK_EAST, makeAnimation(atlas, NU_WALK_EAST));

        animationManager.setCurrentAnimation(IDLE_WEST);

        setWidth(16);
        setHeight(16);

        ctx.collisionEntities.add(this);
        ctx.combatants.add(this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        animationManager.draw(batch, getX() - 8, getY());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        float v = stateTime * 1000 % 4000;
        if (v > 3000) {
            animationManager.setCurrentAnimation(WALK_EAST);
        } else if (v > 2000) {
            animationManager.setCurrentAnimation(WALK_SOUTH);
        } else if (v > 1000) {
            animationManager.setCurrentAnimation(WALK_WEST);
        } else {
            animationManager.setCurrentAnimation(WALK_NORTH);
        }
    }

    @Override
    public boolean isCollisionEnabled() {
        return true;
    }

    @Override
    public void interact() {
        if (id == -1) {
            ctx.openDialogBox.accept("I am a Nu! I am at x:" + getX() + " and y: " + getY());
        } else if (id == 1000) {
            ctx.battleCoordinator.beginBattle(this);
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Nu{" +
                "id=" + id +
                ", x=" + getX() +
                ", y=" + getY() +
                '}';
    }

    @Override
    public CombatStats getCombatStats() {
        return this.combatStats;
    }

    @Override
    public int calculateAttackDamage() {
        return 1;
    }

    @Override
    public void goToBattle(BattlePoint battlePoint, Runnable done) {
        addAction(
                Actions.sequence(
                        Actions.moveTo(battlePoint.getX(), battlePoint.getY(), BattlePoint.BATTLE_MOVE_DURATION),
                        Actions.run(() -> {
                            Gdx.app.debug(Nu.class.getName(), "Nu moved to BattlePoint");
                            done.run();
                        })));
    }

    @Override
    public void dispose() {
        ctx.collisionEntities.remove(this);
        ctx.combatants.remove(this);
    }

    public static class NuFactory implements ActorFactory<Nu> {


        private Game.Context ctx;

        public NuFactory(Game.Context ctx) {
            this.ctx = ctx;
        }

        @Override
        public Nu make(MapProperties props) {
            Integer id = (Integer) props.get(PROP_ACTOR_ID);
            return new Nu(ctx, id == null ? -1 : id);
        }

        @Override
        public Class<Nu> actorClass() {
            return Nu.class;
        }
    }

}
