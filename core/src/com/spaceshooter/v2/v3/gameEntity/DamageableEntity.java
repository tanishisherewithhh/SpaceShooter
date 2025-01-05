package com.spaceshooter.v2.v3.gameEntity;

public interface DamageableEntity {
    int getHealth();
    int getMaxHealth();
    void resetHealth();
    void damage(int damage);
}
