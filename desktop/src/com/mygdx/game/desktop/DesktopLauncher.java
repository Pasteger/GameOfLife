package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import static com.mygdx.game.WarehouseOfConstants.*;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Game of live";
		config.width = WINDOW_SCALE;
		config.height = WINDOW_SCALE;
		new LwjglApplication(new MyGdxGame(), config);
	}
}
