package com.spaceshooter.v2;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spaceshooter.v2.movement.MovementType;
import com.spaceshooter.v2.screens.GameOverScreen;
import com.spaceshooter.v2.screens.MainMenuScreen;
import com.spaceshooter.v2.screens.SettingsScreen;
import com.spaceshooter.v2.screens.WaveScreen;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SpaceShooter extends Game {
    public static Texture bossImage;
    public static Texture basic_enemy;
    public static Texture mid_enemy;
    public static Texture strong_enemy;
    public static Texture spaceShip;
    public static Texture space;
    public static Texture menuBg, menuBg2;
    public static Texture enemyBullet;
    public static Texture playerBullet;
    public static OrthographicCamera camera;
    public static SpaceShip player;
    public static MovementType movementType;
    public static int enemy_difficulty, no_of_waves = 3;
    public static boolean infiniteWaves = false;
    public SpriteBatch batch;
    public List<Enemy> enemies;
    public float musicVolume = 0.7f;
    public static float soundVolume = 1.0f;
    public SettingsScreen settingsScreen;
    public MainMenuScreen mainMenuScreen;
    public int currentWave = 1;
    public BitmapFont font, smallFont;
    Music[] songs;
    private Music bg0, bg1, bg2;
    public static Sound ENEMY_SHOOT, PLAYER_SHOOT, ENEMY_DAMAGE,PLAYER_DAMAGE,COLLISION,BOMB_1,BOMB_2,BOMB_3,BOMB_4;
    public CollisionChecker collisionCheck;

    public void createTextures() {
        spaceShip = new Texture("images/spaceship.png");
        bossImage = new Texture("images/bossspaceship.png");
        basic_enemy = new Texture("images/enemy1.png");
        mid_enemy = new Texture("images/enemy2.png");
        strong_enemy = new Texture("images/enemy3.png");
        space = new Texture("images/space.png");
        menuBg = new Texture("images/mainmenubg.jpg");
        menuBg2 = new Texture("images/mainmenubg2.jpg");
        enemyBullet = new Texture("images/enemybullet.png");
        playerBullet = new Texture("images/playerbullet.png");
    }
    public void initialiseSoundEffects() {
        ENEMY_SHOOT = Gdx.audio.newSound(Gdx.files.internal("sounds/enemyShoot.wav"));
        PLAYER_SHOOT = Gdx.audio.newSound(Gdx.files.internal("sounds/playerShoot.wav"));
        ENEMY_DAMAGE = Gdx.audio.newSound(Gdx.files.internal("sounds/enemydamage.wav"));
        PLAYER_DAMAGE = Gdx.audio.newSound(Gdx.files.internal("sounds/playerdamage.wav"));
        COLLISION = Gdx.audio.newSound(Gdx.files.internal("sounds/collisiondamage.wav"));
        BOMB_1 = Gdx.audio.newSound(Gdx.files.internal("sounds/bomb1.wav"));
        BOMB_2 = Gdx.audio.newSound(Gdx.files.internal("sounds/bomb2.wav"));
        BOMB_3 = Gdx.audio.newSound(Gdx.files.internal("sounds/bomb3.wav"));
        BOMB_4 = Gdx.audio.newSound(Gdx.files.internal("sounds/bomb4.wav"));

        bg0 = Gdx.audio.newMusic(Gdx.files.internal("sounds/bg0.wav"));
        bg1 = Gdx.audio.newMusic(Gdx.files.internal("sounds/bg1.wav"));
        bg2 = Gdx.audio.newMusic(Gdx.files.internal("sounds/bg2.wav"));

        final int[] currentSongIndex = {0};

        songs = new Music[]{bg0, bg1, bg2};

        songs[currentSongIndex[0]].setLooping(false);
        songs[currentSongIndex[0]].play();
        songs[currentSongIndex[0]].setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                // When the current song ends, move to the next one
                currentSongIndex[0] = (currentSongIndex[0] + 1) % songs.length;
                songs[currentSongIndex[0]].setLooping(false);
                songs[currentSongIndex[0]].play();
                songs[currentSongIndex[0]].setOnCompletionListener(this);  // Set the listener on the next song
            }
        });
    }

    public void disposeTextures() {
        bossImage.dispose();
        spaceShip.dispose();
        space.dispose();
        strong_enemy.dispose();
        basic_enemy.dispose();
        mid_enemy.dispose();
        enemyBullet.dispose();
        playerBullet.dispose();
        font.dispose();
        smallFont.dispose();
        menuBg.dispose();
        menuBg2.dispose();
    }

    public void disposeSound(){
        ENEMY_SHOOT.dispose();
        PLAYER_SHOOT.dispose();
        COLLISION.dispose();
        BOMB_1.dispose();
        BOMB_2.dispose();
        BOMB_3.dispose();
        BOMB_4.dispose();
        ENEMY_DAMAGE.dispose();
        PLAYER_DAMAGE.dispose();
        bg0.dispose();
        bg1.dispose();
        bg2.dispose();
    }

    @Override
    public void create() {
        font = new BitmapFont(Gdx.files.internal("comicSans.fnt"));
        smallFont = new BitmapFont(Gdx.files.internal("smallComicSans.fnt"));
        mainMenuScreen = new MainMenuScreen(this);
        settingsScreen = new SettingsScreen(this, mainMenuScreen);
        batch = new SpriteBatch();

        createTextures();
        initialiseSoundEffects();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        player = new SpaceShip(spaceShip);
        enemies = new CopyOnWriteArrayList<>();

        this.setScreen(settingsScreen);
        this.setScreen(mainMenuScreen);

        spawnEnemies(currentWave);

        collisionCheck = new CollisionChecker(this);
    }

    public void setDefaultValues() {
        currentWave = 1;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        player = new SpaceShip(spaceShip);
        enemies = new CopyOnWriteArrayList<>();
        spawnEnemies(currentWave);
    }

    public void spawnEnemies(int wave) {
        // Clear any existing enemies
        enemies.clear();

        // Spawn new enemies based on the wave number
        for (int i = 1; i <= wave; i++) {
            if (wave == 1) {
                for (int j = 0; j < 2; j++) {
                    enemies.add(new Enemy(SpaceShooter.basic_enemy, 200 + 100 * j, 400 + i, 1));
                }
            }
            enemies.add(new Enemy(SpaceShooter.basic_enemy, 200 + 50 * i, 400 + i, 1));
            if (wave >= 2) {
                enemies.add(new Enemy(SpaceShooter.mid_enemy, 200 + 50 * i, 300 + i, 2));
            }

            if (wave >= 3) {
                enemies.add(new Enemy(SpaceShooter.strong_enemy, 200 + 50 * i, 200 + i, 3));
            }
        }
    }

    public void nextWave() {
        currentWave++;
        player.bullets.clear();
        setScreen(new WaveScreen(this, currentWave));
    }

    public void initiateBossWave() {
        enemies.clear();
        BossAlien bossEnemy = new BossAlien(bossImage, this);
        bossEnemy.setPosition(400 - 64, 300);
        enemies.add(bossEnemy);
        for (int i = 1; i <= 2; i++) {
            enemies.add(new Enemy(SpaceShooter.basic_enemy, 200 + 100 * i, 400 + i, 1));
            enemies.add(new Enemy(SpaceShooter.basic_enemy, 200 + 50 * i, 400 + i, 1));
            enemies.add(new Enemy(SpaceShooter.mid_enemy, 200 + 50 * i, 300 + i, 2));
            enemies.add(new Enemy(SpaceShooter.strong_enemy, 200 + 50 * i, 200 + i, 3));

            enemies.add(new Enemy(SpaceShooter.basic_enemy, 500 + 100 * i, 400 + i, 1));
            enemies.add(new Enemy(SpaceShooter.basic_enemy, 500 + 50 * i, 400 + i, 1));
            enemies.add(new Enemy(SpaceShooter.mid_enemy, 500 + 50 * i, 300 + i, 2));
            enemies.add(new Enemy(SpaceShooter.strong_enemy, 500 + 50 * i, 200 + i, 3));
        }
    }

    @Override
    public void render() {
        super.render();
        if (enemies.isEmpty() && currentWave != -1) {
            if (currentWave <= no_of_waves || infiniteWaves) {
                if (!infiniteWaves && currentWave == no_of_waves)
                    initiateBossWave();
                else
                    nextWave();
            }
        }

        camera.update();

        for (Music song : songs) {
            song.setVolume(getMusicVolume());
        }

        collisionCheck.checkCollisions();
    }

    @Override
    public void dispose() {
        batch.dispose();
        disposeTextures();
        disposeSound();
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = musicVolume;
    }

    public static float getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(float soundVolume) {
        SpaceShooter.soundVolume = soundVolume;
    }
}
