package io.michelfaria.chrono.hud;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.util.TxUtil;
import io.michelfaria.chrono.values.TxRegions;

import java.awt.*;

import static io.michelfaria.chrono.values.TxRegions.HudTxRegions.*;

public class PlayHud implements Disposable, IPlayHud {

    public Game game;

    public OrthographicCamera camera;
    public Viewport viewport;
    public Stage stage;

    TextButton.TextButtonStyle dialogBoxStyle;

    public PlayHud(Game game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(Game.V_WIDTH, Game.V_HEIGHT);
        stage = new Stage(viewport, game.batch);

        dialogBoxStyle = DialogBoxStyleFactory.getStyle(game);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void openTestDialogBox() {
        TextButton textButton = new TextButton("Hello world!", dialogBoxStyle);
        textButton.setPosition(0, 0);
        textButton.setSize(Game.V_WIDTH, Game.V_HEIGHT / 2.5f);
        textButton.getLabel().setAlignment(Align.topLeft);
        stage.addActor(textButton);
    }

    public void update(float dt) {

    }
}
