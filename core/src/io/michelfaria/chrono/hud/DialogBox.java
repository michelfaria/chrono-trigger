package io.michelfaria.chrono.hud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class DialogBox extends Actor {

    private TextureRegion txReg;

    public DialogBox() {
        refresh();
    }

    public void refresh() {
        txReg = MenuBoxes.getDialogBox();
        setWidth(txReg.getRegionWidth());
        setHeight(txReg.getRegionHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(txReg, getX(), getY());
    }
}
