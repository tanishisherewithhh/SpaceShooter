package com.spaceshooter.v2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.spaceshooter.v2.movement.Movement;

public abstract class GameObject extends Rectangle implements Movement {
    protected TextureRegion objectTextureRegion;
    Texture objectTexture;
    int movementSpeed = 5;
    int damage;
    public int health;

    public GameObject(Texture objectTexture, int damage, int health) {
        this.objectTexture = objectTexture;
        this.objectTextureRegion = new TextureRegion(objectTexture);
        this.damage = damage;
        this.health = health;
    }

    public GameObject(Texture objectTexture, int movementSpeed, int damage, int health) {
        this.objectTexture = objectTexture;
        this.movementSpeed = movementSpeed;
        this.damage = damage;
        this.health = health;
        this.objectTextureRegion = new TextureRegion(objectTexture);
    }

    @Override
    public void move(float dx, float dy) {
        x += dx;
        y += dy;
    }

    public boolean collides(GameObject other) {
        return this.overlaps(other);
    }

    public abstract void render(SpriteBatch batch);

    public abstract void movement();

    public void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public void setObjectTextureRegion(TextureRegion objectTextureRegion) {
        this.objectTextureRegion = objectTextureRegion;
    }
}
