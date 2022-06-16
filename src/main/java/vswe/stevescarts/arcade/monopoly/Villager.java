package vswe.stevescarts.arcade.monopoly;

public class Villager extends CardPlace
{
    public Villager(final ArcadeMonopoly game)
    {
        super(game);
    }

    @Override
    protected int getTextureId()
    {
        return 9;
    }

    @Override
    public Card getCard()
    {
        return CardVillager.cards.get(game.getModule().getCart().random.nextInt(CardVillager.cards.size()));
    }
}
