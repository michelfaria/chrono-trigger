package io.michelfaria.chrono.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import io.michelfaria.chrono.util.TextureTools;
import io.michelfaria.chrono.values.TextureRegionDescriptor;
import org.jetbrains.annotations.NotNull;

public class AnimationMaker {
    /**
     * Creates an animation from a TextureAtlas and a TextureRegionDescriptor.
     */
    @NotNull
    public static AnimationData<TextureRegion> makeAnimation(TextureAtlas atlas, TextureRegionDescriptor trd) {
        Array<TextureRegion> textureRegions = TextureTools.splitTextureRegion(atlas, trd);
        if (trd.assembly != null) {
            return assembleAnimation(trd, textureRegions);
        } else {
            Animation<TextureRegion> animation = new Animation<>(trd.speed, textureRegions, Animation.PlayMode.LOOP);
            return new AnimationData<>(animation, trd);
        }
    }

    /**
     * Assembles an animation with assembly instructions.
     */
    @NotNull
    public static AnimationData<TextureRegion> assembleAnimation(TextureRegionDescriptor trd,
                                                                 Array<TextureRegion> textureRegions) {
        Array<TextureRegion> assembled = new Array<>(TextureRegion.class);
        for (int assemblyNum : trd.assembly) {
            if (assemblyNum > textureRegions.size) {
                throw new ArrayIndexOutOfBoundsException(assemblyNum);
            }
            assembled.add(textureRegions.get(assemblyNum));
        }
        assert assembled.size > 0;
        Animation<TextureRegion> animation = new Animation<>(trd.speed, assembled, Animation.PlayMode.LOOP);
        return new AnimationData<>(animation, trd);
    }
}
