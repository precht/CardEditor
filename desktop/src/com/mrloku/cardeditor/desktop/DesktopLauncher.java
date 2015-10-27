package com.mrloku.cardeditor.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mrloku.cardeditor.CardEditorMain;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 990;
		config.height = 660;
		config.title = "Cards Editor";
		new LwjglApplication(new CardEditorMain(), config);
	}
}