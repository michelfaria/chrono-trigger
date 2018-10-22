package io.michelfaria.chrono.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import io.michelfaria.chrono.util.TextureTools;
import io.michelfaria.chrono.data.TRD;
import org.jetbrains.annotations.NotNull;

public class AnimationMaker {

    @NotNull
    public static AnimationData<TextureRegion> makeAnimation(TextureAtlas atlas, TRD trd) {

        final Array<TextureRegion> textureRegions = TextureTools.splitTextureRegion(atlas, trd);

        if (trd.assembly != null) {
            return makeAnimationWithAssemblyInstructions(textureRegions, trd);
        } else {
            final Animation<TextureRegion> animation
                    = new Animation<>(trd.speed, textureRegions, Animation.PlayMode.LOOP);
            return new AnimationData<>(animation, trd);
        }
    }

    @NotNull
    public static AnimationData<TextureRegion> makeAnimationWithAssemblyInstructions(Array<TextureRegion> textureRegions, TRD trd) {

        final Array<TextureRegion> assembled = new Array<>(TextureRegion.class);

        for (int assemblyNum : trd.assembly) {
            if (assemblyNum > textureRegions.size) {
                throw new ArrayIndexOutOfBoundsException(assemblyNum);
            }
            assembled.add(textureRegions.get(assemblyNum));
        }
        assert assembled.size > 0;

        final Animation<TextureRegion> animation
                = new Animation<>(trd.speed, assembled, Animation.PlayMode.LOOP);
        return new AnimationData<>(animation, trd);
    }
}
