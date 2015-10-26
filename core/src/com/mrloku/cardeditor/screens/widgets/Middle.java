package com.mrloku.cardeditor.screens.widgets;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.mrloku.cardeditor.game.Assets;
import com.mrloku.cardeditor.game.Card.CARD_PROPERTY;

public class Middle {
	public static final String TAG = Middle.class.getName();
	private Skin skin;
	private Assets assets;
	private Side side;
	private Fight fight;

	private Stack actor;
	private ArrayList<WidgetGroup> groups;

	private VIEW_TYPE view;

	public Middle(Skin skin, Assets assets, Side side, Fight fight) {
		this.skin = skin;
		this.assets = assets;
		this.side = side;
		this.fight = fight;

		build();
		view = VIEW_TYPE.START;
		update();
	}

	public void build() {
		actor = new Stack();
		groups = new ArrayList<>();

		// add all tables to tabs in proper order
		for (VIEW_TYPE vt : VIEW_TYPE.values()) {
			try {
				groups.add((WidgetGroup) this.getClass().getMethod("build" + vt.name()).invoke(this));
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				Gdx.app.debug(TAG, "FAILED TO INVOKE " + vt.name());
				e.printStackTrace();
			}
		}

		for (WidgetGroup g : groups) {
			// Gdx.app.debug(TAG, g.toString());
			g.setFillParent(true);
			g.setVisible(false);
			actor.add(g);
		}
	}

	private Image cardImg;
	private ArrayList<Label> cardLbs1, cardLbs2;
	private ArrayList<TextField> cardTxfs;
	private TextButton cardTxb1, cardTxb2, cardTxb3;

