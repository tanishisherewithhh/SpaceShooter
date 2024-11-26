package com.spaceshooter.v2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Enemy extends GameObject {

    public List<Bullet> bullets = new CopyOnWriteArrayList<>();
    public boolean selfDestruct = false;
    protected float shootTimer = 0;
    int enemyLevel;
    int wave = 1;

    public Enemy(Texture objectTexture, int enemyLevel) {
        super(objectTexture, 3 + enemyLevel, 5 + 2 * enemyLevel);
        switch (enemyLevel) {
            case 2:
                this.objectTexture = SpaceShooter.mid_enemy;
                break;
            case 3:
                this.objectTexture = SpaceShooter.strong_enemy;
                break;
            default:
                this.objectTexture = SpaceShooter.basic_enemy;
        }
        this.objectTextureRegion = new TextureRegion(objectTexture);
        this.enemyLevel = enemyLevel;
        setSize(32, 32);
    }


    public Enemy(Texture objectTexture, int movementSpeed, int enemyLevel) {
        super(objectTexture, movementSpeed, 3 + enemyLevel, 5 + 2 * enemyLevel);
        switch (enemyLevel) {
            case 2:
                this.objectTexture = SpaceShooter.mid_enemy;
                break;
            case 3:
                this.objectTexture = SpaceShooter.strong_enemy;
                break;
            default:
                this.objectTexture = SpaceShooter.basic_enemy;
        }
        this.objectTextureRegion = new TextureRegion(objectTexture);
        setSize(32, 32);
        this.enemyLevel = enemyLevel;
    }

    public Enemy(Texture objectTexture, int x, int y, int enemyLevel) {
        super(objectTexture, 3, 3 + enemyLevel, 5 + 2 * enemyLevel);
        switch (enemyLevel) {
            case 2:
                this.objectTexture = SpaceShooter.mid_enemy;
                break;
            case 3:
                this.objectTexture = SpaceShooter.strong_enemy;
                break;
            default:
                this.objectTexture = SpaceShooter.basic_enemy;
        }
        this.objectTextureRegion = new TextureRegion(objectTexture);
        setSize(32, 32);
        setPosition(x, y);
        this.enemyLevel = enemyLevel;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (selfDestruct) {
            this.health = 0;
            this.bullets.clear();
            SpaceShooter.BOMB_2.play(0.7f);
            this.setPosition(-20,-20);
        }
        else{
            batch.draw(objectTexture, x, y, width, height);
            shootTimer += Gdx.graphics.getDeltaTime();
            shoot();
            bullets.forEach(bullet -> {
                bullet.move(3);
                batch.draw(bullet.objectTextureRegion, bullet.x, bullet.y, bullet.width / 2, (float) bullet.height / 2, bullet.width, bullet.height, 1, 1, (float) (bullet.angle * 180 / Math.PI) - 90);
            });
        }
    }

    @Override
    public void movement() {
        move(0, 0);
    }

    public void shoot() {
        Random rand = new Random();
        if (shootTimer >= 1.0f / enemyLevel && rand.nextFloat(16) < 1.0f / enemyLevel) {
            Bullet bullet = new Bullet(SpaceShooter.enemyBullet);
            bullet.setSize(16, 36);
            bullet.setPosition(x + width / 2 - 6, y + 6);
            bullet.setAngle(-1.61f);
            shootTimer = 0f;
            bullets.add(bullet);
            SpaceShooter.ENEMY_SHOOT.play(SpaceShooter.getSoundVolume());
        }
        bullets.removeIf(bullet -> (bullet.x > 800 || bullet.x < 0) && (bullet.y < 0 || bullet.y > 480));
    }
   /* public void shoot() {
        Random rand = new Random();
        if (rand.nextFloat(100) < shootProbability && !destroyAllEnemies) {
            enemyBullets.add(new Bullet(x + width / 2, y + height, 3));
            SoundUtils.playSound( SpaceShooter.class.getResourceAsStream("/sounds/enemyShoot.wav"));
        }
    }

    */

    @Override
    public void move(float dx, float dy) {
        Random rand = new Random();
        dx = rand.nextInt(3) - 1; // random number between -1 and 1
        dy = rand.nextInt(3) - 1; // random number between -1 and 1
        if (x < 5 || x > 800 - width)
            dx = -dx; // change direction if the enemy hits the edge of the frame
        if (y < 10 || y > 480 - height)
            dy = -dy; // change direction if the enemy hits the edge of the frame

        super.move(dx, dy);
    }

    public int getWave() {
        return wave;
    }

    public void setWave(int wave) {
        this.wave = wave;
    }
}
