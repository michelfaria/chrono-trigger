package io.michelfaria.chrono.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.michelfaria.chrono.Assets;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.controller.Buttons;
import io.michelfaria.chrono.controller.Ctrl;

public class Crono extends Actor {

    private final Game game;

    private static final String REG_IDLE_SOUTH = "crono-idle-south",
            REG_IDLE_WEST = "crono-idle-west",
            REG_IDLE_EAST = "crono-idle-east",
            REG_IDLE_NORTH = "crono-idle-north",
            REG_WALK_NORTH = "crono-walk-north",
            REG_WALK_SOUTH = "crono-walk-south",
            REG_WALK_EAST = "crono-walk-east",
            REG_WALK_WEST = "crono-walk-west";

    private static final short IDLE_SOUTH_FRAME_COLS = 3,
            IDLE_SOUTH_FRAME_ROWS = 1,
            IDLE_WEST_FRAME_COLS = 3,
            IDLE_WEST_FRAME_ROWS = 1,
            IDLE_EAST_FRAME_COLS = 3,
            IDLE_EAST_FRAME_ROWS = 1,
            IDLE_NORTH_FRAME_COLS = 1,
            IDLE_NORTH_FRAME_ROWS = 1,
            WALK_NORTH_FRAME_COLS = 6,
            WALK_NORTH_FRAME_ROWS = 1,
            WALK_SOUTH_FRAME_COLS = 6,
            WALK_SOUTH_FRAME_ROWS = 1,
            WALK_EAST_FRAME_COLS = 6,
            WALK_EAST_FRAME_ROWS = 1,
            WALK_WEST_FRAME_COLS = 6,
            WALK_WEST_FRAME_ROWS = 1;

    private static final float WALK_ANIMATION_SPEED = 0.125f,
            WALK_MOVEMENT_SPEED = 0.75f;

    private final Animation<TextureRegion> idleSouthAni, idleWestAni, idleEastAni,
            idleNorthAni, walkNorthAni, walkSouthAni, walkEastAni, walkWestAni;

    // Mutable variables
    private Direction facing = Direction.SOUTH;
    private boolean moving = false,
            running = false;
    private Animation<? extends TextureRegion> animation;
    private float stateTime = 0;

    public Crono(Game game) {
        this.game = game;

        final TextureAtlas atlas = game.asmgr.get(Assets.ASSET_TXATLAS, TextureAtlas.class);

        idleSouthAni = createAnimation(atlas, REG_IDLE_SOUTH, IDLE_SOUTH_FRAME_COLS, IDLE_SOUTH_FRAME_ROWS, 0.3f);
        idleWestAni = createAnimation(atlas, REG_IDLE_WEST, IDLE_WEST_FRAME_COLS, IDLE_WEST_FRAME_ROWS, 0.3f);
        idleEastAni = createAnimation(atlas, REG_IDLE_EAST, IDLE_EAST_FRAME_COLS, IDLE_EAST_FRAME_ROWS, 0.3f);
        idleNorthAni = createAnimation(atlas, REG_IDLE_NORTH, IDLE_NORTH_FRAME_COLS, IDLE_NORTH_FRAME_ROWS, 0);
        walkNorthAni = createAnimation(atlas, REG_WALK_NORTH, WALK_NORTH_FRAME_COLS, WALK_NORTH_FRAME_ROWS, WALK_ANIMATION_SPEED);
        walkSouthAni = createAnimation(atlas, REG_WALK_SOUTH, WALK_SOUTH_FRAME_COLS, WALK_SOUTH_FRAME_ROWS, WALK_ANIMATION_SPEED);
        walkEastAni = createAnimation(atlas, REG_WALK_EAST, WALK_EAST_FRAME_COLS, WALK_EAST_FRAME_ROWS, WALK_ANIMATION_SPEED);
        walkWestAni = createAnimation(atlas, REG_WALK_WEST, WALK_WEST_FRAME_COLS, WALK_WEST_FRAME_ROWS, WALK_ANIMATION_SPEED);
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
        handleInput();
        updateAnimations();
    }

    protected void handleInput() {
        float xMoveSpeed = 0;
        float yMoveSpeed = 0;

        if (Ctrl.isButtonPressed(0, Buttons.DPAD_LEFT)) {
            moving = true;
            xMoveSpeed = -WALK_MOVEMENT_SPEED;
            facing = Direction.WEST;
        }
        if (Ctrl.isButtonPressed(0, Buttons.DPAD_RIGHT)) {
            moving = true;
            xMoveSpeed = WALK_MOVEMENT_SPEED;
            facing = Direction.EAST;
        }
        if (Ctrl.isButtonPressed(0, Buttons.DPAD_UP)) {
            moving = true;
            yMoveSpeed = WALK_MOVEMENT_SPEED;
            facing = Direction.NORTH;
        }
        if (Ctrl.isButtonPressed(0, Buttons.DPAD_DOWN)) {
            moving = true;
            yMoveSpeed = -WALK_MOVEMENT_SPEED;
            facing = Direction.SOUTH;
        }
        if (xMoveSpeed == 0 && yMoveSpeed == 0) {
            moving = false;
        } else {
            move(xMoveSpeed, yMoveSpeed);
        }
    }

    protected void updateAnimations() {
        if (animation == null) {
            animation = idleSouthAni;
        }
        if (moving) {
            // Walking
            switch (facing) {
                case NORTH:
                    animation = walkNorthAni;
                    break;
                case SOUTH:
                    animation = walkSouthAni;
                    break;
                case WEST:
                    animation = walkWestAni;
                    break;
                case EAST:
                    animation = walkEastAni;
                    break;
            }
        } else {
            // Standing still
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
            }
        }
    }

    protected void move(float x, float y) {
        moveBy(x, y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();

        final TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, getX(), getY());
    }
}