	public Table buildCARD() {
		Table tab = new Table();
		// card picture
		cardImg = new Image(assets.getCardTexture());
		tab.add(cardImg).size(160, 240).pad(20).padTop(40);
		// properties
		cardLbs1 = new ArrayList<>();
		cardLbs2 = new ArrayList<>();
		cardTxfs = new ArrayList<>();
		for (CARD_PROPERTY cp : CARD_PROPERTY.values()) {
			cardLbs1.add(new Label(cp.name() + ":", skin));
			cardLbs2.add(new Label("", skin));
			TextField tf = new TextField("", skin);
			tf.setTextFieldListener(new TextFieldListener() {
				@Override
				public void keyTyped(TextField textField, char c) {
					if (c == '\n' || c == '\r')
						cardTxb2.fire(new ChangeEvent());
				}
			});
			cardTxfs.add(tf);
		}
		Table properts = new Table();
		properts.defaults().spaceLeft(20).spaceRight(20);
		// add properties apart from last one (ability), it need more space
		for (int i = 0; i < CARD_PROPERTY.values().length - 1; i++) {
			properts.add(cardLbs1.get(i)).width(140).left();
			properts.add(cardLbs2.get(i)).width(140).left();
			properts.add(cardTxfs.get(i)).width(140).left().row();
		}
		// add ability
		properts.add(cardLbs1.get(cardLbs1.size() - 1)).width(80).left().row();
		properts.add(cardLbs2.get(cardLbs2.size() - 1)).colspan(3).width(460).right().space(8).row();
		properts.add(cardTxfs.get(cardTxfs.size() - 1)).colspan(3).width(460).right().row();
		tab.add(properts).size(500, 240).expandX().top().padTop(40).left();
		// change buttons
		tab.row().size(120, 45).expandY().top().padTop(40);
		cardTxb1 = new TextButton("CANCEL", skin);
		cardTxb1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				for (TextField tf : cardTxfs) {
					tf.setText("");
				}
			}
		});
		tab.add(cardTxb1).right();
		cardTxb2 = new TextButton("SAVE", skin);
		cardTxb2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				ArrayList<String> newVals = new ArrayList<>();
				/*
				 * not good idea, ability can be empty // delete white spaces at
				 * beginning and end for (CARD_PROPERTY cp :
				 * CARD_PROPERTY.values()) if (cp.getType() instanceof String)
				 * cardTxfs.get(cp.ordinal()).setText(cardTxfs.get(cp.ordinal())
				 * .getText().trim());
				 */
				// remember title to set proper card number after reloading
				// titles to side pane
				String title = cardTxfs.get(CARD_PROPERTY.TITLE.ordinal()).getText().trim();
				cardTxfs.get(CARD_PROPERTY.TITLE.ordinal()).setText(title);
				for (TextField tf : cardTxfs) {
					newVals.add(tf.getText());
					tf.setText("");
				}
				assets.updateCardVals(side.getSelectedIndex(), newVals);
				side.reloadItems();
				// new title update side list (and sideWidget updates labels)
				if (title.length() > 0)
					side.setSelected(title);
				// if not update labels,
				else
					update();
			}
		});
		tab.add(cardTxb2).left().padLeft(100);
		cardTxb3 = new TextButton("FIGHT", skin);
		cardTxb3.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				setVisible(false);
				fight.setVisible(true);
				update();
			}
		});
		tab.add(cardTxb3).padRight(80);

		return tab;
	}

	private Label startLb1, startLb2, startLb3, startLb4;

	public Table buildSTART() {
		Table tab = new Table();
		startLb1 = new Label("Hello! :)", skin);
		startLb2 = new Label("Load existing deck", skin);
		startLb3 = new Label("or", skin);
		startLb4 = new Label("add card to create new one.", skin);
		tab.add(startLb1).row();
		tab.add(startLb2).row();
		tab.add(startLb3).row();
		tab.add(startLb4).padBottom(200);
		return tab;
	}

	private Label saveLb1, saveLb2;
	private TextField saveTxf;
	private TextButton saveTxb1, saveTxb2;

	public Table buildSAVE_DECK() {
		Table tab = new Table();
		tab.defaults().expandX().space(20).height(40);
		// top text
		saveLb1 = new Label("Enter deck name:", skin);
		tab.add(saveLb1).colspan(2).row();
		// text field
		saveTxf = new TextField("", skin);
		saveTxf.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {
				if (c == '\n' || c == '\r')
					saveTxb2.fire(new ChangeEvent());
			}
		});
		tab.add(saveTxf).colspan(2).width(200);
		// buttons: cancel and save
		tab.row().width(120).space(30);
		saveTxb1 = new TextButton("CANCEL", skin);
		saveTxb1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				view = VIEW_TYPE.CARD;
				update();
			}
		});
		tab.add(saveTxb1).right();
		saveTxb2 = new TextButton("SAVE", skin);
		saveTxb2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (!assets.correctName(saveTxf.getText())) {
					saveLb2.setText("Incorrect name. Allowed characters: \" a-zA-Z0-9_-\"");
					saveLb2.setVisible(true);
				} else if (assets.saveDeck(saveTxf.getText(), false)) {
					view = VIEW_TYPE.CARD;
					update();
				} else {
					saveLb2.setText("Failed to save deck as: " + saveTxf.getText() + ". Cause unknown.");
					saveLb2.setVisible(true);
				}
			}
		});
		tab.add(saveTxb2).left().row();
		// bottom text
		saveLb2 = new Label("", skin);
		saveLb2.setVisible(false);
		tab.add(saveLb2).colspan(2).padBottom(100);

		return tab;
	}

	private Label loadLb1, loadLb2;
	private SelectBox<String> loadSb;
	private TextButton loadTxb1, loadTxb2;

	public WidgetGroup buildLOAD_DECK() {
		Table tab = new Table();
		tab.defaults().expandX().space(20).height(40);
		// top label
		loadLb1 = new Label("Choose deck name:", skin);
		tab.add(loadLb1).colspan(2).row();
		// select box
		loadSb = new SelectBox<String>(skin);
		tab.add(loadSb).colspan(2).width(200);
		// buttons: cancel and save
		tab.row().width(120).space(30);
		loadTxb1 = new TextButton("CANCEL", skin);
		loadTxb1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				view = VIEW_TYPE.CARD;
				update();
			}
		});
		tab.add(loadTxb1).right();
		loadTxb2 = new TextButton("LOAD", skin);
		loadTxb2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (assets.loadDeck(loadSb.getSelected())) {
					view = VIEW_TYPE.CARD;
					side.reloadItems();
					update();
				} else {
					loadLb2.setVisible(true);
				}
			}
		});
		tab.add(loadTxb2).left().row();
		// label failed
		loadLb2 = new Label("Failed to load file: " + loadSb.getSelected() + "Cause unknown.", skin);
		loadLb2.setVisible(false);
		tab.add(loadLb2).colspan(2).padBottom(100);

		return tab;
	}

	public Table buildADD_CARD() {
		Table tab = new Table();
		return tab;
	}

	private TextButton delTxb1, delTxb2;
	private Label delLb;

	public Table buildDELETE_CARD() {
		Table tab = new Table();
		tab.defaults().expandX().space(30).height(40);
		// top text
		delLb = new Label("", skin);
		tab.add(delLb).colspan(2);
		// buttons: no and yes
		tab.row().width(100).padBottom(150);
		delTxb1 = new TextButton("NO", skin);
		delTxb1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				view = VIEW_TYPE.CARD;
				update();
			}
		});
		tab.add(delTxb1).right();
		delTxb2 = new TextButton("YES", skin);
		delTxb2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				int selected = side.getSelectedIndex();
				assets.deleteCard(selected);
				setView(VIEW_TYPE.CARD);
				side.reloadItems();
				if (assets.hasCardInDeck()) {
					side.setSelected(Math.max(selected - 1, 0));
				}

			}
		});
		tab.add(delTxb2).left();

		return tab;
	}

	public void update() {
		if (actor.isVisible()) {
			for (WidgetGroup g : groups)
				g.setVisible(false);
			groups.get(view.ordinal()).setVisible(true);

			switch (view) {
			case CARD:
				if (side.getSelectedIndex() < 0) {
					groups.get(view.ordinal()).setVisible(false);
					view = VIEW_TYPE.START;
					groups.get(view.ordinal()).setVisible(true);
				} else {
					ArrayList<Object> vals = assets.getCardVals(side.getSelectedIndex());
					for (CARD_PROPERTY cp : CARD_PROPERTY.values()) {
						cardLbs2.get(cp.ordinal()).setText(vals.get(cp.ordinal()).toString());
						cardTxfs.get(cp.ordinal()).setText("");
					}
				}
				break;
			case LOAD_DECK:
				loadSb.setItems(assets.getSavedDecks());
				loadLb2.setVisible(false);
				break;
			case SAVE_DECK:
				saveTxf.setText(assets.getDeckName());
				saveLb2.setVisible(false);
				break;
			case DELETE_CARD:
				delLb.setText("Delete card: \"" + assets.getCardTitle(side.getSelectedIndex()) + "\" ?");
				break;
			default:
				break;
			}
		} else {
			if (fight.isFinished()) {
				fight.buttonBack();
			} else {
				fight.update();
			}
		}
	}

	public void setView(VIEW_TYPE view) {
		this.view = view;
	}

	public Actor getActor() {
		return actor;
	}

	public boolean unsaved() {
		for (TextField tf : cardTxfs)
			if (tf.getText().length() > 0)
				return true;
		return false;
	}

	public VIEW_TYPE getView() {
		return view;
	}

	public void setVisible(boolean visible) {
		actor.setVisible(visible);
	}

	public enum VIEW_TYPE {
		CARD, START, SAVE_DECK, LOAD_DECK, DELETE_CARD, ADD_CARD;
	}

}