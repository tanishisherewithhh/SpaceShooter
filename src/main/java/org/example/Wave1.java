package org.example;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

abstract class Wave {
    protected List<SpaceShooter.Enemy> enemies;
    protected int frameWidth;

    public Wave(int frameWidth) {
        this.frameWidth = frameWidth;
        enemies = new ArrayList<>();
    }

    public List<SpaceShooter.Enemy> getEnemies() {
        return enemies;
    }

    public abstract void createEnemies();
}

class Wave1 extends Wave {
    public Wave1(int frameWidth) {
        super(frameWidth);
    }

    public void createEnemies() {
        for (int i = 0; i < 5; i++) {
            enemies.add(new SpaceShooter.Enemy(i * 30 + i * 20+ 350, 40, 1,2, SpaceShooter.EnemyType.ONE));
        }
    }
}

class Wave2 extends Wave {
    public Wave2(int frameWidth) {
        super(frameWidth);
    }

    public void createEnemies() {
        for (int i = 0; i < 5; i++) {
            enemies.add(new SpaceShooter.Enemy(i * 30 +  i * 20+ 350, 40, 2,5,SpaceShooter.EnemyType.TWO));
            enemies.add(new SpaceShooter.Enemy(i * 30 +  i * 20+ 350, 70, 2,2,SpaceShooter.EnemyType.ONE));
        }
    }
}

class Wave3 extends Wave {
    public Wave3(int frameWidth) {
        super(frameWidth);
    }

    public void createEnemies() {
        for (int i = 0; i < 5; i++) {
            enemies.add(new SpaceShooter.Enemy(i * 30 +  i * 20 + 350, 40, 3,8,SpaceShooter.EnemyType.THREE));
            enemies.add(new SpaceShooter.Enemy(i * 30 +  i * 20+ 350, 70, 3,5,SpaceShooter.EnemyType.TWO));
            enemies.add(new SpaceShooter.Enemy(i * 30 +  i * 20 + 350, 60 + 40, 3,2,SpaceShooter.EnemyType.ONE));
        }
    }
}
class Wave4 extends Wave{
    public Wave4(int frameWidth) {
        super(frameWidth);
    }

    public void createEnemies() {
        enemies.add(new SpaceShooter.Enemy(400, 50, 4, 15,SpaceShooter.EnemyType.BOSS));
        for (int i = 0; i < 3; i++) {
            enemies.add(new SpaceShooter.Enemy(i * 30 +  i * 30 + 350, 200, 3,8,SpaceShooter.EnemyType.THREE));
            enemies.add(new SpaceShooter.Enemy(i * 30 +  i * 30+ 350, 230, 3,5,SpaceShooter.EnemyType.TWO));
            enemies.add(new SpaceShooter.Enemy(i * 30 +  i * 30 + 350, 260 + 40, 3,2,SpaceShooter.EnemyType.ONE));
        }
    }
}