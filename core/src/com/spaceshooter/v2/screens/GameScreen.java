package com.spaceshooter.v2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.spaceshooter.v2.Enemy;
import com.spaceshooter.v2.SpaceShooter;

public class GameScreen implements Screen {
    final SpaceShooter game;
    float speed = 25f;
    float spaceY = 0;
    int spaceHeight = 1000;
    ParticleEffect hyperSpaceEffect;

    public GameScreen(final SpaceShooter game) {
        this.game = game;
    }

    @Override
    public void show() {
        game.settingsScreen = new SettingsScreen(game, this);
        game.settingsScreen.show();
        hyperSpaceEffect = new ParticleEffect();
        hyperSpaceEffect.load(Gdx.files.internal("hyperspace.p"), Gdx.files.internal(""));
        hyperSpaceEffect.start();
        hyperSpaceEffect.setPosition(300, 350);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spaceY -= speed * Gdx.graphics.getDeltaTime(); // Update the y-coordinate based on the speed and the time passed since the last frame
        if (spaceY <= -spaceHeight + 480) { // Check if the space image has reached the bottom
            spaceY = -spaceHeight + 480;
        }
        game.batch.setProjectionMatrix(SpaceShooter.camera.combined);
        game.batch.begin();
     //   game.batch.draw(SpaceShooter.space, 0, spaceY, 800, spaceHeight); // Draw the space image with the updated y-coordinate
        hyperSpaceEffect.draw(game.batch,delta);
        SpaceShooter.player.render(game.batch);
        for (Enemy enemy : game.enemies) {
            enemy.render(game.batch);
            enemy.movement();
        }
        game.smallFont.draw(game.batch, "Health: "+SpaceShooter.player.health,5,20);
        game.smallFont.draw(game.batch, "Enemies Killed: "+SpaceShooter.player.enemiesKilled,5,40);

        game.batch.end();

        SpaceShooter.player.movement();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.setScreen(game.settingsScreen);
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}
