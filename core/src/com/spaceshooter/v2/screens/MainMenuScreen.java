package com.spaceshooter.v2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.spaceshooter.v2.SpaceShooter;

import static com.spaceshooter.v2.SpaceShooter.camera;

public class MainMenuScreen extends ScreenAdapter {
    final SpaceShooter game;
    private Stage stage;
    private Skin skin;
    private TextButton startButton;
    private TextButton settingsButton;
    private TextButton helpButton;


    public MainMenuScreen(final SpaceShooter game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        startButton = new TextButton("    Play    ", skin);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        settingsButton = new TextButton("Settings", skin);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game, MainMenuScreen.this));
                dispose();
            }
        });
        helpButton = new TextButton("   Help   ", skin);
        helpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HelpScreen(MainMenuScreen.this, game));
                dispose();
            }
        });

        Table table = new Table();
        table.setRound(true);
        table.setFillParent(true);
        table.setSkin(skin);
        table.add(startButton).colspan(8);
        table.row();
        table.add(settingsButton);
        table.row();
        table.add(helpButton);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(SpaceShooter.menuBg, 0, 0, 800, 480);
        game.font.draw(game.batch, "SpaceShooter V2", 300, 400);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }
}
