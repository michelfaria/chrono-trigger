package io.michelfaria.chrono.textures;

import static io.michelfaria.chrono.textures.TRD.*;

public final class NuTRD {
    public static final TRD NU_IDLE_NORTH = new TRD("nu-walk-north", 3, 1, 0.3f, arr0),
            NU_IDLE_SOUTH = new TRD("nu-walk-south", 3, 1, 0.3f, arr0),
            NU_IDLE_WEST = new TRD("nu-walk-east", 3, 1, 0.3f, arr0, flipHorizontal),
            NU_IDLE_EAST = new TRD("nu-walk-east", 3, 1, 0.3f, arr0),

    NU_WALK_NORTH = new TRD("nu-walk-north", 3, 1, 0.3f, arr0_1_0_2),
            NU_WALK_SOUTH = new TRD("nu-walk-south", 3, 1, 0.3f, arr0_1_0_2),
            NU_WALK_WEST = new TRD("nu-walk-east", 3, 1, 0.3f, arr0_1_0_2, flipHorizontal),
            NU_WALK_EAST = new TRD("nu-walk-east", 3, 1, 0.3f, arr0_1_0_2);
}
