package vswe.stevescarts.arcade.monopoly;

public class Jail extends CornerPlace
{
    public Jail(final ArcadeMonopoly game)
    {
        super(game, 1);
    }

    @Override
    protected int getPieceYPosition(final int area)
    {
        return (area == 1) ? 30 : 95;
    }

    @Override
    protected int getAllowedWidth(final int area)
    {
        return (area == 1) ? 90 : 122;
    }

    @Override
    public int getPieceAreaCount()
    {
        return 2;
    }

    @Override
    public int getPieceAreaForPiece(final Piece piece)
    {
        return piece.isInJail() ? 1 : 0;
    }
}
