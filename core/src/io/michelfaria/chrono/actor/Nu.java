package io.michelfaria.chrono.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.animation.AnimationManager;
import io.michelfaria.chrono.logic.CollisionContext;
import io.michelfaria.chrono.util.TextureTools;

import static io.michelfaria.chrono.animation.AnimationType.IDLE_SOUTH;
import static io.michelfaria.chrono.values.TextureRegionDescriptor.NU_IDLE_SOUTH;

public class Nu extends Actor implements CollisionEntity {

    protected Game game;
    protected CollisionContext collisionContext;
    protected float stateTime = 0f;

    protected AnimationManager animationManager = new AnimationManager();

    public Nu(Game game, CollisionContext collisionContext) {
        this.game = game;
        this.collisionContext = collisionContext;

        animationManager.animations.put(IDLE_SOUTH, TextureTools.makeAnimation(game.atlas, NU_IDLE_SOUTH));
        animationManager.currentAnimation = IDLE_SOUTH;

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
