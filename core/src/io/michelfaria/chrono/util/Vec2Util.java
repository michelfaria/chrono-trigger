/*
 * Developed by Michel Faria on 11/3/18 12:53 AM.
 * Last modified 11/3/18 12:53 AM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public final class Vec2Util {
    public static float angle(Vector2 veca, Vector2 vecb) {
        return MathUtils.atan2(vecb.y - veca.y, vecb.x - veca.x) * 180 / (float) Math.PI;
    }
}
