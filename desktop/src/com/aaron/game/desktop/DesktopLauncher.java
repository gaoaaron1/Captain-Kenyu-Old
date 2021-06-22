package com.aaron.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.aaron.game.PlatformGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.foregroundFPS = 60;
		config.width = PlatformGame.WIDTH;
		config.height = PlatformGame.HEIGHT;
		config.resizable = true;

		new LwjglApplication(new PlatformGame(), config);
	}
}
