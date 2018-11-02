/*
 * Developed by Michel Faria on 11/2/18 5:11 PM.
 * Last modified 11/2/18 5:11 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package io.michelfaria.chrono;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Disposable;

import java.util.Objects;

public class MusicManager implements Disposable {
    private Music music;

    public void setMusic(Music music) {
        stopMusic();
        this.music = music;
    }

    public void playMusic(boolean loop) {
        Objects.requireNonNull(music, "Music not set");
        if (music.isPlaying()) {
            throw new IllegalStateException("Music already playing");
        }
        music.setLooping(loop);
        music.play();
    }

    public void stopMusic() {
        if (music != null) {
            music.stop();
            music.dispose();
            music = null;
        }
    }

    @Override
    public void dispose() {
        if (music != null) {
            stopMusic();
        }
    }
}
