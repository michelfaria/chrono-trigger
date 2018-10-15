package io.michelfaria.chrono.hud.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.values.Assets;

public class DialogBox extends Group {

	private BoxActor boxActor;

	private String text = "Hello world!";

	private Label.LabelStyle labelStyle;
	private Label label;

	public DialogBox() {
		boxActor = new BoxActor();
		addActor(boxActor);

		// Set up label style
		labelStyle = new Label.LabelStyle();
		labelStyle.font = Core.asmgr.get(Assets.FONT);
		labelStyle.font.getData().setScale(0.5f);
		labelStyle.fontColor = Color.WHITE;

		// Set up label
		label = new Label(null, labelStyle);
		label.setBounds(boxActor.getX() + 10, boxActor.getY() - 8, boxActor.getWidth() - 10, boxActor.getHeight());
		label.setAlignment(Align.topLeft);
		label.setWrap(true);
		addActor(label);

		label.setText(text);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
}
