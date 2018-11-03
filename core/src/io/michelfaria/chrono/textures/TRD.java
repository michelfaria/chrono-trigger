/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:44 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.textures;

import com.badlogic.gdx.graphics.g2d.Animation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Texture Region Descriptor (TRD)
 * <p>
 * The fields of this class describe how a region in a TextureAtlas should be
 * assembled into an animation.
 * <p>
 * 1. "regionName" is the name of the region in the TextureAtlas
 * <p>
 * 2. "columns" is the amount of columns in the animation, (0 if static!)
 * <p>
 * 3. "rows" is the amount of rows in the animation, (0 if static!)
 * <p>
 * 4. "speed" is the speed of the animation, (0 if static!)
 * <p>
 * 5. The "assembly" int[] represents how the subregions should be arranged in
 * the animation.
 * <p>
 * Example: A hypothetical animation has 3 frames. An int[] of {0, 1, 0, 2}
 * should instruct the animation assembler to first put the 0th frame in the
 * animation, then the 1st, then the 0th again, and then the 2nd.
 * <p>
 * 6. The "flipData" object contains instructions on how the animation should be
 * flipped.
 * <p>
 * Example: A hypothetical animation has 5 frames. The FlipData object for this
 * TRD has an "indexes" int[] of {0, 3} and a "flip" byte[] of {FLIP_NONE,
 * FLIP_HORZ}. What this means is that at the 0th frame of the animation, the
 * flip mode should be set to FLIP_NONE. Subsequent frames will also use the
 * FLIP_NONE mode until the 3rd frame, which has a flip instruction of
 * FLIP_HORZ. All following frames will also be flipped horizontally, until the
 * animator reaches a new instruction.
 * <p>
 * NOTE 0: Use constant, pre-defined arrays to avoid polluting the heap with
 * repetitive objects.
 * <p>
 * NOTE 1: If the texture region descriptor is not an animation, set the
 * columns, rows and speed to 0.
 * <p>
 * NOTE 2: The "assembly" and "flipData" fields can be set to null if not
 * needed.
 */
public class TRD {

    public static final byte[] flipHorz = {FlipData.FLIP_HORZ};
    public static final int[] arr0 = {0};
    public static final FlipData flipHorizontal = new FlipData(arr0, flipHorz);
    public static final int[] arr0_1_0_2 = {0, 1, 0, 2};

    public final String regionName;
    public final int columns;
    public final int rows;
    public final float speed;

    @Nullable
    public final int[] assembly;

    @Nullable
    public final FlipData flipData;

    public final Animation.PlayMode playMode;


    public TRD(String regionName, int columns, int rows) {
        this(regionName, columns, rows, 0);
    }

    public TRD(String regionName, int columns, int rows, float speed) {
        this(regionName, columns, rows, speed, null);
    }

    public TRD(String regionName, int columns, int rows, float speed, int[] assembly) {
        this(regionName, columns, rows, speed, assembly, null);
    }

    public TRD(String regionName, int columns, int rows, float speed, @Nullable int[] assembly,
               @Nullable FlipData flipData) {
        this(regionName, columns, rows, speed, assembly, flipData, Animation.PlayMode.LOOP);
    }

    public TRD(String regionName, int columns, int rows, float speed, @Nullable int[] assembly, @Nullable FlipData flipData, Animation.PlayMode playMode) {
        this.regionName = regionName;
        this.columns = columns;
        this.rows = rows;
        this.speed = speed;
        this.assembly = assembly;
        this.flipData = flipData;
        this.playMode = playMode;
    }

    public static final class FlipData {
        @NotNull
        public final int[] indexes;
        @NotNull
        public final byte[] flip;

        public static final byte FLIP_NONE = 0;
        public static final byte FLIP_HORZ = 1;
        public static final byte FLIP_VERT = 2;
        public static final byte FLIP_BOTH = 3;

        public FlipData(@NotNull int[] indexes, @NotNull byte[] flip) {
            this.indexes = indexes;
            this.flip = flip;
        }
    }
}
