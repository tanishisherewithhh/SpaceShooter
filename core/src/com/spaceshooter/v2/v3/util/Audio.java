package com.spaceshooter.v2.v3.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Audio {
    public static Sound ENEMY_SHOOT;
    public static Sound PLAYER_SHOOT;
    public static Sound ENEMY_DAMAGE;
    public static Sound PLAYER_DAMAGE;
    public static Sound COLLISION;
    public static Sound BOMB_1,BOMB_2,BOMB_3,BOMB_4;
    private static Music[] BACKGROUND_SONGS;

    public static void create() {
        ENEMY_SHOOT = Gdx.audio.newSound(Gdx.files.internal("sounds/enemyShoot.wav"));
        PLAYER_SHOOT = Gdx.audio.newSound(Gdx.files.internal("sounds/playerShoot.wav"));
        ENEMY_DAMAGE = Gdx.audio.newSound(Gdx.files.internal("sounds/enemydamage.wav"));
        PLAYER_DAMAGE = Gdx.audio.newSound(Gdx.files.internal("sounds/playerdamage.wav"));
        COLLISION = Gdx.audio.newSound(Gdx.files.internal("sounds/collisiondamage.wav"));
        BOMB_1 = Gdx.audio.newSound(Gdx.files.internal("sounds/bomb1.wav"));
        BOMB_2 = Gdx.audio.newSound(Gdx.files.internal("sounds/bomb2.wav"));
        BOMB_3 = Gdx.audio.newSound(Gdx.files.internal("sounds/bomb3.wav"));
        BOMB_4 = Gdx.audio.newSound(Gdx.files.internal("sounds/bomb4.wav"));

        Music bg0 = Gdx.audio.newMusic(Gdx.files.internal("sounds/bg0.wav"));
        Music bg1 = Gdx.audio.newMusic(Gdx.files.internal("sounds/bg1.wav"));
        Music bg2 = Gdx.audio.newMusic(Gdx.files.internal("sounds/bg2.wav"));

        final int[] currentSongIndex = {0};

        BACKGROUND_SONGS = new Music[]{bg0, bg1, bg2};

        BACKGROUND_SONGS[currentSongIndex[0]].setLooping(false);
        BACKGROUND_SONGS[currentSongIndex[0]].play();
        BACKGROUND_SONGS[currentSongIndex[0]].setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                // When the current song ends, move to the next one
                currentSongIndex[0] = (currentSongIndex[0] + 1) % BACKGROUND_SONGS.length;
                BACKGROUND_SONGS[currentSongIndex[0]].setLooping(false);
                BACKGROUND_SONGS[currentSongIndex[0]].play();
                BACKGROUND_SONGS[currentSongIndex[0]].setOnCompletionListener(this);  // Set the listener on the next song
            }
        });
    }

    public static void dispose(){
        ENEMY_SHOOT.dispose();
        PLAYER_SHOOT.dispose();
        COLLISION.dispose();
        BOMB_1.dispose();
        BOMB_2.dispose();
        BOMB_3.dispose();
        BOMB_4.dispose();
        ENEMY_DAMAGE.dispose();
        PLAYER_DAMAGE.dispose();
    }

    public static Music[] getBackgroundSongs() {
        return BACKGROUND_SONGS;
    }
}
