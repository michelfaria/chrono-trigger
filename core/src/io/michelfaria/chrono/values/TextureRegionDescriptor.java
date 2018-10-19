package io.michelfaria.chrono.values;

import io.michelfaria.chrono.values.TextureRegionDescriptor.FlipData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static io.michelfaria.chrono.values.TRDConstants.*;

class TRDConstants {
    public static final byte[] flipHorz = {FlipData.FLIP_HORZ};
    public static final int[] arr0 = {0};
    public static final FlipData flipHorizontal = new FlipData(arr0, flipHorz);
    public static final int[] arr0_1_0_2 = {0, 1, 0, 2};
}

public enum TextureRegionDescriptor {

    // @formatter:off
    CRONO_IDLE_NORTH("crono-idle-north", 1, 1),
    CRONO_IDLE_SOUTH("crono-idle-south", 1, 1),
    CRONO_IDLE_WEST("crono-idle-east", 1, 1, 0, null, flipHorizontal),
    CRONO_IDLE_EAST("crono-idle-east", 1, 1),
    CRONO_WALK_NORTH("crono-walk-north", 6, 1, 0.125f),
    CRONO_WALK_SOUTH("crono-walk-south", 6, 1, 0.125f),
    CRONO_WALK_WEST("crono-walk-east", 6, 1, 0.125f, null, flipHorizontal),
    CRONO_WALK_EAST("crono-walk-east", 6, 1, 0.125f),
    CRONO_RUN_NORTH("crono-run-north", 6, 1, 0.1f),
    CRONO_RUN_SOUTH("crono-run-south", 6, 1, 0.1f),
    CRONO_RUN_WEST("crono-run-east", 6, 1, 0.1f, null, flipHorizontal),
    CRONO_RUN_EAST("crono-run-east", 6, 1, 0.1f),

    UI_DIALOGBOX_0("ui-dialogbox-0", 1, 1),

    NU_IDLE_NORTH("nu-walk-north", 3, 1, 0.3f, arr0),
    NU_IDLE_SOUTH("nu-walk-south", 3, 1, 0.3f, arr0),
    NU_IDLE_WEST("nu-walk-east", 3, 1, 0.3f, arr0, flipHorizontal),
    NU_IDLE_EAST("nu-walk-east", 3, 1, 0.3f, arr0),

    NU_WALK_NORTH("nu-walk-north", 3, 1, 0.3f, arr0_1_0_2),
    NU_WALK_SOUTH("nu-walk-south", 3, 1, 0.3f, arr0_1_0_2),
    NU_WALK_WEST("nu-walk-east", 3, 1, 0.3f, arr0_1_0_2, flipHorizontal),
    NU_WALK_EAST("nu-walk-east", 3, 1, 0.3f, arr0_1_0_2);
    // @formatter:on

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

    TextureRegionDescriptor(String regionName, int columns, int rows) {
        this(regionName, columns, rows, 0);
    }

    TextureRegionDescriptor(String regionName, int columns, int rows, float speed) {
        this(regionName, columns, rows, speed, null);
    }

    TextureRegionDescriptor(String regionName, int columns, int rows, float speed, int[] assembly) {
        this(regionName, columns, rows, speed, assembly, null);
    }

    TextureRegionDescriptor(String regionName, int columns, int rows, float speed,
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
