package com.spaceshooter.v2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

public class HelpScreen extends ScreenAdapter {
    final SpaceShooter game;
    Screen prvScreen;
    private Stage stage;
    private Skin skin;
    public HelpScreen(Screen prvScreen, final SpaceShooter game) {
        this.prvScreen = prvScreen;
        this.game = game;
        skin = new Skin(Gdx.files.internal("uiskin.json"));

    }

    @Override
    public void show() {
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.mainMenuScreen);
                dispose();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.setSkin(skin);
        table.add(backButton).colspan(8);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(SpaceShooter.menuBg, 0, 0, 800, 480);
        game.smallFont.draw(game.batch, "This is a normal space invaders game which was extended from my previous space shooter game built", 5, 400);
        game.smallFont.draw(game.batch, "using `java.awt` and `java.swingx` libraries", 5, 375);
        game.smallFont.draw(game.batch, "You can change the settings. After a number of waves (can be changed in settings), a boss wave will", 5, 350);
        game.smallFont.draw(game.batch, "start with a boss and more difficult enemies.", 5, 325);
        game.smallFont.draw(game.batch, "Boss wave does not initiate when infinite waves are selected in settings", 5, 300);
        game.smallFont.draw(game.batch, "- Control the player using W/A/S/D keys or the arrow keys", 5, 275);
        game.smallFont.draw(game.batch, "- Use the mouse to aim", 5, 250);
        game.smallFont.draw(game.batch, "- Use spacebar to shoot at enemies", 5, 225);

        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
