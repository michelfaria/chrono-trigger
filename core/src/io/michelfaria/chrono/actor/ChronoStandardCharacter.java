package io.michelfaria.chrono.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.controller.Buttons;
import io.michelfaria.chrono.controller.Ctrl;

public abstract class ChronoStandardCharacter extends Actor {

    protected Game game;

    protected float walkSpeed = 1f;
    protected float runSpeedMultiplier = 2f;

    protected Animation<TextureRegion> idleNorth;
    protected Animation<TextureRegion> idleSouth;
    protected Animation<TextureRegion> idleWest;
    protected Animation<TextureRegion> idleEast;
    protected Animation<TextureRegion> walkNorth;
    protected Animation<TextureRegion> walkSouth;
    protected Animation<TextureRegion> walkWest;
    protected Animation<TextureRegion> walkEast;
    protected Animation<TextureRegion> runWest;
    protected Animation<TextureRegion> runEast;

    protected Direction facing = Direction.SOUTH;
    protected boolean moving = false;
    protected boolean running = false;
    protected float stateTime = 0;
    protected Animation<TextureRegion> animation;
    protected boolean handleInput = false;

    public ChronoStandardCharacter(Game game) {
        this.game = game;
    }

    /**
     * Splits a texture sequence (spritesheet) into individual TextureRegions in a 1-dimension array.
     * The textures are ordered left-to-right, up-to-down.
     *
     * @param atlas      Atlas containing texture regions
     * @param regionName Name of the atlas region
     * @param frameCols  Amount of columns in animation
     * @param frameRows  Amount of rows in animation
     */
    public Array<TextureRegion> splitTextureRegion(TextureAtlas atlas, String regionName, int frameCols, int frameRows) {
        TextureAtlas.AtlasRegion reg = atlas.findRegion(regionName);
        assert reg != null : regionName;

        // Use the split utility method to create a 2D array of TextureRegions.
        TextureRegion[][] tmp = reg.split(reg.getRegionWidth() / frameCols,
                reg.getRegionHeight() / frameRows);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        Array<TextureRegion> textures = new Array<>(frameRows * frameCols);
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                textures.add(tmp[i][j]);
            }
        }

        return textures;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (handleInput) {
            handleInput(delta);
        }
        updateAnimations();
    }

    protected void updateAnimations() {
        if (animation == null) {
            animation = idleSouth;
        }
        if (running && moving) {
            // Running
            switch (facing) {
                case NORTH:
                    break;
                case SOUTH:
                    break;
                case WEST:
                    animation = runWest;
                    break;
                case EAST:
                    animation = runEast;
                    break;
            }
        } else if (moving) {
            assert !running;
            // Walking
            switch (facing) {
                case NORTH:
                    animation = walkNorth;
                    break;
                case SOUTH:
                    animation = walkSouth;
                    break;
                case WEST:
                    animation = walkWest;
                    break;
                case EAST:
                    animation = walkEast;
                    break;
            }
        } else {
            // Standing still
            switch (facing) {
                case NORTH:
                    animation = idleNorth;
                    break;
                case SOUTH:
                    animation = idleSouth;
                    break;
                case WEST:
                    animation = idleWest;
                    break;
                case EAST:
                    animation = idleEast;
                    break;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame = animation.getKeyFrame(stateTime);

        batch.draw(currentFrame, getX(), getY());
    }

    protected void handleInput(float delta) {
        float xMoveSpeed = 0;
        float yMoveSpeed = 0;

        if (Ctrl.isButtonPressed(0, Buttons.DPAD_LEFT)) {
            moving = true;
            xMoveSpeed = -walkSpeed;
            facing = Direction.WEST;
        }
        if (Ctrl.isButtonPressed(0, Buttons.DPAD_RIGHT)) {
            moving = true;
            xMoveSpeed = walkSpeed;
            facing = Direction.EAST;
        }
        if (Ctrl.isButtonPressed(0, Buttons.DPAD_UP)) {
            moving = true;
            yMoveSpeed = walkSpeed;
            facing = Direction.NORTH;
        }
        if (Ctrl.isButtonPressed(0, Buttons.DPAD_DOWN)) {
            moving = true;
            yMoveSpeed = -walkSpeed;
            facing = Direction.SOUTH;
        }
        running = Ctrl.isButtonPressed(0, Buttons.B);
        if (running) {
            xMoveSpeed *= runSpeedMultiplier;
            yMoveSpeed *= runSpeedMultiplier;
        }
        if (xMoveSpeed == 0 && yMoveSpeed == 0) {
            moving = false;
        } else {
            addAction(Actions.moveBy(xMoveSpeed, yMoveSpeed));
        }
    }

    public void setHandleInput(boolean handleInput) {
        this.handleInput = handleInput;
    }
}
