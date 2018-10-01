package io.michelfaria.chrono.actor;

import com.badlogic.gdx.graphics.g2d.*;
import io.michelfaria.chrono.Assets;
import io.michelfaria.chrono.Game;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP_PINGPONG;

public class Crono extends ChronoStandardCharacter {

    public Crono(Game game) {
        super(game);

        TextureAtlas atlas = game.asmgr.get(Assets.ASSET_TXATLAS, TextureAtlas.class);

        // Idle
        idleNorth = new Animation<TextureRegion>(0, atlas.findRegion("crono-idle-north"));
        idleSouth = new Animation<TextureRegion>(0, atlas.findRegion("crono-idle-south"));
        idleWest = new Animation<TextureRegion>(0, atlas.findRegion("crono-idle-west"));
        idleEast = new Animation<TextureRegion>(0, atlas.findRegion("crono-idle-east"));

        // Walk
        walkNorth = new Animation<>(0.125f, splitTextureRegion(atlas, "crono-walk-north", 6, 1), LOOP);
        walkSouth = new Animation<>(0.125f, splitTextureRegion(atlas, "crono-walk-south", 6, 1), LOOP);
        walkEast = new Animation<>(0.125f, splitTextureRegion(atlas, "crono-walk-east", 6, 1), LOOP);
        walkWest = new Animation<>(0.125f, splitTextureRegion(atlas, "crono-walk-west", 6, 1), LOOP);

        // Run
        runWest = new Animation<>(0.1f, splitTextureRegion(atlas, "crono-run-west", 6, 1), LOOP_PINGPONG);
        runEast = new Animation<>(0.1f, splitTextureRegion(atlas, "crono-run-east", 6, 1), LOOP_PINGPONG);

        // Temporary
        setX((float) Math.random() * 200);
        setY((float) Math.random() * 200);
    }
}