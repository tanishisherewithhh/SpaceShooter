package com.spaceshooter.v2.v3.wave;

import com.spaceshooter.v2.v3.SpaceShooter;
import com.spaceshooter.v2.v3.gameEntity.entities.Enemy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WaveManager {
    private final List<Wave> waves;
    private int currentWaveIndex = 0;
    private static float waveTimer = 0;
    private boolean isWaveActive = false;

    public WaveManager() {
        waves = new ArrayList<>();
    }

    public void update(float deltaTime) {
        if (!isWaveActive && currentWaveIndex < waves.size()) {
            startNextWave();
        }

        if (isWaveActive) {
            waveTimer += deltaTime;
            Wave currentWave = waves.get(currentWaveIndex);

            // Check if it's time to spawn enemies
            if (currentWave.shouldSpawnEnemies(waveTimer)) {
                spawnEnemiesForCurrentWave();
            }

            // Check if wave is complete
            if (currentWave.isWaveComplete()) {
                completeCurrentWave();
            }
        }
    }

    private void startNextWave() {
        Wave currentWave = waves.get(currentWaveIndex);
        currentWave.reset();
        isWaveActive = true;
        waveTimer = 0;
        System.out.println("Starting Wave " + (currentWaveIndex + 1));
    }

    private void spawnEnemiesForCurrentWave() {
        Wave currentWave = waves.get(currentWaveIndex);
        Enemy enemyToSpawn = currentWave.getNextEnemy();
        if (enemyToSpawn != null) {
            // Set initial position at the top of the screen
            float enemyToSpawnX = enemyToSpawn.getX();
            float enemyToSpawnY = enemyToSpawn.getY();

            enemyToSpawn.setPosition(enemyToSpawn.getX(), SpaceShooter.WINDOW_HEIGHT + enemyToSpawn.height);
            // Set target position to the specified position in the .wv file
            enemyToSpawn.setTargetPosition(enemyToSpawnX, enemyToSpawnY);

            SpaceShooter.getGame().spawn(enemyToSpawn);
        }
    }

    private void completeCurrentWave() {
        System.out.println("Wave " + (currentWaveIndex + 1) + " completed!");
        isWaveActive = false;
        currentWaveIndex++;
    }

    public void addWave(Wave wave) {
        waves.add(wave);
    }

    public void loadWaveFromFile(String filePath) {
        Wave wave = new Wave();
        boolean hasSpawnInterval = false;
        boolean hasWaveDuration = false;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isMetadataSection = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }

                if (isMetadataSection) {
                    // Check if the line starts with "type:" (indicating the start of enemy data)
                    if (line.startsWith("type:")) {
                        isMetadataSection = false; // Exit metadata section
                    } else {
                        // Parse metadata
                        if (line.startsWith("spawnInterval:")) {
                            hasSpawnInterval = true;
                        } else if (line.startsWith("waveDuration:")) {
                            hasWaveDuration = true;
                        }
                        parseMetadata(line, wave);
                        continue; // Skip to the next line
                    }
                }

                // Parse enemy data
                Enemy enemy = parseEnemyFromLine(line);
                if (enemy != null) {
                    wave.addEnemy(enemy);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Log warnings for missing metadata
        if (!hasSpawnInterval) {
            System.out.println("WARNING: spawnInterval not specified in wave file: {"+ filePath + "}. Using default value: " + wave.getSpawnInterval());
        }
        if (!hasWaveDuration) {
            System.out.println("WARNING: waveDuration not specified in wave file: {" + filePath + "}. Using default value: " + wave.getWaveDuration());
        }

        waves.add(wave);
    }

    private void parseMetadata(String line, Wave wave) {
        String[] parts = line.split(": ");
        if (parts.length != 2) {
            return; // Invalid metadata line
        }

        String key = parts[0].trim();
        String value = parts[1].trim();

        switch (key) {
            case "spawnInterval":
                wave.setSpawnInterval(Float.parseFloat(value));
                break;
            case "waveDuration":
                wave.setWaveDuration(Float.parseFloat(value));
                break;
            default:
                System.out.println("Unknown metadata key: " + key);
                break;
        }
    }

    private Enemy parseEnemyFromLine(String line) {
        String[] parts = line.split(", ");
        Enemy.EnemyType type = null;
        float x = 0, y = 0;
        int health = 0, speed = 0;

        for (String part : parts) {
            String[] keyValue = part.split(": ");
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            switch (key) {
                case "type":
                    type = Enemy.EnemyType.valueOf(value);
                    break;
                case "x":
                    x = Float.parseFloat(value);
                    break;
                case "y":
                    y = Float.parseFloat(value);
                    break;
                case "health":
                    health = Integer.parseInt(value);
                    break;
                case "speed":
                    speed = Integer.parseInt(value);
                    break;
            }
        }

        if (type != null) {
            Enemy enemy = type.create();
            enemy.setPosition(x, y);
            enemy.setHealth(health);
            enemy.setMovementSpeed(speed);
            return enemy;
        }
        return null;
    }

    // Wave Class
    public static class Wave {
        private final List<Enemy> enemiesToSpawn;
        private final List<Enemy> spawnedEnemies;
        private float spawnInterval = 1f;
        private float waveDuration = 30f;
        private float lastSpawnTime = 0;
        private int enemyIndex = 0;

        public Wave() {
            enemiesToSpawn = new ArrayList<>();
            spawnedEnemies = new ArrayList<>();
        }

        public void addEnemy(Enemy enemy) {
            enemiesToSpawn.add(enemy);
        }

        public void setSpawnInterval(float interval) {
            this.spawnInterval = interval;
        }

        public void setWaveDuration(float duration) {
            this.waveDuration = duration;
        }

        public boolean shouldSpawnEnemies(float currentTime) {
            return enemyIndex < enemiesToSpawn.size() &&
                    currentTime - lastSpawnTime >= spawnInterval &&
                    currentTime <= waveDuration;
        }

        public Enemy getNextEnemy() {
            if (enemyIndex < enemiesToSpawn.size()) {
                lastSpawnTime = waveTimer;
                Enemy enemy = enemiesToSpawn.get(enemyIndex);
                spawnedEnemies.add(enemy);
                enemyIndex++;
                return enemy;
            }
            return null;
        }

        public boolean isWaveComplete() {
            return enemyIndex >= enemiesToSpawn.size() && spawnedEnemies.isEmpty();
        }

        public void reset() {
            enemyIndex = 0;
            lastSpawnTime = 0;
            spawnedEnemies.clear();
        }

        public float getSpawnInterval() {
            return spawnInterval;
        }

        public float getWaveDuration() {
            return waveDuration;
        }
    }
}