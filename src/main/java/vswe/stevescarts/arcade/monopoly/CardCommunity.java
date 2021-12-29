package vswe.stevescarts.arcade.monopoly;

import java.util.ArrayList;

public abstract class CardCommunity extends Card {
	public static ArrayList<CardCommunity> cards;

	public CardCommunity(final String message) {
		super(message);
	}

	@Override
	public int getBackgroundV() {
		return 1;
	}

	static {
		(CardCommunity.cards = new ArrayList<>()).add(new CardCommunity("You just found a ton of buckets in the dungeon.") {
			@Override
			public void doStuff(final ArcadeMonopoly game, final Piece piece) {
				piece.addMoney(Note.IRON, 9, true);
			}

			@Override
			public int getNoteCount() {
				return 9;
			}

			@Override
			public Note getNote() {
				return Note.IRON;
			}

			@Override
			public String getMoneyPrefix() {
				return "Collect";
			}
		});
		CardCommunity.cards.add(new CardCommunity("D2") {
			@Override
			public void doStuff(final ArcadeMonopoly game, final Piece piece) {
			}
		});
		CardCommunity.cards.add(new CardCommunity("D3") {
			@Override
			public void doStuff(final ArcadeMonopoly game, final Piece piece) {
			}
		});
	}
}
