package org.example;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SpaceShooter {
    // Game state and configuration variables
    private static boolean gameRunning = true, gameOver = false, gameWon = false, destroyAllEnemies = false;
    private static int wave = 1;
    private static int enemyKilled = 0;
    private static int gameSpeed = 15;
    private static long startTime;
    private static SkillPointSystem skillPointSystem = new SkillPointSystem();

    // Game objects and resources
    private static JFrame frame;
    private static DrawPanel drawPanel = new DrawPanel();
    private static Spaceship spaceship;
    private static List<Enemy> enemies;
    private static List<PlayerBullet> bullets;
    private static List<Bullet> enemyBullets;
    private static Image spaceImage, background;
    private static InputStream[] backgroundSounds;
    private static int currentSoundIndex = 0;
    private static SpaceShooter spaceShooter;

    public static void main(String[] args) {
        spaceShooter = new SpaceShooter();
        spaceShooter.startGame();
    }

    public void startGame() {
        initializeBackgroundSounds();
        playBackgroundSounds();
        loadImages();
        createGameFrame();
        initializeGameObjects();


        // Game loop
        while (gameRunning) {
            frame.validate();
            moveGameObjects();
            shootEnemyBullets();
            checkCollisions();
            repaintFrame();
            sleep(gameSpeed);
        }

        if (destroyAllEnemies) {
            enemies.clear();
            gameRunning = false;
            JOptionPane.showMessageDialog(frame, "You won!");
            gameOver = true;
            gameWon = true;
            showGameOverMenu();
        }
    }

    private void initializeBackgroundSounds() {
        try {
            InputStream soundURL = SpaceShooter.class.getResourceAsStream("/sounds/bg0.wav");
            InputStream sound2URL = SpaceShooter.class.getResourceAsStream("/sounds/bg1.wav");
            InputStream sound3URL = SpaceShooter.class.getResourceAsStream("/sounds/bg2.wav");
            if (soundURL != null || sound2URL != null || sound3URL != null) {
                backgroundSounds = new InputStream[]{soundURL, sound2URL, sound3URL};
            } else {
                System.out.println("Resource not found");
                System.out.println(soundURL);
                System.out.println(sound2URL);
                System.out.println(sound3URL);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void playBackgroundSounds() {
        final Clip[] clip = {SoundUtils.playSound(backgroundSounds[currentSoundIndex])};
        clip[0].addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                clip[0].close();
                currentSoundIndex = (currentSoundIndex + 1) % backgroundSounds.length;
                clip[0] = SoundUtils.playSound(backgroundSounds[currentSoundIndex]);
                clip[0].loop(Clip.LOOP_CONTINUOUSLY);
            }
        });
    }

    private void loadImages() {
        try {
            spaceImage = ImageIO.read(SpaceShooter.class.getResourceAsStream("/images/space.png"));
            background = ImageIO.read(SpaceShooter.class.getResourceAsStream("/images/background.png"));
        } catch (Exception ignored) {
        }
    }

    private void initializeGameObjects() {
        startTime = System.currentTimeMillis();
        spaceship = new Spaceship();
        Wave wave = new Wave1(frame.getWidth());
        wave.createEnemies();
        enemies = wave.getEnemies();
        bullets = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        sleep(1000);  // wait for 1 second before starting the game loop
    }

    private void createGameFrame() {
        frame = new JFrame("Space Shooter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setSize(800, 610);
        frame.setLocation(375, 55);
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    spaceship.dx = -5;
                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    spaceship.dx = 5;
                if (e.getKeyCode() == KeyEvent.VK_UP)
                    spaceship.dy = -5;
                if (e.getKeyCode() == KeyEvent.VK_DOWN)
                    spaceship.dy = 5;
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    bullets.add(new PlayerBullet(spaceship.x + spaceship.width / 2, spaceship.y,spaceship.bulletDamage ));
                    SoundUtils.playSound(SpaceShooter.class.getResourceAsStream("/sounds/playerShoot.wav"));
                }
            }

            public void keyReleased(KeyEvent e) {
                spaceship.dx = 0;
                spaceship.dy = 0;
            }
        });
        JPanel buttonPanel = getButtonPanel();

        // Add the panel to the frame
        frame.add(buttonPanel, BorderLayout.NORTH);
    }

    private static JPanel getButtonPanel() {
        JButton upgradeHealthButton = new JButton("Upgrade Health");
        upgradeHealthButton.addActionListener(e -> {
            if (skillPointSystem.useSkillPoint()) {
                spaceship.health += 5;  // Upgrade health
            }
            drawPanel.requestFocusInWindow();  // Request focus back to the game panel

        });

        JButton upgradeBulletDamageButton = new JButton("Upgrade Bullet Damage");
        upgradeBulletDamageButton.addActionListener(e -> {
            if (skillPointSystem.useSkillPoint()) {
               spaceship.bulletDamage += 1;  // Upgrade bullet damage
            }
            drawPanel.requestFocusInWindow();  // Request focus back to the game panel

        });

        // Create a panel with a grid layout
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(upgradeHealthButton);
        buttonPanel.add(upgradeBulletDamageButton);
        return buttonPanel;
    }

    private static void moveGameObjects() {
        spaceship.move();
        for (Enemy enemy : enemies)
            enemy.move();
        for (PlayerBullet bullet : bullets)
            bullet.move();
        for (Bullet bullet : enemyBullets)
            bullet.move();
    }


    private static void shootEnemyBullets() {
        for (Enemy enemy : enemies)
            enemy.shoot();
    }

    private static void checkCollisions() {
        for (Enemy enemy : new ArrayList<>(enemies)) {
            if (destroyAllEnemies) {
                enemies.remove(enemy);
                continue;
            }
            if (enemy.intersects(spaceship)) {
                spaceship.health -= enemy.strength;
                SoundUtils.playSound(SpaceShooter.class.getResourceAsStream("/sounds/collisiondamage.wav"));
                if (spaceship.health <= 0) {
                    gameRunning = false;
                    JOptionPane.showMessageDialog(frame, "Game Over");
                    gameOver = true;
                    gameWon = false;
                }
                enemies.remove(enemy);
                enemyKilled++;
                skillPointSystem.addSkillPoint();  // Add a skill point when an enemy is killed
                if (enemies.isEmpty()) {
                    wave++;
                    if (wave == 2) {
                        Wave wave = new Wave2(frame.getWidth());
                        wave.createEnemies();
                        enemies = wave.getEnemies();
                    } else if (wave == 3) {
                        Wave wave = new Wave3(frame.getWidth());
                        wave.createEnemies();
                        enemies = wave.getEnemies();
                    } else if (wave == 4) {
                        Wave wave = new Wave4(frame.getWidth());
                        wave.createEnemies();
                        spaceship.health += 5;
                        enemies = wave.getEnemies();
                    } else if (wave > 4) {
                        gameRunning = false;
                        JOptionPane.showMessageDialog(frame, "You won!");
                        gameOver = true;
                        gameWon = true;
                    }
                }
            }

            for (PlayerBullet bullet : new ArrayList<>(bullets)) {
                for (Bullet bullet2 : new ArrayList<>(enemyBullets)) {

                    if (bullet2.intersects(bullet)) {
                        bullets.remove(bullet);
                        enemyBullets.remove(bullet2);
                        continue;
                    }
                    if (bullet2.intersects(spaceship)) {
                        spaceship.health -= bullet2.damage;
                        SoundUtils.playSound(SpaceShooter.class.getResourceAsStream("/sounds/playerdamage.wav"));
                        if (spaceship.health <= 0) {
                            gameRunning = false;
                            JOptionPane.showMessageDialog(frame, "Game Over");
                            gameOver = true;
                        }
                        enemyBullets.remove(bullet2);
                    }
                }
                if (bullet.intersects(enemy)) {
                    enemy.health -= bullet.damage;
                    SoundUtils.playSound( SpaceShooter.class.getResourceAsStream("/sounds/enemydamage.wav"));
                    if (enemy.type == EnemyType.BOSS && enemy.health <= 0) {
                            sleep(100);
                            SoundUtils.playSound( SpaceShooter.class.getResourceAsStream("/sounds/bomb1.wav"));
                            sleep(100);
                            SoundUtils.playSound( SpaceShooter.class.getResourceAsStream("/sounds/bomb2.wav"));
                            sleep(100);
                            SoundUtils.playSound( SpaceShooter.class.getResourceAsStream("/sounds/bomb3.wav"));
                            sleep(100);
                            SoundUtils.playSound( SpaceShooter.class.getResourceAsStream("/sounds/bomb4.wav"));
                            sleep(200);

                            destroyAllEnemies = true;
                    }
                    if (enemy.health <= 0) {
                        enemies.remove(enemy);
                        enemyKilled++;
                        skillPointSystem.addSkillPoint();  // Add a skill point when an enemy is killed
                        if (enemies.isEmpty()) {
                            wave++;
                            if (wave == 2) {
                                Wave wave = new Wave2(frame.getWidth());
                                wave.createEnemies();
                                enemies = wave.getEnemies();
                            } else if (wave == 3) {
                                Wave wave = new Wave3(frame.getWidth());
                                wave.createEnemies();
                                enemies = wave.getEnemies();
                            } else if (wave == 4) {
                                Wave wave = new Wave4(frame.getWidth());
                                wave.createEnemies();
                                spaceship.health += 5;
                                enemies = wave.getEnemies();
                            } else if (wave > 4) {
                                gameRunning = false;
                                JOptionPane.showMessageDialog(frame, "You won!");
                                gameOver = true;
                                gameWon = true;
                            }
                        }
                    }
                    bullets.remove(bullet);
                }
            }
        }
    }

    private static void repaintFrame() {
        SwingUtilities.invokeLater(() -> frame.repaint());
    }

    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception ignore) {
        }
    }

    public static void showGameOverMenu() {
        JFrame menu = new JFrame("Space Shooter");
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setSize(400, 400);
        menu.setLocation(frame.getLocation());
        menu.setLayout(new GridLayout(3, 1));
        JLabel titleLabel = new JLabel("Game Over", SwingConstants.CENTER);
        JLabel gameStateLabel = new JLabel(gameWon ? "YOU WON!!!" : "YOU LOST", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        gameStateLabel.setFont(new Font("Serif", Font.BOLD, 15));
        frame.setVisible(false);
        menu.add(titleLabel);
        menu.add(gameStateLabel);
        menu.setVisible(true);
    }

    public void showMainMenu() {
        JFrame menu = new JFrame("Space Shooter");
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setSize(400, 400);
        menu.setLayout(new GridLayout(2, 1));
        JLabel titleLabel = new JLabel("Space Shooter", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        JButton playButton = new JButton("Play");
        playButton.addActionListener(e -> {
            menu.setVisible(false);
            startGame();
        });
        menu.add(titleLabel);
        menu.add(playButton);
        menu.setVisible(true);
    }

    public enum EnemyType {
        ONE,
        TWO,
        THREE,
        BOSS
    }

    static class DrawPanel extends JPanel {
        public void paintComponent(Graphics g) {
            if (!gameOver) {
                g.drawImage(spaceImage, 0, 0, this.getWidth(), this.getHeight(), null);

                if (spaceship != null) {
                    spaceship.drawHealthBar(g, spaceship);
                    g.drawImage(spaceship.image, spaceship.x, spaceship.y, spaceship.width, spaceship.height, null);
                }


                g.drawImage(background, 2, frame.getHeight() - 180, 202, 120, null);


                g.setColor(Color.WHITE);
                g.setFont(g.getFont().deriveFont(20f));
                g.drawString("Health: " + spaceship.health, 25, frame.getHeight() - 73);
                g.drawString("Time: " + (System.currentTimeMillis() - startTime) / 1000 + "s", 25, frame.getHeight() - 93);
                g.drawString("Wave: " + wave, 25, frame.getHeight() - 113);
                g.drawString("Enemies Left: " + enemies.size(), 25, frame.getHeight() - 133);
                g.drawString("Enemies Killed: " + enemyKilled, 25, frame.getHeight() - 153);
                g.setFont(g.getFont().deriveFont(15f));
                g.drawString("Skill Points left: " + skillPointSystem.getSkillPoints(), 5, 20);
                g.drawString("Current Bullet Damage: " + spaceship.bulletDamage, 5, 38);

                if (bullets != null) {
                    g.setColor(Color.YELLOW);
                    for (PlayerBullet bullet : new ArrayList<>(bullets))
                        g.fillRect(bullet.x, bullet.y, 2, 6);
                }
                if (enemyBullets != null) {
                    g.setColor(Color.RED);
                    for (Bullet bullet : new ArrayList<>(enemyBullets)) {
                        if (wave == 4) {
                            bullet.damage = 3;
                            bullet.width = 2;
                            bullet.health = 3;
                            bullet.height = 16;
                        }
                        g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
                    }
                }

                for (Enemy enemy : new ArrayList<>(enemies)) {
                    if (wave == 4 && enemy.type != EnemyType.BOSS) {
                        enemy.width = 40;
                        enemy.height = 40;
                    }
                    if (enemy.type == EnemyType.BOSS && enemy.health <= 0) {
                        enemies.clear();
                    }
                    if (destroyAllEnemies) return;
                    g.drawImage(enemy.image, enemy.x, enemy.y, enemy.width, enemy.height, null);
                    enemy.drawHealthBar(g, enemy);
                }
            } else {
                showGameOverMenu();
            }
        }
    }

    static abstract class GameObject extends Rectangle {
        int dx, dy;
        int health, maxHealth;

        public void move() {
            x += dx;
            y += dy;
        }

        void drawHealthBar(Graphics g, GameObject gameObject) {
            g.setColor(Color.RED);
            g.fillRect(gameObject.x, gameObject.y - 10, gameObject.width, 5);
            g.setColor(Color.GREEN);
            int healthBarWidth = (int) ((double) gameObject.health / gameObject.maxHealth * gameObject.width);
            g.fillRect(gameObject.x, gameObject.y - 10, healthBarWidth, 5);
        }

    }

    static class Spaceship extends GameObject {
        Image image;
        int bulletDamage;

        public Spaceship() {
            width = 30;
            height = 30;
            x = 385;
            y = 510;
            health = 20;
            maxHealth = health;
            bulletDamage = 2;
            try {
                image = ImageIO.read(Objects.requireNonNull(SpaceShooter.class.getResourceAsStream("/images/spaceship.png")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class Enemy extends GameObject {
        Image image;
        int strength;
        float shootProbability;
        EnemyType type;

        public Enemy(int x, int y, int wave, int strength, EnemyType type) {
            this.x = x;
            this.y = y;
            if (type != EnemyType.BOSS) {
                width = 30;
                height = 30;
                dy = wave;
                health = wave * 10 + 5;
            } else {
                width = 150;
                height = 150;
                dy = wave + 1;
                health = 100;
            }
            maxHealth = health;
            this.strength = strength;
            shootProbability = wave - wave / 1.34f;
            this.type = type;
            try {
                switch (type) {

                    case ONE -> image = ImageIO.read(SpaceShooter.class.getResource("/images/enemy1.png"));
                    case TWO -> image = ImageIO.read(SpaceShooter.class.getResource("/images/enemy2.png"));
                    case THREE -> image = ImageIO.read(SpaceShooter.class.getResource("/images/enemy3.png"));
                    case BOSS -> image = ImageIO.read(SpaceShooter.class.getResource("/images/bossspaceship.png"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void shoot() {
            Random rand = new Random();
            if (rand.nextFloat(100) < shootProbability && !destroyAllEnemies) {
                enemyBullets.add(new Bullet(x + width / 2, y + height, 3));
                SoundUtils.playSound( SpaceShooter.class.getResourceAsStream("/sounds/enemyShoot.wav"));
            }
        }

        @Override
        public void move() {
            Random rand = new Random();
            dx = rand.nextInt(3) - 1; // random number between -1 and 1
            dy = rand.nextInt(3) - 1; // random number between -1 and 1
            if (x < 5 || x > frame.getWidth() - width)
                dx = -dx; // change direction if the enemy hits the edge of the frame
            if (y < 10 || y > frame.getHeight() - height)
                dy = -dy; // change direction if the enemy hits the edge of the frame

            super.move();
        }

    }

    static class Bullet extends GameObject {
        int damage;


        public Bullet(int x, int y, int dy) {
            this.x = x;
            this.y = y;
            width = 2;
            height = 5;
            maxHealth = health;
            this.dy = dy;
            damage = 2;
        }
    }
    static class PlayerBullet extends GameObject {
        int damage;


        public PlayerBullet(int x, int y, int damage) {
            this.x = x;
            this.y = y;
            width = 2;
            height = 5;
            maxHealth = health;
            this.dy = -3;
            this.damage = damage;
        }
    }
    static class SoundUtils {
        public static Clip playSound(InputStream soundStream) {
            Clip clip = null;
            try {
                // Wrap the InputStream in a BufferedInputStream
                BufferedInputStream bufferedStream = new BufferedInputStream(soundStream);
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedStream);
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.out.println("Error with playing sound.");
                e.printStackTrace();
            }
            return clip;
        }
    }


}

