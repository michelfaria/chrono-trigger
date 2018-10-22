package io.michelfaria.chrono.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * TRD == Texture Region Descriptor
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

    /**
     * Index instructions on how to assemble the animation.
     * Null if there is no animation.
     */
    @Nullable
    public final int[] assembly;
    /**
     * Instruction on how to flip the animation.
     */
    @Nullable
    public final FlipData flipData;

    public TRD(String regionName, int columns, int rows) {
        this(regionName, columns, rows, 0);
    }

    public TRD(String regionName, int columns, int rows, float speed) {
        this(regionName, columns, rows, speed, null);
    }

    public TRD(String regionName, int columns, int rows, float speed, int[] assembly) {
        this(regionName, columns, rows, speed, assembly, null);
    }

    public TRD(String regionName, int columns, int rows, float speed,
               @Nullable int[] assembly, @Nullable FlipData flipData) {
        this.regionName = regionName;
        this.columns = columns;
        this.rows = rows;
        this.speed = speed;
        this.assembly = assembly;
        this.flipData = flipData;
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
