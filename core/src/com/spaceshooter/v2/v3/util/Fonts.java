package com.spaceshooter.v2.v3.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Fonts {
    public static BitmapFont FONT, SMALL_FONT;

    public static void create(){
        FONT = new BitmapFont(Gdx.files.internal("comicSans.fnt"));
        SMALL_FONT = new BitmapFont(Gdx.files.internal("smallComicSans.fnt"));
    }

    public static void dispose() {
        FONT.dispose();;
        SMALL_FONT.dispose();
    }
}
