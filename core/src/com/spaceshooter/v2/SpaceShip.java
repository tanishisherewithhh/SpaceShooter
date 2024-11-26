package com.spaceshooter.v2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.spaceshooter.v2.movement.MovementType;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SpaceShip extends GameObject {
    float targetX, targetY;
    Vector3 touchPos;
    List<Bullet> bullets = new CopyOnWriteArrayList<>();
    private float shootTimer = 0;
    public int enemiesKilled = 0;

    public SpaceShip(Texture SpaceShipTexture) {
        super(SpaceShipTexture, 3, 20);
        setDefaultPosition();
    }

    public void render(SpriteBatch batch) {
        // Calculate the angle between the spaceship and the mouse
        touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        SpaceShooter.camera.unproject(touchPos);
        float dx = touchPos.x - x - width / 2;
        float dy = touchPos.y - y - height / 2;
        float angle = (float) Math.atan2(dy, dx);

        // Rotate the spaceship's sprite to face the mouse
        batch.draw(objectTextureRegion, x, y, (float) width / 2, (float) height / 2, width, height, 1, 1, (float) (angle * 180 / Math.PI) - 90);

        shootTimer += Gdx.graphics.getDeltaTime();

        shoot(angle);
        bullets.forEach(bullet -> {
            bullet.move(3);
            batch.draw(bullet.objectTextureRegion, bullet.x, bullet.y, bullet.width / 2, (float) bullet.height / 2, bullet.width, bullet.height, 1, 1, (float) (bullet.angle * 180 / Math.PI) - 90);
        });
    }

    public void movement() {
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && SpaceShooter.movementType == MovementType.Mouse) {
            targetX = touchPos.x - width / 2;
            targetY = touchPos.y - height / 2;
            float t = 0.05f; // control the speed of glide
            float lerpedX = Interpolation.linear.apply(x, targetX, t);
            float lerpedY = Interpolation.linear.apply(y, targetY, t);
            this.x = lerpedX;
            this.y = lerpedY;
        } else if (SpaceShooter.movementType != MovementType.Mouse) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                move(-movementSpeed, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                move(movementSpeed, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                move(0, movementSpeed);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                move(0, -movementSpeed);
            }
        }

        if (this.x >= 800 - width / 2)
            this.x = 800 - width / 2;
        else if (this.x <= 0)
            this.x = 0;

        if (this.y > 480 - height / 2)
            this.y = 480 - height / 2;
        else if (this.y < 0)
            this.y = 0;
    }

    public void shoot(float angle) {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && shootTimer >= 0.4f) {
            Bullet bullet = new Bullet(SpaceShooter.playerBullet);
            bullet.setAngle(angle);
            bullet.setSize(16, 16);
            bullet.setPosition(x + width / 2 - 6, y + 6);
            shootTimer = 0f;
            bullets.add(bullet);
            SpaceShooter.PLAYER_SHOOT.play(SpaceShooter.getSoundVolume());
        }
        bullets.removeIf(bullet -> (bullet.x > 800 || bullet.x < 0) && (bullet.y > 0 || bullet.y < 480));
    }

    public void setDefaultPosition() {
        set(400, 20, 36, 36);
    }
}
