package com.spaceshooter.v2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.spaceshooter.v2.screens.GameOverScreen;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BossAlien extends Enemy {
    final SpaceShooter game;
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();


    public BossAlien(Texture objectTexture, final SpaceShooter game) {
        super(objectTexture, 10);
        this.objectTexture = SpaceShooter.bossImage;
        this.objectTextureRegion = new TextureRegion(this.objectTexture);
        this.damage = 10;
        this.game = game;
        setSize(150, 150);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (health <= 0) {
            selfDestruct = true;
            game.enemies.forEach(enemy -> enemy.selfDestruct = true);
            game.enemies.clear();
            game.currentWave = -1;
            executor.schedule(()->game.setScreen(new GameOverScreen(game, true)), 3, TimeUnit.SECONDS);
            executor.shutdown();
        }
        super.render(batch);
    }

    @Override
    public void shoot() {
        if (shootTimer >= 0.4f) {
            Bullet bullet1 = new Bullet(SpaceShooter.enemyBullet);
            Bullet bullet2 = new Bullet(SpaceShooter.enemyBullet);
            bullet1.setSize(24, 38);
            bullet1.setPosition(x + 1, y + 7);
            bullet1.setAngle(-1.61f);

            bullet2.setSize(24, 38);
            bullet2.setPosition(x + width - 2, y + 7);
            bullet2.setAngle(-1.61f);

            bullets.add(bullet1);
            SpaceShooter.ENEMY_SHOOT.play(SpaceShooter.getSoundVolume());
            bullets.add(bullet2);
            SpaceShooter.ENEMY_SHOOT.play(SpaceShooter.getSoundVolume());

            shootTimer = 0f;
        }
        bullets.removeIf(bullet -> (bullet.x > 800 || bullet.x < 0) && (bullet.y < 0 || bullet.y > 480));
    }
}
