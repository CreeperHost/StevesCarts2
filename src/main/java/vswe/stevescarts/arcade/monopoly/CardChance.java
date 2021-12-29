package vswe.stevescarts.arcade.monopoly;

import java.util.ArrayList;

public abstract class CardChance extends Card {
	public static ArrayList<CardChance> cards;

	public CardChance(final String message) {
		super(message);
	}

	@Override
	public int getBackgroundV() {
		return 0;
	}

	static {
		(CardChance.cards = new ArrayList<>()).add(new CardChance("Jaded managed to crash the server, again. The server had to roll back. Go 3 steps back.") {
			@Override
			public void doStuff(final ArcadeMonopoly game, final Piece piece) {
				game.movePiece(-3);
			}
		});
		CardChance.cards.add(new CardChance("C2") {
			@Override
			public void doStuff(final ArcadeMonopoly game, final Piece piece) {
				piece.bankrupt(null);
			}
		});
		CardChance.cards.add(new CardChance("You found a linking book in the middle of nowhere and foolishly you used it. You shouldn't have done that, now you're trapped in a void age.") {
			@Override
			public void doStuff(final ArcadeMonopoly game, final Piece piece) {
				piece.goToJail();
			}
		});
	}
}
