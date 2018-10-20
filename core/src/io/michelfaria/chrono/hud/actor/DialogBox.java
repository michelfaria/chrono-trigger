package io.michelfaria.chrono.hud.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import io.michelfaria.chrono.hud.MenuBoxes;
import io.michelfaria.chrono.values.Assets;

public class DialogBox extends Group {

    private final BoxActor boxActor;

    private String text = "Hello world!";

    private final Label.LabelStyle labelStyle;
    private final Label label;

    public DialogBox(AssetManager assetManager, MenuBoxes menuBoxes) {
        this.boxActor = new BoxActor(menuBoxes);
        addActor(this.boxActor);

        // Set up label style
        this.labelStyle = new Label.LabelStyle();
        this.labelStyle.font = assetManager.get(Assets.FONT);
        this.labelStyle.font.getData().setScale(0.5f);
        this.labelStyle.fontColor = Color.WHITE;

        // Set up label
        this.label = new Label(null, labelStyle);
        this.label.setBounds(boxActor.getX() + 10, boxActor.getY() - 8, boxActor.getWidth() - 10, boxActor.getHeight());
        this.label.setAlignment(Align.topLeft);
        this.label.setWrap(true);
        addActor(this.label);

        this.label.setText(text);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public void setText(String string) {
        label.setText(string);
    }
}
