/*
 * Developed by Michel Faria on 10/29/18 7:20 PM.
 * Last modified 10/29/18 7:20 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
    public static final AssetDescriptor<TextureAtlas> CHRONO_ATLAS = new AssetDescriptor<>("textures/chrono.atlas", TextureAtlas.class);
    public static final AssetDescriptor<Texture> DELTA_STORM = new AssetDescriptor<>("textures/delta-storm.png", Texture.class);
    public static final AssetDescriptor<BitmapFont> FONT = new AssetDescriptor<>("font.fnt", BitmapFont.class);
    public static final String EXAMPLE_TMX = "maps/example.tmx";
}
