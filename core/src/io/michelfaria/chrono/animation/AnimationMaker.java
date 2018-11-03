/*
 * Developed by Michel Faria on 10/29/18 8:33 PM.
 * Last modified 10/25/18 7:45 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import io.michelfaria.chrono.textures.TRD;
import io.michelfaria.chrono.util.TextureTools;

import org.jetbrains.annotations.NotNull;

public final class AnimationMaker {

    @NotNull
    public static AnimationData<TextureRegion> makeAnimation(TextureAtlas atlas, TRD trd) {

        final Array<TextureRegion> textureRegions = TextureTools.splitTextureRegion(atlas, trd);

        if (trd.assembly == null) {
            final Animation<TextureRegion> animation = new Animation<>(trd.speed, textureRegions, trd.playMode);
            return new AnimationData<>(animation, trd);
        } else {
            return makeAnimationWithAssemblyInstructions(textureRegions, trd);
        }
    }

    @NotNull
    public static AnimationData<TextureRegion> makeAnimationWithAssemblyInstructions(Array<TextureRegion> textureRegions, TRD trd) {
        assert trd.assembly != null;

        final Array<TextureRegion> assembled = new Array<>(TextureRegion.class);
        for (int assemblyNum : trd.assembly) {
            if (assemblyNum > textureRegions.size) {
                throw new ArrayIndexOutOfBoundsException(assemblyNum);
            }
            assembled.add(textureRegions.get(assemblyNum));
        }
        assert assembled.size > 0;

        final Animation<TextureRegion> animation = new Animation<>(trd.speed, assembled, trd.playMode);
        return new AnimationData<>(animation, trd);
    }
}
