package io.michelfaria.chrono.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.michelfaria.chrono.Core;
import io.michelfaria.chrono.animation.ScissorAnimator;
import io.michelfaria.chrono.hud.actor.DialogBox;
import io.michelfaria.chrono.util.GroupUtil;

public class WalkHud implements Disposable {

	private Core core;

	public OrthographicCamera camera;
	public Viewport viewport;
	public Stage stage;

	public DialogBox dialogBox;

	private ScissorAnimator scissorAnimator;

	public WalkHud(Core core) {
		this.core = core;

		this.camera = new OrthographicCamera();
		this.viewport = new FitViewport(core.getVirtualWidth(), core.getVirtualHeight());
		this.stage = new Stage(viewport, core.getBatch());

		// Set up dialog box
		setDialogBoxType(0);
		this.dialogBox = new DialogBox(core);

		// Set up animator for the dialog box
		this.scissorAnimator = new ScissorAnimator(core, new Rectangle(dialogBox.getX(),
				dialogBox.getY(), GroupUtil.getWidth(dialogBox), GroupUtil.getHeight(dialogBox)),
				viewport);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	public void draw() {
		stage.draw();
		core.getBatch().begin();

		scissorAnimator.scissor(() -> {
			dialogBox.draw(core.getBatch(), 1);
		});

		core.getBatch().end();
	}

	public void update(float delta) {
		stage.act(delta);
		dialogBox.act(delta);

		if (Gdx.input.isKeyPressed(Input.Keys.P)) {
			core.getState().setHudPause(true);
			scissorAnimator.open();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.O)) {
			core.getState().setHudPause(false);
			scissorAnimator.close();
		}
	}

	/**
	 * Sets the dialog box for the Hud.
	 */
	public void setDialogBoxType(int type) {
		if (type < 0) {
			throw new IllegalArgumentException("type must be >= 0");
		}
		core.getMenuBoxes().setUiType(type);
	}
}
