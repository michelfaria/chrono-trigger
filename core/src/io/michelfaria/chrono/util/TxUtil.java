package io.michelfaria.chrono.util;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public final class TxUtil {
	/**
	 * Splits a texture sequence (spritesheet) into individual TextureRegions in a
	 * 1-dimension array. The textures are ordered left-to-right, up-to-down.
	 *
	 * @param atlas      Atlas containing texture regions
	 * @param regionName Name of the atlas region
	 * @param frameCols  Amount of columns in animation
	 * @param frameRows  Amount of rows in animation
	 */
	public static Array<TextureRegion> splitTextureRegion(TextureAtlas atlas, String regionName, int frameCols,
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
	 *
	 * @param reg       The region to split
	 * @param frameCols Amount of columns in animation
	 * @param frameRows Amount of rows in animation
	 */
	public static Array<TextureRegion> splitTextureRegion(TextureRegion reg, int frameCols, int frameRows) {
		// Use the split utility method to create a 2D array of TextureRegions.
		TextureRegion[][] tmp = reg.split(reg.getRegionWidth() / frameCols, reg.getRegionHeight() / frameRows);

		// Place the regions into a 1D array in the correct order, starting from the top
		// left, going across first. The Animation constructor requires a 1D array.
		Array<TextureRegion> textures = new Array<TextureRegion>(frameRows * frameCols);
		for (int i = 0; i < frameRows; i++) {
			for (int j = 0; j < frameCols; j++) {
				textures.add(tmp[i][j]);
			}
		}

		return textures;
	}

	/**
	 * Null-safe find-region
	 */
	public static TextureRegion findRegion(TextureAtlas atlas, String name) {
		TextureAtlas.AtlasRegion region = atlas.findRegion(name);
		if (region == null) {
			throw new RuntimeException("Region not found");
		}
		return region;
	}
}
