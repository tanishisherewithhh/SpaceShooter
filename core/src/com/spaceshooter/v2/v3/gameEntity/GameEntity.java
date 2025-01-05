package com.spaceshooter.v2.v3.gameEntity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.spaceshooter.v2.GameObject;

public abstract class GameEntity extends Rectangle {
    public abstract void render(SpriteBatch batch, float speedMul);

    public boolean collides(GameObject other) {
        return this.overlaps(other);
    }

    public float calculateSpeedMultiplier(float multiplier){
        return Gdx.graphics.getDeltaTime() * multiplier * 10;
    }
}
