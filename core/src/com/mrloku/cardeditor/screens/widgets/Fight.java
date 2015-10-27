package com.mrloku.cardeditor.screens.widgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.mrloku.cardeditor.game.Assets;
import com.mrloku.cardeditor.game.Card.CARD_PROPERTY;

public class Fight {
	// private static final String TAG = Fight.class.getName();

	private Assets assets;
	private Skin skin;
	private Side side;
	@SuppressWarnings("unused")
	private Middle middle;

	private Table actor;

	public Fight(Assets assets, Skin skin, Side side) {
		this.assets = assets;
		this.skin = skin;
		this.side = side;
	}

	private Label lb1, lb2, lb3, lb11, lb33;
	private int card1pos, card2pos;
	private Table inTab;
	private ScrollPane sp;
	private TextButton txb1, txb2;

	public void build(Middle middle) {
		this.middle = middle;
		actor = new Table();
		actor.setVisible(false);

		lb1 = new Label("", skin);
		lb2 = new Label("VS", skin);
		lb3 = new Label("", skin);
		lb11 = new Label("", skin);
		lb33 = new Label("", skin);

		inTab = new Table();
		sp = new ScrollPane(inTab);

		txb1 = new TextButton("BACK", skin);
		txb1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				lb1.setText("");
				lb11.setText("");
				lb3.setText("");
				lb33.setText("");
				finished = false;
				setVisible(false);
				middle.setVisible(true);
				middle.update();
			}
		});
		txb2 = new TextButton("GO!", skin);
		txb2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (lb1.getText().length > 0 && lb2.getText().length > 0) {
					actor.setVisible(false);
					performFight();
				}
			}
		});

		actor.add(lb1).width(150);
		actor.add(lb2).width(50);
		actor.add(lb3).width(150).row();
		actor.add(lb11).left();
		actor.add(new Label("", skin));
		actor.add(lb33).left().row();
		actor.add(sp).colspan(3).height(300).padRight(20).row();
		actor.add(txb1).size(120, 45).padBottom(150);
		actor.add(new Label("", skin));
		actor.add(txb2).size(120, 45).padBottom(150);

	}

	private boolean finished = false;

	private void performFight() {
		int a1 = (int) assets.getCardVals(card1pos).get(CARD_PROPERTY.ATTACK.ordinal());
		int h1 = (int) assets.getCardVals(card1pos).get(CARD_PROPERTY.HEALTH.ordinal());
		int a2 = (int) assets.getCardVals(card2pos).get(CARD_PROPERTY.ATTACK.ordinal());
		int h2 = (int) assets.getCardVals(card2pos).get(CARD_PROPERTY.HEALTH.ordinal());

		h1 += a2;
		h2 += a1;
		// i < 99 to avoid endless loop
		for (int i = 0; h1 > 0 && h2 > 0 && i < 99; i++) {
			h1 -= a2;
			h2 -= a1;
			inTab.add(new Label(String.format("%5d", h1), skin)).left();
			inTab.add(new Label(String.format("%-10s", ":  (" + i + ") ") + ":", skin)).width(50);
			inTab.add(new Label(String.format("%5d", h2), skin)).right().row();
		}
		inTab.add(new Label("", skin)).row();
		inTab.add(new Label(h1 > 0 ? "WINNER" : "LOSER", skin)).width(80).left();
		inTab.add(new Label("", skin)).width(50);
		inTab.add(new Label(h2 > 0 ? "WINNER" : "LOSER", skin)).width(80).right().row();

		finished = true;
	}

	public void update() {
		if (!finished) {
			inTab.clear();
			txb2.setVisible(true);

			if (lb1.getText().length == 0) {
				lb1.setText(side.getSelected());
				card1pos = side.getSelectedIndex();

				lb11.setText(String.format("%-8s",
						"A=" + assets.getCardVals(card1pos).get(CARD_PROPERTY.ATTACK.ordinal()).toString())
						+ String.format("%s",
								"H=" + assets.getCardVals(card1pos).get(CARD_PROPERTY.HEALTH.ordinal()).toString()));
			} else {
				lb3.setText(side.getSelected());
				card2pos = side.getSelectedIndex();
				lb33.setText(String.format("%-8s",
						"A=" + assets.getCardVals(card2pos).get(CARD_PROPERTY.ATTACK.ordinal()).toString())
						+ String.format("%s",
								"H=" + assets.getCardVals(card2pos).get(CARD_PROPERTY.HEALTH.ordinal()).toString()));
			}
		}
	}

	public Actor getActor() {
		return actor;
	}

	public void setVisible(boolean visible) {
		actor.setVisible(visible);
	}

	public boolean isVisible() {
		return actor.isVisible();
	}

	public void buttonBack() {
		txb1.fire(new ChangeEvent());
	}

	public boolean isFinished() {
		return finished;
	}

}