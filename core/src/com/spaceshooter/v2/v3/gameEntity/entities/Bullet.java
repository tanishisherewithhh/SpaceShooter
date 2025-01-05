package com.spaceshooter.v2.v3.gameEntity.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.spaceshooter.v2.v3.util.Textures;

public class Bullet extends CharacterEntity {
    private static final TextureRegion playerBulletTextureRegion = new TextureRegion(Textures.PLAYER_BULLET);
    private static final TextureRegion enemyBulletTextureRegion = new TextureRegion(Textures.ENEMY_BULLET);
    private final ParentShooter parentShooter;
    private float bulletAngle;

    public Bullet(int attackDamage, int penetrationPower,float bulletAngle, ParentShooter parentShooter){
        super(3 + penetrationPower * 2,5,attackDamage);
        this.attackDamage = attackDamage;
        this.bulletAngle = bulletAngle;
        this.parentShooter = parentShooter;
        setSize(16, 16);
    }

    public Bullet(int attackDamage,float bulletAngle, ParentShooter parentShooter){
        this(attackDamage,0,bulletAngle,parentShooter);
    }
    public Bullet(int attackDamage, ParentShooter parentShooter){
        this(attackDamage,0,0,parentShooter);
    }

    @Override
    public void render(SpriteBatch batch, float speedMul) {
        batch.draw(parentShooter.getTextureRegion(), x, y,width / 2, height / 2, width, height, 1F,1F, (float) ((bulletAngle * 180) / Math.PI) - 90);
    }

    @Override
    public void movement() {
        x += (float) (Math.cos(bulletAngle) * movementSpeed * calculateSpeedMultiplier(speedMultiplier));
        y += (float) (Math.sin(bulletAngle) * movementSpeed * calculateSpeedMultiplier(speedMultiplier));
    }

    public void setBulletAngle(float bulletAngle) {
        this.bulletAngle = bulletAngle;
    }

    public enum ParentShooter {
        ENEMY(enemyBulletTextureRegion),
        PLAYER(playerBulletTextureRegion);

        private final TextureRegion textureRegion;

        ParentShooter(TextureRegion textureRegion) {
            this.textureRegion = textureRegion;
        }

        public TextureRegion getTextureRegion() {
            return textureRegion;
        }
    }
}
