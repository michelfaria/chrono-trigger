/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:38 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.michelfaria.chrono.animation.AnimationData;
import io.michelfaria.chrono.animation.AnimationId;
import io.michelfaria.chrono.animation.AnimationManager;
import io.michelfaria.chrono.consts.MapConstants;
import io.michelfaria.chrono.events.EventDispatcher;
import io.michelfaria.chrono.events.OpenDialogBoxEvent;
import io.michelfaria.chrono.interfaces.ActorFactory;
import io.michelfaria.chrono.interfaces.CollisionEntity;
import io.michelfaria.chrono.interfaces.Identifiable;
import io.michelfaria.chrono.interfaces.Interactible;
import io.michelfaria.chrono.logic.CollisionContext;

import javax.xml.soap.Text;
import java.util.Map;
import java.util.Objects;

import static io.michelfaria.chrono.animation.AnimationId.*;
import static io.michelfaria.chrono.animation.AnimationMaker.makeAnimation;
import static io.michelfaria.chrono.consts.MapConstants.PROP_ACTOR_ID;
import static io.michelfaria.chrono.textures.NuTRD.*;

public class Nu extends Actor implements CollisionEntity, Interactible, Identifiable {

    private final int id;
    private final EventDispatcher eventDispatcher;
    private final CollisionContext collisionContext;

    private float stateTime = 0f;

    private final AnimationManager animationManager;

    public Nu(CollisionContext collisionContext, EventDispatcher eventDispatcher, TextureAtlas atlas, int id) {
        this.eventDispatcher = eventDispatcher;
        this.collisionContext = collisionContext;
        this.id = id;
        this.animationManager = new AnimationManager();

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

        collisionContext.addEntity(this);
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
    public CollisionContext getCollisionContext() {
        return this.collisionContext;
    }

    @Override
    public boolean isCollisionEnabled() {
        return true;
    }

    @Override
    public void interact() {
        eventDispatcher.emitEvent(new OpenDialogBoxEvent("I am a Nu! I am at x:" + getX() + " and y: " + getY()));
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

    public static class NuFactory implements ActorFactory<Nu> {

        private CollisionContext collisionContext;
        private EventDispatcher eventDispatcher;
        private TextureAtlas textureAtlas;

        public NuFactory(CollisionContext collisionContext, EventDispatcher eventDispatcher, TextureAtlas textureAtlas) {
            this.collisionContext = collisionContext;
            this.eventDispatcher = eventDispatcher;
            this.textureAtlas = textureAtlas;
        }

        @Override
        public Nu make(MapProperties props) {
            Integer id = (Integer) props.get(PROP_ACTOR_ID);
            return new Nu(collisionContext, eventDispatcher, textureAtlas, id == null ? -1 : id);
        }

        @Override
        public Class<Nu> actorClass() {
            return Nu.class;
        }
    }

}
