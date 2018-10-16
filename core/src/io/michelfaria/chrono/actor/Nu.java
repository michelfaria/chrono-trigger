package io.michelfaria.chrono.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.animation.AnimationManager;
import io.michelfaria.chrono.logic.CollisionContext;

import static io.michelfaria.chrono.animation.AnimationType.IDLE_SOUTH;
import static io.michelfaria.chrono.values.TxRegs.NU_IDLE_SOUTH;

public class Nu extends Actor implements Positionable, CollisionEntity {

    protected Core core;
    protected CollisionContext collisionContext;

    protected AnimationManager aniMan;

    public Nu(Core core, CollisionContext collisionContext) {
        this.core = core;
        this.collisionContext = collisionContext;

        this.aniMan = new AnimationManager(this);
        aniMan.xOffset = -8;
        aniMan.anims.put(IDLE_SOUTH, core.getTxTools().makeAnimation(NU_IDLE_SOUTH));
        aniMan.anim = IDLE_SOUTH;
        setWidth(16);
        setHeight(16);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        aniMan.draw(batch, parentAlpha);
    }

    @Override
    public CollisionContext getCollisionContext() {
        return this.collisionContext;
    }
}
