/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:38 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.animation;

import com.badlogic.gdx.graphics.g2d.Animation;

import io.michelfaria.chrono.textures.TRD;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnimationData<T> {

    @NotNull
    public final Animation<T> animation;
    @Nullable
    public final TRD trd;

    public AnimationData(@NotNull Animation<T> animation,
                         @Nullable TRD trd) {
        this.animation = animation;
        this.trd = trd;
    }
}
