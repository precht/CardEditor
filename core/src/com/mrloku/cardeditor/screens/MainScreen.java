package com.mrloku.cardeditor.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mrloku.cardeditor.game.Assets;
import com.mrloku.cardeditor.screens.widgets.Fight;
import com.mrloku.cardeditor.screens.widgets.Middle;
import com.mrloku.cardeditor.screens.widgets.Side;
import com.mrloku.cardeditor.screens.widgets.Top;

public class MainScreen implements Screen {
	// private static final String TAG = MainScreen.class.getName();

	private boolean debugLines = false;
	private Assets assets;
	private Stage stage;
	private Skin skin;

	private Top top;
	private Side side;
	private Middle middle;
	private Fight fight;

	public MainScreen(Assets assets) {
		this.assets = assets;
	}

	@Override
	public void show() {
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		top = new Top(skin, assets);
		side = new Side(skin, assets);
		fight = new Fight(assets, skin, side);
		middle = new Middle(skin, assets, side, fight);
		side.build(middle);
		fight.build(middle);
		top.build(middle, side, fight);

		stage.addActor(fight.getActor());
		stage.addActor(middle.getActor());
		stage.addActor(top.getActor());
		stage.addActor(side.getActor());

		stage.setDebugAll(debugLines); // draw debug lines
		calibrateStage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		calibrateStage(stage.getWidth(), stage.getHeight());
	}

	/** Set proper size and position to all actors */
	public void calibrateStage(float width, float height) {
		Actor top = this.top.getActor();
		Actor side = this.side.getActor();
		Actor middle = this.middle.getActor();
		Actor fight = this.fight.getActor();

		top.setSize(width, 30);
		top.setPosition(0, height - top.getHeight());

		side.setSize(260, height - top.getHeight());
		side.setPosition(0, 0);

		middle.setSize(width - side.getWidth(), height - top.getHeight());
		middle.setPosition(side.getWidth(), 0);

		fight.setSize(width - side.getWidth(), height - top.getHeight());
		fight.setPosition(side.getWidth(), 0);
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
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

}