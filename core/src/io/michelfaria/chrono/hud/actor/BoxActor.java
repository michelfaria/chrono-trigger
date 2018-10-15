package io.michelfaria.chrono.hud.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import io.michelfaria.chrono.Core;

public class BoxActor extends Actor {
	private Core core;
	
	private TextureRegion txReg;

	public BoxActor(Core core) {
		this.core = core;
		refresh();
	}

	public void refresh() {
		txReg = core.getMenuBoxes().getDialogBox();
		setWidth(txReg.getRegionWidth());
		setHeight(txReg.getRegionHeight());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(txReg, getX(), getY());
	}
}
