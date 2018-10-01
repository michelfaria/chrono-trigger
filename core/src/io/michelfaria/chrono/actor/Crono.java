package io.michelfaria.chrono.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import io.michelfaria.chrono.Assets;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.animation.ExtendedAnimation;
import io.michelfaria.chrono.controller.Buttons;
import io.michelfaria.chrono.controller.Ctrl;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP_PINGPONG;

public class Crono extends Actor {

    private Game game;

    private static final float WALK_MOVEMENT_SPEED = 1f;
    private static final float RUN_SPEED_MULTIPLIER = 2f;

    private Animation<TextureRegion> idleNorth;
    private Animation<TextureRegion> idleSouth;
    private Animation<TextureRegion> idleWest;
    private Animation<TextureRegion> idleEast;
    private Animation<TextureRegion> walkNorth;
    private Animation<TextureRegion> walkSouth;
    private Animation<TextureRegion> walkWest;
    private Animation<TextureRegion> walkEast;
    private Animation<TextureRegion> runWest;
    private Animation<TextureRegion> runEast;

    private Direction facing = Direction.SOUTH;
    private boolean moving = false;
    private boolean running = false;
    private float stateTime = 0;
    private Animation<TextureRegion> animation;

    public Crono(Game game) {
        this.game = game;

        TextureAtlas atlas = game.asmgr.get(Assets.ASSET_TXATLAS, TextureAtlas.class);

        // Idle
        idleNorth = new Animation<TextureRegion>(0, atlas.findRegion("crono-idle-north"));
        idleSouth = new Animation<TextureRegion>(0, atlas.findRegion("crono-idle-south"));
        idleWest = new Animation<TextureRegion>(0, atlas.findRegion("crono-idle-west"));
        idleEast = new Animation<TextureRegion>(0, atlas.findRegion("crono-idle-east"));

        // Walk
        walkNorth = new Animation<>(0.125f, splitTexSeq(atlas, "crono-walk-north", 6, 1), LOOP);
        walkSouth = new Animation<>(0.125f, splitTexSeq(atlas, "crono-walk-south", 6, 1), LOOP);
        walkEast = new Animation<>(0.125f, splitTexSeq(atlas, "crono-walk-east", 6, 1), LOOP);
        walkWest = new Animation<>(0.125f, splitTexSeq(atlas, "crono-walk-west", 6, 1), LOOP);

        // Run
        runWest = new Animation<>(0.1f, splitTexSeq(atlas, "crono-run-west", 6, 1), LOOP_PINGPONG);
        runEast = new Animation<>(0.1f, splitTexSeq(atlas, "crono-run-east", 6, 1), LOOP_PINGPONG);
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
    public Array<TextureRegion> splitTexSeq(TextureAtlas atlas, String regionName, int frameCols, int frameRows) {
        AtlasRegion reg = atlas.findRegion(regionName);
        assert reg != null : regionName;

        // Use the split utility method to create a 2D array of TextureRegions.
        TextureRegion[][] tmp = reg.split(reg.getRegionWidth() / frameCols,
                reg.getRegionHeight() / frameRows);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        Array<TextureRegion> textures = new Array<>(frameRows * frameCols);
        int index = 0;
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
        running = Ctrl.isButtonPressed(0, Buttons.B);
        if (running) {
            xMoveSpeed *= RUN_SPEED_MULTIPLIER;
            yMoveSpeed *= RUN_SPEED_MULTIPLIER;
        }
        if (xMoveSpeed == 0 && yMoveSpeed == 0) {
            moving = false;
        } else {
            move(xMoveSpeed, yMoveSpeed);
        }
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

    protected void move(float x, float y) {
        moveBy(x, y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame = animation.getKeyFrame(stateTime);

        batch.draw(currentFrame, getX(), getY());
    }
}