package com.spaceshooter.v2.v3.util;

import com.badlogic.gdx.graphics.Texture;

public class Textures {
    public static Texture BOSS_IMAGE;
    public static Texture ENEMY1;
    public static Texture ENEMY2;
    public static Texture ENEMY3;
    public static Texture SPACE_SHIP_PLAYER;
    public static Texture SPACE;
    public static Texture MENU_BG,MENU_BG_2;
    public static Texture ENEMY_BULLET;
    public static Texture PLAYER_BULLET;

    public static void create(){
        SPACE_SHIP_PLAYER = new Texture("images/spaceship.png");
        BOSS_IMAGE = new Texture("images/bossspaceship.png");
        ENEMY1 = new Texture("images/enemy1.png");
        ENEMY2 = new Texture("images/enemy2.png");
        ENEMY3 = new Texture("images/enemy3.png");
        SPACE = new Texture("images/space.png");
        MENU_BG = new Texture("images/mainmenubg.jpg");
        MENU_BG_2 = new Texture("images/mainmenubg2.jpg");
        ENEMY_BULLET = new Texture("images/enemybullet.png");
        PLAYER_BULLET = new Texture("images/playerbullet.png");
    }

    public static void dispose() {
        BOSS_IMAGE.dispose();
        MENU_BG.dispose();
        MENU_BG_2.dispose();
        ENEMY1.dispose();
        ENEMY2.dispose();
        ENEMY3.dispose();
        SPACE_SHIP_PLAYER.dispose();
        SPACE.dispose();
        ENEMY_BULLET.dispose();
        PLAYER_BULLET.dispose();
    }
}
