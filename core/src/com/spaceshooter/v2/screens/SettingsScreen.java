package com.spaceshooter.v2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.spaceshooter.v2.SpaceShooter;
import com.spaceshooter.v2.movement.MovementType;

import static com.spaceshooter.v2.SpaceShooter.camera;

public class SettingsScreen implements Screen {
    final SpaceShooter game;
    Pixmap pixmap;
    Texture rectangleTexture;
    ParticleEffect fire;
    Screen prevScreen;
    private Stage stage;
    private Skin skin;
    private Slider musicVolumeSlider;
    private Slider soundVolumeSlider;
    private SelectBox<MovementType> movementSelectBox;
    private Slider difficultySlider;
    private Slider wavesSlider;
    private CheckBox infiniteCheckBox;
    private TextButton backButton;

    public SettingsScreen(final SpaceShooter game, Screen prevScreen) {
        this.game = game;
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.prevScreen = prevScreen;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        pixmap = new Pixmap(100, 50, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.DARK_GRAY);
        pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
        rectangleTexture = new Texture(pixmap);
        pixmap.dispose();

        musicVolumeSlider = new Slider(0f, 1f, 0.1f, false, skin);
        musicVolumeSlider.setValue(game.getMusicVolume());
        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setMusicVolume(musicVolumeSlider.getValue());
            }
        });

        soundVolumeSlider = new Slider(0f, 1f, 0.1f, false, skin);
        soundVolumeSlider.setValue(SpaceShooter.getSoundVolume());
        soundVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setSoundVolume(soundVolumeSlider.getValue());
            }
        });
        fire = new ParticleEffect();
        fire.load(Gdx.files.internal("fire.p"), Gdx.files.internal(""));
        fire.start();
        fire.setPosition(400, 200);

        movementSelectBox = new SelectBox<>(skin);
        movementSelectBox.setItems(MovementType.values());
        movementSelectBox.setSelected(SpaceShooter.movementType);
        movementSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SpaceShooter.movementType = getMovementType();
            }
        });

        difficultySlider = new Slider(1, 5, 1, false, skin);
        difficultySlider.setValue(SpaceShooter.enemy_difficulty);
        difficultySlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SpaceShooter.enemy_difficulty = getEnemyDifficulty();
            }
        });

        wavesSlider = new Slider(1, 10, 1, false, skin);
        wavesSlider.setValue(SpaceShooter.no_of_waves);
        wavesSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SpaceShooter.no_of_waves = getTotalWaves();
            }
        });

        infiniteCheckBox = new CheckBox("Infinite", skin);
        infiniteCheckBox.setChecked(SpaceShooter.infiniteWaves);
        infiniteCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                wavesSlider.setDisabled(infiniteCheckBox.isChecked());
                wavesSlider.setAnimateDuration(1);
                wavesSlider.setVisualPercent(100);
                if (!infiniteCheckBox.isChecked()) {
                    wavesSlider.setAnimateDuration(0);
                }
                SpaceShooter.infiniteWaves = getInfiniteWaves();
            }
        });

        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(prevScreen);
                dispose();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.setSkin(skin);
        table.add("Music Volume");
        table.add(musicVolumeSlider);
        table.row();
        table.add("Sound Volume");
        table.add(soundVolumeSlider);
        table.row();
        table.add("Movement");
        table.add(movementSelectBox);
        table.row();
        table.add("Enemy Difficulty");
        table.add(difficultySlider);
        table.row();
        table.add("Number of Waves");
        table.add(wavesSlider);
        table.add(infiniteCheckBox);
        table.row();
        table.add(backButton).colspan(3);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(SpaceShooter.menuBg, 0, 0, 800, 480);
        fire.draw(game.batch, delta);
        game.batch.draw(skin.getRegion("white"), 800 / 3 - 115, 105, 500, 300);
        game.batch.draw(rectangleTexture, 800 / 3 - 110, 100, 500, 300);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        rectangleTexture.dispose();
        fire.dispose();
    }

    public MovementType getMovementType() {
        return movementSelectBox.getSelected();
    }

    public int getEnemyDifficulty() {
        return Math.round(difficultySlider.getValue());
    }

    public Boolean getInfiniteWaves() {
        return infiniteCheckBox.isChecked();
    }

    public int getTotalWaves() {
        return Math.round(wavesSlider.getValue());
    }
}
