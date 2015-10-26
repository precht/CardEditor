package com.mrloku.cardeditor.game;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;

public class Card {
	private static final String TAG = Card.class.getName();

	private ArrayList<Object> vals;

	public Card() {
		vals = new ArrayList<>();
		for (CARD_PROPERTY cp : CARD_PROPERTY.values())
			vals.add(cp.getType());
	}

	public Card(ArrayList<Object> vals) {
		this.vals = vals;
	}

	public ArrayList<Object> getVals() {
		return vals;
	}

	public void setVals(ArrayList<String> vals) {
		this.vals = strs2vals(vals);
	}

	public void updateVals(ArrayList<String> strs) {
		ArrayList<Object> newVals = strs2vals(strs);
		for (CARD_PROPERTY cp : CARD_PROPERTY.values()) {
			if (cp.type instanceof String) {
				if (((String) newVals.get(cp.ordinal())).length() > 0)
					this.vals.set(cp.ordinal(), newVals.get(cp.ordinal()));
			} else {
				if (((int) newVals.get(cp.ordinal())) >= 0)
					this.vals.set(cp.ordinal(), newVals.get(cp.ordinal()));
			}
		}
	}

	public void setVal(String val, CARD_PROPERTY cp) {
		try {
			vals.set(cp.ordinal(), cp.getType() instanceof String ? val : Integer.parseInt(val));
		} catch (NumberFormatException e) {
			Gdx.app.debug(TAG, val + " is not int");
		}
	}

	public static ArrayList<Object> str2vals(String str) {
		return strs2vals(new ArrayList<>(Arrays.asList(str.split("\\s+"))));
	}

	public static ArrayList<Object> strs2vals(ArrayList<String> strs) {
		ArrayList<Object> vals = new ArrayList<>();
		for (CARD_PROPERTY cp : CARD_PROPERTY.values()) {
			if (cp.type instanceof String)
				vals.add(strs.get(cp.ordinal()).replace('_', ' ').trim());
			else {
				vals.add(-1);
				try {
					int x = Integer.parseInt(strs.get(cp.ordinal()).replace('_', ' ').trim());
					vals.set(vals.size() - 1, x);
				} catch (NumberFormatException e) {
					Gdx.app.debug(TAG, strs.get(cp.ordinal()) + " is not int");
				}
			}
		}
		return vals;
	}

	public static String vals2str(ArrayList<Object> vals) {
		StringBuilder sb = new StringBuilder();
		for (CARD_PROPERTY cp : CARD_PROPERTY.values()) {
			sb.append(vals.get(cp.ordinal()).toString().replace(' ', '_')).append("_ ");
		}
		return sb.toString();
	}

	public static enum CARD_PROPERTY {
		TITLE(""), ATTACK(0), HEALTH(0), COST(0), ABILITY("");

		// remembers what types are stored in this property
		private final Object type;

		private CARD_PROPERTY(Object type) {
			this.type = type;
		}

		public Object getType() {
			return type;
		}
	}

}