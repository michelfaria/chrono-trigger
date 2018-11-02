/*
 * Developed by Michel Faria on 10/29/18 7:55 PM.
 * Last modified 10/25/18 7:46 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import io.michelfaria.chrono.Assets;
import io.michelfaria.chrono.Game;

@SuppressWarnings("FieldCanBeLocal")
public class DialogBoxGroup extends Group {

    private BoxActor boxActor;
    private Label.LabelStyle labelStyle;
    private Label label;

    public String text = "Hello world!";

    public DialogBoxGroup(Game.Context ctx) {
        boxActor = new BoxActor(ctx);
        addActor(boxActor);

        // Set up label style
        labelStyle = new Label.LabelStyle();
        labelStyle.font = ctx.assetManager.get(Assets.FONT);
        labelStyle.font.getData().setScale(0.5f);
        labelStyle.fontColor = Color.WHITE;

        // Set up label
        label = new Label(text, labelStyle);
        label.setBounds(boxActor.getX() + 10, boxActor.getY() - 8, boxActor.getWidth() - 10, boxActor.getHeight());
        label.setAlignment(Align.topLeft);
        label.setWrap(true);
        addActor(label);
    }


}
