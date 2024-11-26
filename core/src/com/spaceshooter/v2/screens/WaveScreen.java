package com.spaceshooter.v2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.spaceshooter.v2.SpaceShooter;

public class WaveScreen extends ScreenAdapter {
    SpaceShooter game;
    int wave;

    public WaveScreen(SpaceShooter game, int wave) {
        this.game = game;
        this.wave = wave;
    }

    @Override
    public void show() {
        super.show();
        SpaceShooter.player.setDefaultPosition();
        if (game.currentWave == SpaceShooter.no_of_waves && !SpaceShooter.infiniteWaves) {
            game.initiateBossWave();
        } else {
            game.spawnEnemies(game.currentWave);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
        }
        game.batch.begin();
        if (game.currentWave == SpaceShooter.no_of_waves && !SpaceShooter.infiniteWaves) {
            game.font.draw(game.batch, "FINAL WAVE APPROACHING", 255, 400);

        } else {
            game.font.draw(game.batch, "WAVE " + (wave - 1) + " COMPLETED", 275, 400);
        }
        game.smallFont.draw(game.batch, "Click Anywhere To Continue to Next Wave", 250, 40);
        game.batch.end();
    }
}
