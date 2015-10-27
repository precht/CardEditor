package com.mrloku.cardeditor;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mrloku.cardeditor.game.Assets;
import com.mrloku.cardeditor.screens.MainScreen;

public class CardEditorMain extends Game {
	Assets assets;
	Screen screen;

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_NONE);
		assets = new Assets();
		screen = new MainScreen(assets);
		setScreen(screen);
	}

	@Override
	public void dispose() {
		assets.dispose();
		screen.dispose();
	}
}