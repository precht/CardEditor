package com.mrloku.cardeditor.screens.widgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mrloku.cardeditor.game.Assets;
import com.mrloku.cardeditor.screens.widgets.Middle.VIEW_TYPE;

public class Side {
	static final String TAG = Side.class.getName();

	private Skin skin;
	private Assets assets;
	private Middle middle;

	private List<String> list;
	private ScrollPane scrollPane;
	private Table actor;
	private int selected = 0;

	public Side(Skin skin, Assets assets) {
		this.skin = skin;
		this.assets = assets;
	}

	public void build(Middle middle) {
		this.middle = middle;
		actor = new Table();
		list = new List<>(skin);
		list.addListener(new ListChangeListener());
		list.setItems(assets.getCardsTitles());
		scrollPane = new ScrollPane(list, skin);
		actor.add(scrollPane).expand().fill();
	}

	private class ListChangeListener extends ChangeListener {
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			middle.setView(VIEW_TYPE.CARD);
			if (middle.unsaved()) {
				list.setSelectedIndex(selected);
			} else {
				selected = list.getSelectedIndex();
				middle.update();
			}
		}
	}

	public void reloadItems() {
		list.setItems(assets.getCardsTitles());
	}

	public Actor getActor() {
		return actor;
	}

	public int getSelectedIndex() {
		return list.getSelectedIndex();
	}

	public String getSelected() {
		return list.getSelected();
	}

	public void setSelected(String str) {
		list.setSelected(str);
	}

	public void setSelected(int pos) {
		list.setSelectedIndex(pos);
	}

}