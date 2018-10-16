package io.michelfaria.chrono.actor;

import static io.michelfaria.chrono.animation.AnimationType.IDLE_SOUTH;
import static io.michelfaria.chrono.values.TxRegs.NU_IDLE_SOUTH;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.Positionable;
import io.michelfaria.chrono.animation.AnimationManager;

public class Nu extends Actor implements Positionable {

	protected Core core;
	protected AnimationManager aniMan;

	public Nu(Core core) {
		this.core = core;
		this.aniMan = new AnimationManager(this);

		this.aniMan.anims.put(IDLE_SOUTH, core.getTxTools().makeAnimation(NU_IDLE_SOUTH));
		this.aniMan.anim = IDLE_SOUTH;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		aniMan.draw(batch, parentAlpha);
	}
}
