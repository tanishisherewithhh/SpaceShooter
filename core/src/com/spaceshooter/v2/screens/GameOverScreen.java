package com.spaceshooter.v2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.spaceshooter.v2.SpaceShooter;

import static com.spaceshooter.v2.SpaceShooter.BOMB_4;
import static com.spaceshooter.v2.SpaceShooter.camera;

public class GameOverScreen extends ScreenAdapter {
    final SpaceShooter game;
    boolean won;

    public GameOverScreen(final SpaceShooter game, boolean won) {
        this.game = game;
        this.won = won;
    }

    @Override
    public void show() {
        super.show();
        BOMB_4.play(0.9f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(SpaceShooter.menuBg, 0, 0, 800, 480);
        if (won) {
            game.font.draw(game.batch, "YOU WON!!!", 320, 400);
        } else {
            game.font.draw(game.batch, "GAME OVER", 300, 400);
            game.smallFont.draw(game.batch, "YOU LOST", 320, 350);
        }
        game.smallFont.draw(game.batch, "Click Anywhere To Restart", 300, 40);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setDefaultValues();
            game.setScreen(game.mainMenuScreen);
        }
    }
}
