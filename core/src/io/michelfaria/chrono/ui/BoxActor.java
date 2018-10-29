/*
 * Developed by Michel Faria on 10/29/18 7:50 PM.
 * Last modified 10/25/18 7:46 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BoxActor extends Actor {

    private TextureRegion region;

    public BoxActor() {
        refresh();
    }

    public void refresh() {
        region = MenuBoxes.getDialogBoxGraphic();
        setWidth(region.getRegionWidth());
        setHeight(region.getRegionHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(region, getX(), getY());
    }
}
