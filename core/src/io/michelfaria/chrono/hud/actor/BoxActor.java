package io.michelfaria.chrono.hud.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import io.michelfaria.chrono.Game;

public class BoxActor extends Actor {
	private Game game;
	
	private TextureRegion txReg;

	public BoxActor(Game game) {
		this.game = game;
		refresh();
	}

	public void refresh() {
        txReg = game.menuBoxes.getDialogBox();
		setWidth(txReg.getRegionWidth());
		setHeight(txReg.getRegionHeight());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(txReg, getX(), getY());
	}
}
