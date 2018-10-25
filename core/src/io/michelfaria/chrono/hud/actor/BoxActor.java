/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:44 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.hud.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.michelfaria.chrono.hud.MenuBoxes;

public class BoxActor extends Actor {

    private final MenuBoxes menuBoxes;
    private TextureRegion textureRegion;

    public BoxActor(MenuBoxes menuBoxes) {
        this.menuBoxes = menuBoxes;
        refresh();
    }

    public void refresh() {
        textureRegion = menuBoxes.getDialogBox();
        setWidth(textureRegion.getRegionWidth());
        setHeight(textureRegion.getRegionHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY());
    }
}
