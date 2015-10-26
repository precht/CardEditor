package com.mrloku.cardeditor.screens.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mrloku.cardeditor.game.Assets;
import com.mrloku.cardeditor.screens.widgets.Middle.VIEW_TYPE;

public class Top {
	private static final String TAG = Top.class.getName();

	private Skin skin;
	private Assets assets;
	private Middle middle;
	private Side side;
	private Fight fight;

	private Table actor;
	private TextButton tbut1, tbut2, tbut3, tbut4;

	public Top(Skin skin, Assets assets) {
		this.skin = skin;
		this.assets = assets;
	}

	public void build(Middle middle, Side side, Fight fight) {
		this.middle = middle;
		this.side = side;
		this.fight = fight;

		actor = new Table(skin);
		TextButtonChangeListener tbcl = new TextButtonChangeListener();
		tbut1 = new TextButton("Add Card", skin);
		tbut1.addListener(tbcl);
		tbut2 = new TextButton("Delete Card", skin);
		tbut2.addListener(tbcl);
		tbut3 = new TextButton("Save Deck", skin);
		tbut3.addListener(tbcl);
		tbut4 = new TextButton("Load Deck", skin);
		tbut4.addListener(tbcl);

		// set buttons size and position
		actor.defaults().space(5).size(120, 25);
		actor.add(tbut1).padLeft(5);
		actor.add(tbut2);
		actor.add(tbut3).expandX().right();
		actor.add(tbut4).expandX().left();
	}

	private class TextButtonChangeListener extends ChangeListener {
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			if (fight.isVisible())
				fight.buttonBack();
			if (!middle.unsaved()) {
				// need this to not delete card when in other view than card
				VIEW_TYPE lastView = middle.getView();
				// set middleActor view according to pressed key
				try {
					middle.setView(VIEW_TYPE
							.valueOf(((TextButton) actor).getText().toString().toUpperCase().replace(" ", "_")));
				} catch (Exception e) {
					Gdx.app.debug(TAG, "Failed to create VIEW_TYPE from button name");
				}
				switch (middle.getView()) {
				case ADD_CARD:
					String title = assets.addCard();
					side.reloadItems();
					side.setSelected(title);
					middle.setView(VIEW_TYPE.CARD);
					break;
				case DELETE_CARD:
					if (assets.hasCardInDeck()) {
						middle.setView(VIEW_TYPE.DELETE_CARD);
					} else {
						middle.setView(lastView);
					}
					break;
				default:
					break;
				}
				middle.update();
			}
		}
	}

	public Actor getActor() {
		return actor;
	}

}