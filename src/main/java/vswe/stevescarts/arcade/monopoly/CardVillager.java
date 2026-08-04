package vswe.stevescarts.arcade.monopoly;

import java.util.ArrayList;

public abstract class CardVillager extends Card {
    public static ArrayList<CardVillager> cards;

    static {
        (CardVillager.cards = new ArrayList<>()).add(new CardVillager("No, I'm a helicopter.") {
            @Override
            public void doStuff(final ArcadeMonopoly game, final Piece piece) {
            }
        });
    }

    public CardVillager(final String message) {
        super(message);
    }

    @Override
    public int getBackgroundV() {
        return 2;
    }
}
