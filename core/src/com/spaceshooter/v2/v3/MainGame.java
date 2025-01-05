package com.spaceshooter.v2.v3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spaceshooter.v2.v3.gameEntity.GameEntity;
import com.spaceshooter.v2.v3.gameEntity.MovableEntity;
import com.spaceshooter.v2.v3.wave.WaveManager;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class MainGame {
    private final Set<GameEntity> GAME_ENTITIES = new CopyOnWriteArraySet<>();
    private WaveManager manager;

    public void spawn(GameEntity entity){
        this.GAME_ENTITIES.add(entity);
    }
    public void despawn(GameEntity entity){
        this.GAME_ENTITIES.remove(entity);
    }
    public void clearAll(){
        this.GAME_ENTITIES.clear();
    }
    public void createWaveManager(){
        manager = new WaveManager();
        manager.loadWaveFromFile(Gdx.files.internal("waves/wave1.wv").path());
    }

    public void update(float deltaTime){
        manager.update(deltaTime);
    }

    public void renderGameEntities(SpriteBatch batch, float speedMul){
        GAME_ENTITIES.forEach(entity -> entity.render(batch,speedMul));
    }
    public void simulateMovement(){
        GAME_ENTITIES.forEach(entity -> {
            if(entity instanceof MovableEntity movableEntity){
                movableEntity.movement();
            }
        });
    }

    public void checkEntityBounds(){
        GAME_ENTITIES.removeIf(entity -> entity.x < -2 ||
                                         entity.x > SpaceShooter.WINDOW_WIDTH + 2 ||
                                         entity.y < -2 ||
                                         entity.y > SpaceShooter.WINDOW_HEIGHT + 2
        );
    }
}
