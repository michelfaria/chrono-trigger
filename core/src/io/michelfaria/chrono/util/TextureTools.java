/*
 * Developed by Michel Faria on 10/25/18 7:45 PM.
 * Last modified 10/25/18 7:44 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono.util;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import io.michelfaria.chrono.textures.TRD;

public final class TextureTools {

    private TextureTools() {
    }

    /**
     * Splits a texture sequence (spritesheet) into individual TextureRegions in a
     * 1-dimension array. The textures are ordered left-to-right, up-to-down.
     */
    public static Array<TextureRegion> splitTextureRegion(TextureAtlas atlas, TRD txReg) {
        return splitTextureRegion(atlas, txReg.regionName, txReg.columns, txReg.rows);
    }

    /**
     * Splits a texture sequence (spritesheet) into individual TextureRegions in a
     * 1-dimension array. The textures are ordered left-to-right, up-to-down.
     */
    public static Array<TextureRegion> splitTextureRegion(TextureAtlas atlas,
                                                   String regionName, int frameCols,
                                                   int frameRows) {
        TextureAtlas.AtlasRegion reg = atlas.findRegion(regionName);
        if (reg == null) {
            throw new IllegalStateException("Texture region is null: " + regionName);
        }
        return splitTextureRegion(reg, frameCols, frameRows);

    }

    /**
     * Splits a texture sequence (spritesheet) into individual TextureRegions in a
     * 1-dimension array. The textures are ordered left-to-right, up-to-down.
     */
    public static Array<TextureRegion> splitTextureRegion(TextureRegion reg, int frameCols,
                                                   int frameRows) {
        // Use the split utility method to create a 2D array of TextureRegions.
        TextureRegion[][] tmp = reg.split(reg.getRegionWidth() / frameCols,
                reg.getRegionHeight() / frameRows);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        Array<TextureRegion> textures = new Array<>(false,frameRows * frameCols, TextureRegion.class);
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                textures.add(tmp[i][j]);
            }
        }

        return textures;
    }

    /**
     * Null-safe findRegion
     */
    public static TextureRegion findRegion(TextureAtlas atlas, String name) {
        TextureAtlas.AtlasRegion region = atlas.findRegion(name);
        if (region == null) {
            throw new RuntimeException("Region not found");
        }
        return region;
    }
}
