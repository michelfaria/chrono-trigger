package io.michelfaria.chrono.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.animation.AnimationMaker;
import io.michelfaria.chrono.animation.AnimationManager;
import io.michelfaria.chrono.logic.CollisionContext;

import static io.michelfaria.chrono.animation.AnimationType.*;
import static io.michelfaria.chrono.values.TextureRegionDescriptor.*;

public class Nu extends Actor implements CollisionEntity {

    protected Game game;
    protected CollisionContext collisionContext;
    protected float stateTime = 0f;

    protected AnimationManager animationManager = new AnimationManager();

    public Nu(Game game, CollisionContext collisionContext) {
        this.game = game;
        this.collisionContext = collisionContext;

        animationManager.animations.put(IDLE_NORTH, AnimationMaker.makeAnimation(game.atlas, NU_IDLE_NORTH));
        animationManager.animations.put(IDLE_SOUTH, AnimationMaker.makeAnimation(game.atlas, NU_IDLE_SOUTH));
        animationManager.animations.put(IDLE_WEST, AnimationMaker.makeAnimation(game.atlas, NU_IDLE_WEST));
        animationManager.animations.put(IDLE_EAST, AnimationMaker.makeAnimation(game.atlas, NU_IDLE_EAST));
        animationManager.animations.put(WALK_NORTH, AnimationMaker.makeAnimation(game.atlas, NU_WALK_NORTH));
        animationManager.animations.put(WALK_SOUTH, AnimationMaker.makeAnimation(game.atlas, NU_WALK_SOUTH));
        animationManager.animations.put(WALK_WEST, AnimationMaker.makeAnimation(game.atlas, NU_WALK_WEST));
        animationManager.animations.put(WALK_EAST, AnimationMaker.makeAnimation(game.atlas, NU_WALK_EAST));

        animationManager.currentAnimation = IDLE_WEST;

        setWidth(16);
        setHeight(16);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        animationManager.draw(batch, getX() - 8, getY(), stateTime);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        float v = stateTime * 1000 % 4000;
        if (v > 3000) {
            animationManager.currentAnimation = WALK_EAST;
        } else if (v > 2000) {
            animationManager.currentAnimation = WALK_SOUTH;
        } else if (v > 1000) {
            animationManager.currentAnimation = WALK_WEST;
        } else {
            animationManager.currentAnimation = WALK_NORTH;
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
}
