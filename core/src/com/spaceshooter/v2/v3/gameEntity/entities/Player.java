package com.spaceshooter.v2.v3.gameEntity.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.spaceshooter.v2.SpaceShooter;
import com.spaceshooter.v2.movement.MovementType;
import com.spaceshooter.v2.v3.util.Audio;
import com.spaceshooter.v2.v3.util.Settings;
import com.spaceshooter.v2.v3.util.Textures;

public class Player extends CharacterEntity  {
    private static final TextureRegion textureRegion = new TextureRegion(Textures.SPACE_SHIP_PLAYER);
    private final Vector3 mousePos = new Vector3();
    private float shootTimer = 0;
    private int weaponLevel = 1;

    public Player() {
        super(20,5,3);
        textureRegion.setTexture(Textures.SPACE_SHIP_PLAYER);
        setDefaultPosition();
    }

    @Override
    public void render(SpriteBatch batch, float speedMul) {
        this.speedMultiplier = speedMul;

        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        com.spaceshooter.v2.v3.SpaceShooter.getCamera().unproject(mousePos);

        float dx = mousePos.x - (x + width / 2);
        float dy = mousePos.y - (y + height / 2);
        float angle = (float) ((Math.atan2(dy, dx) * 180 / Math.PI) - 90);

        // Rotate the spaceship's sprite to face the mouse
        batch.draw(textureRegion, x, y, width / 2, height / 2, width, height, 1, 1, angle);

        movement();

        shootTimer += calculateSpeedMultiplier(speedMul) / 10;

        shoot((float) Math.atan2(dy, dx));
    }

    public void move(double dx, double dy) {
        this.x += (float) dx;
        this.y += (float) dy;
    }

    @Override
    public void movement() {
        float speedMulEffect = calculateSpeedMultiplier(speedMultiplier);

        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && Settings.MOVEMENT_MODE == MovementType.Mouse) {
            float targetX = mousePos.x - width / 2;
            float targetY = mousePos.y - height / 2;
//            float t = 0.046f; // control the speed of glide
            float lerpedX = Interpolation.linear.apply(x, targetX, speedMulEffect / (4 * getMovementSpeed()));
            float lerpedY = Interpolation.linear.apply(y, targetY, speedMulEffect / (4 * getMovementSpeed()));
            this.x = lerpedX;
            this.y = lerpedY;
        } else if (Settings.MOVEMENT_MODE != MovementType.Mouse) {
            float finalMovementSpeed = speedMulEffect * getMovementSpeed();
            // Calculate direction to mouse
            float dx = mousePos.x - (x + width / 2);
            float dy = mousePos.y - (y + height / 2);

            float length = (float) Math.sqrt(dx * dx + dy * dy);

            if (length > 5) {
                float normalizedDx = dx / length;
                float normalizedDy = dy / length;

                // Movement based on key presses, using mouse direction
                if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                    move(normalizedDx * finalMovementSpeed, normalizedDy * finalMovementSpeed);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                    move(-normalizedDx * finalMovementSpeed, -normalizedDy * finalMovementSpeed);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                    // Perpendicular movement to the right
                    move(-normalizedDy * finalMovementSpeed, normalizedDx * finalMovementSpeed);
                }
                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                    // Perpendicular movement to the left
                    move(normalizedDy * finalMovementSpeed, -normalizedDx * finalMovementSpeed);
                }
            }
        }

        this.x = MathUtils.clamp(this.x,2, com.spaceshooter.v2.v3.SpaceShooter.WINDOW_WIDTH - width - 2);
        this.y = MathUtils.clamp(this.y,2, com.spaceshooter.v2.v3.SpaceShooter.WINDOW_HEIGHT - health - 18);
    }

    public void shoot(float angle) {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && shootTimer >= 0.4f) {
            switch(weaponLevel) {
                case 1:
                    // Single bullet (existing implementation)
                    Bullet singleBullet = new Bullet(3, angle, Bullet.ParentShooter.PLAYER);
                    singleBullet.setPosition(x + width / 2 - 6, y + 15);
                    com.spaceshooter.v2.v3.SpaceShooter.getGame().spawn(singleBullet);
                    break;
                case 2:
                    // Two bullets side by side
                    float offsetX = 10f; // Horizontal offset between bullets

                    // Left bullet
                    Bullet leftBullet = new Bullet(3, angle, Bullet.ParentShooter.PLAYER);
                    leftBullet.setPosition(x + width / 2 - 6 - offsetX, y + 15);

                    // Right bullet
                    Bullet rightBullet = new Bullet(3, angle, Bullet.ParentShooter.PLAYER);
                    rightBullet.setPosition(x + width / 2 - 6 + offsetX, y + 15);

                    // Spawn both bullets
                    com.spaceshooter.v2.v3.SpaceShooter.getGame().spawn(leftBullet);
                    com.spaceshooter.v2.v3.SpaceShooter.getGame().spawn(rightBullet);
                    break;
                case 3:
                    // Spread shot (3 bullets)
                    float spreadAngle = -0.2f; // Spread angle in radians

                    // Center bullet
                    Bullet centerBullet = new Bullet(3, angle, Bullet.ParentShooter.PLAYER);
                    centerBullet.setPosition(x + width / 2 - 6, y + 15);

                    // Left spread bullet
                    Bullet leftSpreadBullet = new Bullet(3, angle - spreadAngle, Bullet.ParentShooter.PLAYER);
                    leftSpreadBullet.setPosition(x + width / 2 - 12, y + 15);

                    // Right spread bullet
                    Bullet rightSpreadBullet = new Bullet(3, angle + spreadAngle, Bullet.ParentShooter.PLAYER);
                    rightSpreadBullet.setPosition(x + width / 2 + 6, y + 15);

                    // Spawn all three bullets
                    com.spaceshooter.v2.v3.SpaceShooter.getGame().spawn(centerBullet);
                    com.spaceshooter.v2.v3.SpaceShooter.getGame().spawn(leftSpreadBullet);
                    com.spaceshooter.v2.v3.SpaceShooter.getGame().spawn(rightSpreadBullet);
                    break;
            }

            // Reset shoot timer and play sound
            shootTimer = 0f;
            Audio.PLAYER_SHOOT.play(SpaceShooter.getSoundVolume());
        }
    }

    public void setWeaponLevel(int weaponLevel) {
        this.weaponLevel = weaponLevel;
    }

    public void setDefaultPosition() {
        set(400, 20, 36, 36);
    }
}
