package com.spaceshooter.v2.v3.gameEntity.entities;

import com.spaceshooter.v2.v3.gameEntity.AttackingEntity;
import com.spaceshooter.v2.v3.gameEntity.DamageableEntity;
import com.spaceshooter.v2.v3.gameEntity.GameEntity;
import com.spaceshooter.v2.v3.gameEntity.MovableEntity;

public abstract class CharacterEntity extends GameEntity implements MovableEntity, DamageableEntity, AttackingEntity {
    protected int maxHealth;
    protected int health;
    protected int attackDamage;
    protected int movementSpeed;
    protected float speedMultiplier;

    public CharacterEntity(int maxHealth, int movementSpeed, int attackDamage) {
        super();
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.movementSpeed = movementSpeed;
        this.attackDamage = attackDamage;
        this.speedMultiplier = 1.0f;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void damage(int damage) {
        this.health = Math.max(health - damage, 0);
    }

    @Override
    public int getAttackDamage() {
        return attackDamage;
    }

    @Override
    public int getMovementSpeed() {
        return movementSpeed;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    @Override
    public void resetHealth() {
        this.health = maxHealth;
    }
}