package com.n.a.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.n.a.XYZGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "XYZ";
		config.fullscreen = false;
		// config.width = 1440;
		// config.height = 900;
		config.width = 1200;
		config.height = 800;

		new LwjglApplication(new XYZGame(), config);
	}
}
