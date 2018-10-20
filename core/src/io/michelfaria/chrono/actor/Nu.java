package io.michelfaria.chrono.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.michelfaria.chrono.State;
import io.michelfaria.chrono.animation.AnimationData;
import io.michelfaria.chrono.animation.AnimationId;
import io.michelfaria.chrono.animation.AnimationManager;
import io.michelfaria.chrono.events.EventDispatcher;
import io.michelfaria.chrono.events.OpenDialogBoxEvent;
import io.michelfaria.chrono.interfaces.CollisionEntity;
import io.michelfaria.chrono.interfaces.Interactible;
import io.michelfaria.chrono.logic.CollisionContext;

import java.util.Map;

import static io.michelfaria.chrono.animation.AnimationId.*;
import static io.michelfaria.chrono.animation.AnimationMaker.makeAnimation;
import static io.michelfaria.chrono.values.TextureRegionDescriptor.*;

public class Nu extends Actor implements CollisionEntity, Interactible {

    private EventDispatcher eventDispatcher;
    private CollisionContext collisionContext;
    private State state;

    private float stateTime = 0f;

    private AnimationManager animationManager;

    public Nu(CollisionContext collisionContext,
              EventDispatcher eventDispatcher, TextureAtlas atlas, State state) {
        this.eventDispatcher = eventDispatcher;
        this.collisionContext = collisionContext;
        this.state = state;
        this.animationManager = new AnimationManager(state);

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
}
