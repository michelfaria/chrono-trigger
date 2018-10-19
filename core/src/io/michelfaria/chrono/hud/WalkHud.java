package io.michelfaria.chrono.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.animation.ScissorAnimator;
import io.michelfaria.chrono.hud.actor.DialogBox;
import io.michelfaria.chrono.util.GroupUtil;

public class WalkHud implements Disposable {

    public OrthographicCamera camera;
    public Viewport viewport;
    public Stage stage;
    public DialogBox dialogBox;
    private Game game;
    private ScissorAnimator scissorAnimator;

    public WalkHud(Game game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(256, 224);
        stage = new Stage(viewport, game.batch);

        // Set up dialog box
        setDialogBoxType(0);
        dialogBox = new DialogBox(game);

        // Set up animator for the dialog box
        scissorAnimator = new ScissorAnimator(game, new Rectangle(dialogBox.getX(),
                dialogBox.getY(), GroupUtil.getWidth(dialogBox), GroupUtil.getHeight(dialogBox)),
                viewport);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void draw() {
        stage.draw();
        game.batch.begin();

        scissorAnimator.scissor(() -> dialogBox.draw(game.batch, 1));

        game.batch.end();
    }

    public void update(float delta) {
        stage.act(delta);
        dialogBox.act(delta);

        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            game.state.hudPause = true;
            scissorAnimator.open();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
            game.state.hudPause = false;
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
        game.menuBoxes.setUiType(type);
    }
}
