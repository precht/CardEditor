package com.mrloku.cardeditor.game;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.utils.Disposable;
import com.mrloku.cardeditor.game.Card.CARD_PROPERTY;

public class Assets implements Disposable {

	private ArrayList<Card> deck = new ArrayList<>();
	private String deckPath;
	private String deckName = "";
	private Texture cardTexture;
	public static final char separator = findSeparator();

	public static char findSeparator() {
		if (System.getProperty("os.name").startsWith("Windows"))
			return '\\';
		else
			return '/';
	}

	public Assets() {
		System.out.println(separator);
		cardTexture = new Texture(Gdx.files.internal("cards" + separator + "empty-card.png"));
		cardTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		setDeckPath();
		System.out.println(deckPath);
	}

	private void setDeckPath() {
		String pwd = null;
		try {
			pwd = Assets.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			pwd = pwd.substring(0, pwd.lastIndexOf(separator) + 1);
		} catch (URISyntaxException e) {
		}
		deckPath = pwd + "decks" + separator;
	}

	public String addCard() {
		int i = 1;
		while (Arrays.asList(getCardsTitles()).contains("new card " + i))
			i++;
		ArrayList<String> strs = new ArrayList<>();
		for (CARD_PROPERTY cp : CARD_PROPERTY.values())
			strs.add(cp.getType().toString());
		strs.set(CARD_PROPERTY.TITLE.ordinal(), "new card " + i);
		addCard(Card.strs2vals(strs));
		return "new card " + i;
	}

	public void addCard(ArrayList<Object> vals) {
		deck.add(new Card(vals));
		sortDeck();
	}

	private void addCardNoSort(ArrayList<Object> vals) {
		deck.add(new Card(vals));
	}

	public void deleteCard(int posInDeck) {
		deck.remove(posInDeck);
	}

	private void sortDeck() {
		Collections.sort(deck, new CardComparator());
	}

	class CardComparator implements Comparator<Card> {
		@Override
		public int compare(Card o1, Card o2) {
			return ((String) o1.getVals().get(CARD_PROPERTY.TITLE.ordinal()))
					.compareTo(((String) o2.getVals().get(CARD_PROPERTY.TITLE.ordinal())));
		}
	}

	public String[] getCardsTitles() {
		String[] strs = new String[deck.size()];
		for (int i = 0; i < deck.size(); i++)
			strs[i] = (String) deck.get(i).getVals().get(CARD_PROPERTY.TITLE.ordinal());
		return strs;
	}

	public String getCardTitle(int posInDeck) {
		return deck.get(posInDeck).getVals().get(CARD_PROPERTY.TITLE.ordinal()).toString();
	}

	public ArrayList<Object> getCardVals(int posInDeck) {
		return deck.get(posInDeck).getVals();
	}

	public void updateCardVals(int posInDeck, ArrayList<String> newVals) {
		// do not sort when not necessarily
		String title = deck.get(posInDeck).getVals().get(CARD_PROPERTY.TITLE.ordinal()).toString();
		deck.get(posInDeck).updateVals(newVals);
		if (title != deck.get(posInDeck).getVals().get(CARD_PROPERTY.TITLE.ordinal()).toString())
			sortDeck();
	}

	public boolean loadDeck(String name) {
		FileHandle file = Gdx.files.absolute(deckPath + name);
		if (!file.exists())
			return false;
		deck.clear();
		String[] strs = file.readString().split("\\n+");
		for (int i = 1; i < strs.length; i++)
			addCardNoSort(Card.str2vals(strs[i]));
		deckName = name;
		return true;
	}

	public boolean saveDeck(String name, boolean soft) {
		FileHandle file = Gdx.files.absolute(deckPath + name);
		// if soft is true do not override existing file
		if (file.exists() && soft)
			return false;
		file.writeString("", false);
		for (CARD_PROPERTY cp : CARD_PROPERTY.values())
			file.writeString(cp.name() + " ", true);
		file.writeString("\n", true);
		for (Card cd : deck)
			file.writeString(Card.vals2str(cd.getVals()) + "\n", true);
		deckName = name;
		return true;
	}

	public boolean correctName(String name) {
		return name.matches("\\w+[\\w\\s_-]*");
	}

	public String getDeckName() {
		return deckName;
	}

	public int getDeckSize() {
		return deck.size();
	}

	public boolean hasCardInDeck() {
		return deck.size() > 0;
	}

	public Texture getCardTexture() {
		return cardTexture;
	}

	public String[] getSavedDecks() {
		FileHandle dir = Gdx.files.absolute(deckPath);
		ArrayList<String> list = new ArrayList<>();
		for (FileHandle file : dir.list())
			list.add(file.name());
		return list.toArray(new String[list.size()]);
	}

	@Override
	public void dispose() {
		cardTexture.dispose();
	}

}