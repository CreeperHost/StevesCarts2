package vswe.stevescarts.arcade.monopoly;

public class Chance extends CardPlace
{
    public Chance(final ArcadeMonopoly game)
    {
        super(game);
    }

    @Override
    protected int getTextureId()
    {
        return 0;
    }

    @Override
    public Card getCard()
    {
        return CardChance.cards.get(game.getModule().getCart().random.nextInt(CardChance.cards.size()));
    }
}
