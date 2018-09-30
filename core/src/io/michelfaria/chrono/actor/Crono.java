package io.michelfaria.chrono.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.michelfaria.chrono.Assets;

public class Crono extends Actor {

    private static final String REG_IDLE_SOUTH = "crono-idle-south";
    private static final int IDLE_SOUTH_FRAME_COLS = 3;
    private static final int IDLE_SOUTH_FRAME_ROWS = 1;
    private static final String REG_IDLE_WEST = "crono-idle-west";
    private static final int IDLE_WEST_FRAME_COLS = 3;
    private static final int IDLE_WEST_FRAME_ROWS = 1;
    private static final String REG_IDLE_EAST = "crono-idle-east";
    private static final int IDLE_EAST_FRAME_COLS = 3;
    private static final int IDLE_EAST_FRAME_ROWS = 1;
    private static final String REG_IDLE_NORTH = "crono-idle-north";
    private static final int IDLE_NORTH_FRAME_COLS = 1;
    private static final int IDLE_NORTH_FRAME_ROWS = 1;

    private final Animation<TextureRegion> idleSouthAni;
    private final Animation<TextureRegion> idleWestAni;
    private final Animation<TextureRegion> idleEastAni;
    private final Animation<TextureRegion> idleNorthAni;

    private Facing facing = Facing.SOUTH;
    private Animation<? extends TextureRegion> animation;
    private float stateTime = 0;

    public Crono(AssetManager asmgr) {
        final TextureAtlas atlas = asmgr.get(Assets.ASSET_TXATLAS, TextureAtlas.class);

        idleSouthAni = createAnimation(atlas, REG_IDLE_SOUTH, IDLE_SOUTH_FRAME_COLS, IDLE_SOUTH_FRAME_ROWS, 0.3f);
        idleWestAni = createAnimation(atlas, REG_IDLE_WEST, IDLE_WEST_FRAME_COLS, IDLE_WEST_FRAME_ROWS, 0.3f);
        idleEastAni = createAnimation(atlas, REG_IDLE_EAST, IDLE_EAST_FRAME_COLS, IDLE_EAST_FRAME_ROWS, 0.3f);
        idleNorthAni = createAnimation(atlas, REG_IDLE_NORTH, IDLE_NORTH_FRAME_COLS, IDLE_NORTH_FRAME_COLS, 0);
    }

    /**
     * Assumes a left-to-right up-down animation sequence.
     *
     * @param atlas      Atlas containing texture regions
     * @param regionName Name of the atlas region
     * @param frameCols  Amount of columns in animation
     * @param frameRows  Amount of rows in animation
     * @return Animation
     */
    protected Animation<TextureRegion> createAnimation(final TextureAtlas atlas, final String regionName, final int frameCols, final int frameRows, final float frameDuration) {
        // Load the sheet
        final AtlasRegion reg = atlas.findRegion(regionName);
        assert reg != null : regionName;

        // Use the split utility method to create a 2D array of TextureRegions.
        final TextureRegion[][] tmp = reg.split(reg.getRegionWidth() / frameCols,
                reg.getRegionHeight() / frameRows);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        TextureRegion[] regIdleSouthFrames = new TextureRegion[frameRows * frameCols];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                regIdleSouthFrames[index++] = tmp[i][j];
            }
        }

        // Initialize the Animation with the frame interval and array of frames
        return new Animation<>(frameDuration, regIdleSouthFrames);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (animation == null) {
            animation = idleSouthAni;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            facing = Facing.NORTH;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            facing = Facing.SOUTH;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            facing = Facing.WEST;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            facing = Facing.EAST;
        }
        switch (facing) {
            case NORTH:
                animation = idleNorthAni;
                break;
            case SOUTH:
                animation = idleSouthAni;
                break;
            case WEST:
                animation = idleWestAni;
                break;
            case EAST:
                animation = idleEastAni;
                break;
            default:
                throw new RuntimeException("Unknown direction");
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