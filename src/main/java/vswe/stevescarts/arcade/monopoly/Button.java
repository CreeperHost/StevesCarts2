package vswe.stevescarts.arcade.monopoly;

public abstract class Button
{
    public String getName()
    {
        return "Undefined";
    }

    public boolean isVisible()
    {
        return false;
    }

    public boolean isEnabled()
    {
        return false;
    }

    public boolean isVisibleForPlayer()
    {
        return true;
    }

    public void onClick()
    {
    }

    public boolean isReallyEnabled(final ArcadeMonopoly game)
    {
        return game.getCurrentPiece().getController() == Piece.CONTROLLED_BY.PLAYER && isEnabled();
    }

    public boolean isReallyVisible(final ArcadeMonopoly game)
    {
        return isVisibleForPlayer() && isVisible();
    }
}
