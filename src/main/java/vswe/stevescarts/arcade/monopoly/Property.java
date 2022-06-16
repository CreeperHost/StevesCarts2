package vswe.stevescarts.arcade.monopoly;

import com.mojang.blaze3d.vertex.PoseStack;
import vswe.stevescarts.client.guis.GuiMinecart;

import java.util.EnumSet;

public abstract class Property extends Place
{
    private String name;
    private int cost;
    private Piece owner;
    private PropertyGroup group;
    private boolean mortgaged;

    public Property(final ArcadeMonopoly game, final PropertyGroup group, final String name, final int cost)
    {
        super(game);
        (this.group = group).add(this);
        this.name = name;
        this.cost = cost;
    }

    public void drawValue(PoseStack matrixStack, GuiMinecart gui)
    {
        Note.drawValue(matrixStack, game, gui, 10, 103, 2, cost);
    }

    @Override
    public void drawText(PoseStack matrixStack, GuiMinecart gui, final EnumSet<PLACE_STATE> states)
    {
        game.getModule().drawSplitString(gui, name, 3 + gui.getGuiLeft(), getTextY() + gui.getGuiTop(), 70, true, 4210752);
    }

    protected abstract int getTextY();

    public int getCost()
    {
        return cost;
    }

    public void setOwner(final Piece val)
    {
        owner = val;
    }

    public Piece getOwner()
    {
        return owner;
    }

    public boolean hasOwner()
    {
        return owner != null;
    }

    @Override
    public boolean onPieceStop(final Piece piece)
    {
        return owner == null || owner == piece || mortgaged;
    }

    public PropertyGroup getGroup()
    {
        return group;
    }

    public abstract int getRentCost();

    public int getMortgageValue()
    {
        return getCost() / 2;
    }

    public int getOwnedInGroup()
    {
        int owned = 0;
        for (final Property property : getGroup().getProperties())
        {
            if (property.getOwner() == getOwner() && !property.isMortgaged())
            {
                ++owned;
            }
        }
        return owned;
    }

    public boolean isMortgaged()
    {
        return mortgaged;
    }

    public boolean canMortgage()
    {
        return true;
    }

    public void mortgage()
    {
        mortgaged = true;
    }

    public int getUnMortgagePrice()
    {
        return (int) (getMortgageValue() * 1.1f);
    }

    public void unMortgage()
    {
        mortgaged = false;
    }
}
