package io.michelfaria.chrono.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import io.michelfaria.chrono.values.TextureRegionDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnimationData<T> {

    @NotNull
    public final Animation<T> animation;
    @Nullable
    public final TextureRegionDescriptor textureRegionDescriptor;

    public AnimationData(@NotNull Animation<T> animation,
                         @Nullable TextureRegionDescriptor textureRegionDescriptor) {
        this.animation = animation;
        this.textureRegionDescriptor = textureRegionDescriptor;
    }
}
