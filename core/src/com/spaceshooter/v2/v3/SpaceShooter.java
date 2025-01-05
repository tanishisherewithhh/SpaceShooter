package com.spaceshooter.v2.v3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spaceshooter.v2.v3.gameEntity.entities.Player;
import com.spaceshooter.v2.v3.util.Audio;
import com.spaceshooter.v2.v3.util.Fonts;
import com.spaceshooter.v2.v3.util.Settings;
import com.spaceshooter.v2.v3.util.Textures;

public class SpaceShooter extends Game {
    public static SpaceShooter INSTANCE = new SpaceShooter();
    public OrthographicCamera CAMERA;
    public SpriteBatch DRAWER;
    public Player mainPlayer;
    public MainGame gameInstance;
    public static int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 480;

    private SpaceShooter(){}

    @Override
    public void create() {
        gameInstance = new MainGame();

        Textures.create();
        Audio.create();
        Fonts.create();

        DRAWER = new SpriteBatch();

        CAMERA = new OrthographicCamera();
        CAMERA.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);

        mainPlayer = new Player();

        gameInstance.spawn(mainPlayer);
        gameInstance.createWaveManager();
    }

    @Override
    public void render() {
        super.render();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        CAMERA.update();

        gameInstance.update(Settings.SPEED_MULTIPLIER * Gdx.graphics.getDeltaTime());

        DRAWER.begin();
        gameInstance.renderGameEntities(DRAWER,Settings.SPEED_MULTIPLIER);
        DRAWER.end();

        gameInstance.simulateMovement();
        gameInstance.checkEntityBounds();

        for (Music song : Audio.getBackgroundSongs()) {
            song.setVolume(Settings.MUSIC_VOLUME);
        }
    }

    @Override
    public void dispose() {
        Audio.dispose();
        Fonts.dispose();
        Textures.dispose();
        DRAWER.dispose();
    }

    public static OrthographicCamera getCamera() {
        return INSTANCE.CAMERA;
    }
    public static MainGame getGame(){
        return INSTANCE.gameInstance;
    }
}
