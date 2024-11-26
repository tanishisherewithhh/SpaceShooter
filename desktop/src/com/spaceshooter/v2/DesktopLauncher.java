package com.spaceshooter.v2;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.spaceshooter.v2.SpaceShooter;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(800,480);
		config.setWindowSizeLimits(800,480,800,480);
		config.setMaximized(false);
		config.useVsync(true);
		config.setResizable(false);
		config.setTitle("SpaceShootersV2");
		new Lwjgl3Application(new SpaceShooter(), config);
	}
}
