package com.spaceshooter.v2.v3.gameEntity.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.spaceshooter.v2.v3.SpaceShooter;
import com.spaceshooter.v2.v3.util.Audio;
import com.spaceshooter.v2.v3.util.Settings;
import com.spaceshooter.v2.v3.util.Textures;

public class Enemy extends CharacterEntity {
    private final EnemyType type;
    private float shootTimer = 0;
    private Vector2 movementOffset;
    private static final float MOVEMENT_SPEED = 20f; // Speed of random movement
    private static final float SHOOT_INTERVAL = 3; // Time between shots
    private float targetX, targetY;
    private boolean isMovingToTarget = false;

    @Override
    public void movement() {
        float deltaTime = Gdx.graphics.getDeltaTime() * speedMultiplier;

        if (isMovingToTarget) {
            // Move towards the target position
            float dx = targetX - x;
            float dy = targetY - y;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance > 0.5) {
               this.x += (dx / distance) * movementSpeed * deltaTime;
               this.y += (dy / distance) * movementSpeed * deltaTime;
            } else {
                isMovingToTarget = false; // Stop moving when close to the target
            }
        } else {
            // Generate smooth, random movement with more varied vertical movement
            movementOffset.x += MathUtils.random(-MOVEMENT_SPEED, MOVEMENT_SPEED) * deltaTime;
            movementOffset.y += MathUtils.random(-MOVEMENT_SPEED * 0.5f, MOVEMENT_SPEED * 0.5f) * deltaTime;

            // Apply some natural damping to prevent extreme movements
            movementOffset.scl(0.95f);

            // Update position based on offset
            x += movementOffset.x;
            y += movementOffset.y;
        }

        // Soft screen boundary handling with more dynamic vertical range
        float padding = 10; // Padding from screen edges

        x = MathUtils.clamp(x, padding, SpaceShooter.WINDOW_WIDTH - width - padding);
        y = MathUtils.clamp(y, padding, SpaceShooter.WINDOW_HEIGHT - padding);
    }

    public enum EnemyType {
        BASIC(new TextureRegion(Textures.ENEMY1), 10, 2, 75, 8),
        MEDIUM(new TextureRegion(Textures.ENEMY2), 20, 3, 75, 8),
        ADVANCED(new TextureRegion(Textures.ENEMY3), 30, 4, 100, 12);

        private final TextureRegion texture;
        private final int health;
        private final int attackDamage;
        private final int movementSpeed;
        private final int scoreValue;

        EnemyType(TextureRegion texture, int health, int attackDamage,
                  int movementSpeed, int scoreValue) {
            this.texture = texture;
            this.health = health;
            this.attackDamage = attackDamage;
            this.movementSpeed = movementSpeed;
            this.scoreValue = scoreValue;
        }

        public Enemy create() {
            return new Enemy(this);
        }
    }

    private Enemy(EnemyType type) {
        super(type.health, type.movementSpeed, type.attackDamage);
        this.type = type;
        setSize(32, 32);
        if(x == 0 && y == 0) {
            setPosition(SpaceShooter.WINDOW_WIDTH / 2, SpaceShooter.WINDOW_HEIGHT / 2);
        }
        this.movementOffset = new Vector2(0, 0);
    }

    @Override
    public void render(SpriteBatch batch, float speedMul) {
        this.speedMultiplier = speedMul;

        // Draw enemy
        batch.draw(type.texture, x, y, width, height);

        // Update movement and shooting
        shoot();
    }

    private void shoot() {
        shootTimer += Gdx.graphics.getDeltaTime() * speedMultiplier;

        // Define a base shoot interval and a variation range
        float randomVariation = MathUtils.random(-0.5f, 0.5f); // Random variation between -0.5 and 0.5 seconds
        float shootInterval = SHOOT_INTERVAL + randomVariation; // Adjusted shoot interval

        // Shoot periodically
        if (shootTimer >= shootInterval) {
            // Create a bullet that moves downwards
            Bullet bullet = new Bullet(type.attackDamage, (float) (-Math.PI / 2.0f), Bullet.ParentShooter.ENEMY);
            bullet.setPosition(x + width / 2 - bullet.getWidth() / 2, y); // Center the bullet on the enemy
            SpaceShooter.getGame().spawn(bullet);

            shootTimer = 0f;
            Audio.ENEMY_SHOOT.play(Settings.SOUND_VOLUME);
        }
    }

    public void setTargetPosition(float targetX, float targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.isMovingToTarget = true;
    }

    public static Enemy spawnRandomEnemy() {
        EnemyType[] types = EnemyType.values();
        EnemyType randomType = types[MathUtils.random(types.length - 1)];
        return randomType.create();
    }

    // Getter for score value
    public int getScoreValue() {
        return type.scoreValue;
    }
}