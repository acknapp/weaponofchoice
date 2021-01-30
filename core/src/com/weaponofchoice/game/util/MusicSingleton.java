package com.weaponofchoice.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicSingleton {
    private static final String TAG = MusicSingleton.class.getName();
    private static Music song;


    public MusicSingleton(String musicFilePath) {
        this.song = Gdx.audio.newMusic(Gdx.files.internal(musicFilePath));
    }

    public static void playSong() {
        song.play();
    }

    public static void stopSong() {
        // Gdx.app.log("SONG", "Stopping song.");
        song.stop();
    }

    public static void setSongVolume(float volumeLevel) {
        // Gdx.app.log("SONG", "Setting song to volume: " + String.valueOf(volumeLevel));
        song.setVolume(volumeLevel);
    }


    public static void disposeSong() {
        // Gdx.app.log("SONG", "Disposing Song");
        song.dispose();
    }
}