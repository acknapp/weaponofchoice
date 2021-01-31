package com.weaponofchoice.game.util;

public class Constants {
    // Levels
    public static final String LEVEL_MAP_NAME = "WoCroom1";
    public static final String LEVEL_MAP = "levels/WoCroom1/" + LEVEL_MAP_NAME + ".tmx";

    // Music
    public static final String LEVEL_MUSIC_NAME = "GGJAM - CHILL 1"; // Change this to the song name
    public static final String MUSIC = "music/" + LEVEL_MUSIC_NAME + ".mp3";

    // Player
    public static final String PLAYER_STARTING_SPRITE = "images/prepacked/Idle_Front.png"; // TODO: Get this from the atlas
    public static final String PLAYER_MOVE_UP_SPRITE = "images/prepacked/Walk_Back_Anim.png";
    public static final String PLAYER_MOVE_DOWN_SPRITE = "images/prepacked/Walk_Front_Anim.png";
//    public static final String PLAYER_MOVE_RIGHT_SPRITE = "images/prepacked/Walk_Right_Anim.png";
    public static final String PLAYER_MOVE_LEFT_SPRITE = "images/prepacked/Walk_Side_Anim.png";
    public static final int PLAYER_MOVE_SPEED = 100;

    // Enemy
    public static final String ENEMY_STARTING_SPRITE = "images/prepacked/Idle_Slime.png";
}
