package io.michelfaria.chrono.util;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import io.michelfaria.chrono.values.TxRegs;

public final class TextureTools {

	private TextureAtlas atlas;

	public TextureTools(TextureAtlas atlas) {
		this.atlas = atlas;
	}

	public Animation<?> makeAnimation(TxRegs txReg) {
//		return new Animation<>(txReg.speed, splitTextureRegion(txReg), PlayMode.LOOP);
		Array<TextureRegion> textureRegions = splitTextureRegion(txReg);
		if (txReg.assembly == null) {
			return new Animation<>(txReg.speed, textureRegions, PlayMode.LOOP);
			
		} else {
			Array<TextureRegion> assembled = new Array<>();
			for (int assemblyNum : txReg.assembly) {
				if (assemblyNum > textureRegions.size) {
					throw new ArrayIndexOutOfBoundsException(assemblyNum);
				}
				assembled.add(textureRegions.get(assemblyNum));
			}
			assert assembled.size > 0;
			return new Animation<>(txReg.speed, assembled, PlayMode.LOOP);
		}
	}

	/**
	 * Splits a texture sequence (spritesheet) into individual TextureRegions in a
	 * 1-dimension array. The textures are ordered left-to-right, up-to-down.
	 *
	 * @param txReg Texture region to split
	 */
	public Array<TextureRegion> splitTextureRegion(TxRegs txReg) {
		return splitTextureRegion(txReg.regionName, txReg.columns, txReg.rows);
	}

	/**
	 * Splits a texture sequence (spritesheet) into individual TextureRegions in a
	 * 1-dimension array. The textures are ordered left-to-right, up-to-down.
	 *
	 * @param atlas      Atlas containing texture regions
	 * @param regionName Name of the atlas region
	 * @param frameCols  Amount of columns in animation
	 * @param frameRows  Amount of rows in animation
	 */
	public Array<TextureRegion> splitTextureRegion(String regionName, int frameCols,
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
	public Array<TextureRegion> splitTextureRegion(TextureRegion reg, int frameCols,
			int frameRows) {
		// Use the split utility method to create a 2D array of TextureRegions.
		TextureRegion[][] tmp = reg.split(reg.getRegionWidth() / frameCols,
				reg.getRegionHeight() / frameRows);

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
	public TextureRegion findRegion(String name) {
		TextureAtlas.AtlasRegion region = atlas.findRegion(name);
		if (region == null) {
			throw new RuntimeException("Region not found");
		}
		return region;
	}
}
