package com.spaceshooter.v2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet extends GameObject {
    float angle;

    public Bullet(Texture objectTexture) {
        super(objectTexture, 3, 3);
    }

    public Bullet(Texture objectTexture, int movementSpeed) {
        super(objectTexture, movementSpeed, 3, 3);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(objectTexture, x, y, width, health);
    }

    @Override
    public void movement() {


    }

    public void move(float speed) {
        x += (float) (Math.cos(angle) * speed);
        y += (float) (Math.sin(angle) * speed);
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
