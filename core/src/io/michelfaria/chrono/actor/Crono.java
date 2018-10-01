package io.michelfaria.chrono.actor;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import io.michelfaria.chrono.Game;
import io.michelfaria.chrono.util.TxUtil;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP_PINGPONG;

public class Crono extends GenericPartyCharacter {

    @SuppressWarnings("unchecked")
    public Crono(Game game) {
        super(game);

        TextureAtlas atlas = game.atlas;

        // Idle
        idleNorth = new Animation(0, atlas.findRegion("crono-idle-north"));
        idleSouth = new Animation(0, atlas.findRegion("crono-idle-south"));
        idleWest = new Animation(0, atlas.findRegion("crono-idle-west"));
        idleEast = new Animation(0, atlas.findRegion("crono-idle-east"));

        // Walk
        walkNorth = new Animation(0.125f, TxUtil.splitTextureRegion(atlas, "crono-walk-north", 6, 1), LOOP);
        walkSouth = new Animation(0.125f, TxUtil.splitTextureRegion(atlas, "crono-walk-south", 6, 1), LOOP);
        walkEast = new Animation(0.125f, TxUtil.splitTextureRegion(atlas, "crono-walk-east", 6, 1), LOOP);
        walkWest = new Animation(0.125f, TxUtil.splitTextureRegion(atlas, "crono-walk-west", 6, 1), LOOP);

        // Run
        runNorth = new Animation(0.1f, TxUtil.splitTextureRegion(atlas, "crono-run-north", 6, 1), LOOP);
        runSouth = new Animation(0.1f, TxUtil.splitTextureRegion(atlas, "crono-run-south", 6, 1), LOOP);
        runWest = new Animation(0.1f, TxUtil.splitTextureRegion(atlas, "crono-run-west", 6, 1), LOOP);
        runEast = new Animation(0.1f, TxUtil.splitTextureRegion(atlas, "crono-run-east", 6, 1), LOOP);

        // Temporary
        setX((float) Math.random() * 200);
        setY((float) Math.random() * 200);
    }
}