package io.michelfaria.chrono.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.michelfaria.chrono.Assets;

public class Crono extends Actor {

    public enum Facing {
        SOUTH
    }

    private static final String REG_IDLE_SOUTH = "crono-idle-south";
    private static final int IDLE_SOUTH_FRAME_COLS = 3;
    private static final int IDLE_SOUTH_FRAME_ROWS = 1;

    private final Animation<TextureRegion> idleSouthAnimation;

    private Facing facing = Facing.SOUTH;
    private Animation<? extends TextureRegion> animation;
    private float stateTime = 0;

    public Crono(AssetManager asmgr) {
        final TextureAtlas atlas = asmgr.get(Assets.ASSET_TXATLAS, TextureAtlas.class);

        // Load the Idle South sheet
        final AtlasRegion regIdleSouth = atlas.findRegion(REG_IDLE_SOUTH);

        // Use the split utility method to create a 2D array of TextureRegions.
        final TextureRegion[][] tmp = regIdleSouth.split(regIdleSouth.getRegionWidth() / IDLE_SOUTH_FRAME_COLS,
                regIdleSouth.getRegionHeight() / IDLE_SOUTH_FRAME_ROWS);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        TextureRegion[] regIdleSouthFrames = new TextureRegion[IDLE_SOUTH_FRAME_ROWS * IDLE_SOUTH_FRAME_COLS];
        int index = 0;
        for (int i = 0; i < IDLE_SOUTH_FRAME_ROWS; i++) {
            for (int j = 0; j < IDLE_SOUTH_FRAME_COLS; j++) {
                regIdleSouthFrames[index++] = tmp[i][j];
            }
        }

        // Initialize the Animation with the frame interval and array of frames
        idleSouthAnimation = new Animation<>(0.3f, regIdleSouthFrames);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (animation == null) {
            animation = idleSouthAnimation;
        }
        switch (facing) {
            case SOUTH:
                animation = idleSouthAnimation;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();

        final TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, getX(), getY());
    }
}