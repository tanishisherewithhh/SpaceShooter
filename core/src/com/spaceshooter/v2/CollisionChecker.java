package com.spaceshooter.v2;

import com.spaceshooter.v2.screens.GameOverScreen;

import static com.spaceshooter.v2.SpaceShooter.*;

public class CollisionChecker {
    final SpaceShooter game;

    public CollisionChecker(final SpaceShooter game) {
        this.game = game;
    }

    private void EnemyCollidesWithPlayerCheck(Enemy enemy){
        if (enemy.collides(player)) {
            game.enemies.remove(enemy);
            player.enemiesKilled++;
            player.health -= enemy.damage;
            PLAYER_DAMAGE.play(getSoundVolume());
            ENEMY_DAMAGE.play(getSoundVolume());
            BOMB_3.play(getSoundVolume());
        }
    }
    private void PlayerHealthCheck(){
        if (player.health <= 0) {
            game.setScreen(new GameOverScreen(game, false));
            BOMB_1.play(0.9f);
        }
    }
    private void BulletCollidesPlayerCheck(Bullet bullet, Enemy enemy){
        if (bullet.collides(player)) {
            enemy.bullets.remove(bullet);
            player.health = player.health - bullet.damage;
            PLAYER_DAMAGE.play(getSoundVolume());
        }
    }
    private void BulletCollidesEnemyCheck(Bullet bullet, Enemy enemy){
        if (enemy.collides(bullet)) {
            player.bullets.remove(bullet);
            enemy.health -= bullet.damage;
            ENEMY_DAMAGE.play(getSoundVolume());
            if (enemy.health <= 0) {
                enemy.selfDestruct = true;
                if (enemy instanceof BossAlien) {
                    BossAlien alien = (BossAlien) enemy;
                    game.batch.begin();
                    alien.render(game.batch);
                    game.batch.end();
                }
                game.enemies.remove(enemy);
                BOMB_1.play(getSoundVolume());
                player.enemiesKilled++;
            }
        }
    }

    public void checkCollisions(){
        for(Enemy enemy: game.enemies){
            for(Bullet bullet1: enemy.bullets){
                BulletCollidesPlayerCheck(bullet1,enemy);

                for(Bullet bullet: player.bullets){
                    if (bullet1.collides(bullet)) {
                        enemy.bullets.remove(bullet1);
                        player.bullets.remove(bullet);
                    }
                    BulletCollidesEnemyCheck(bullet,enemy);
                }
            }
            EnemyCollidesWithPlayerCheck(enemy);
            PlayerHealthCheck();
        }
    }
}
