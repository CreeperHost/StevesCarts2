package vswe.stevescarts.modules.realtimers;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;

import java.util.List;

public class ModuleExperience extends ModuleBase
{
    private static final int MAX_EXPERIENCE_AMOUNT = 1500;
    private DataParameter<Byte> EXPERIENCE;

    public ModuleExperience(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public void update()
    {
        if (!getCart().level.isClientSide)
        {
            final List list = getCart().level.getEntities(getCart(), getCart().getBoundingBox().inflate(3.0, 1.0, 3.0));
            for (int e = 0; e < list.size(); ++e)
            {
                if (list.get(e) instanceof ExperienceOrbEntity)
                {
                    addExperience(((ExperienceOrbEntity) list.get(e)).getValue());
                    if (getExperienceAmount() > MAX_EXPERIENCE_AMOUNT)
                    {
                        setExperienceAmount(MAX_EXPERIENCE_AMOUNT);
                    }
                    else
                    {
                        ((ExperienceOrbEntity) list.get(e)).remove();
                    }
                }
            }
        }
    }

    private void addExperience(int amount)
    {
        int val = getExperienceAmount();
        val += amount;
        setExperienceAmount(val);
    }

    public int getExperienceAmount()
    {
        final int val = getDw(EXPERIENCE);
        if (val < 0)
        {
            return val + 256;
        }
        return val;
    }

    private void setExperienceAmount(final int val)
    {
        if (!isPlaceholder())
        {
            updateDw(EXPERIENCE, (byte) val);
        }
    }

    @Override
    public int numberOfDataWatchers()
    {
        return 1;
    }

    @Override
    public void initDw()
    {
        EXPERIENCE = createDw(DataSerializers.BYTE);
        registerDw(EXPERIENCE, (byte) 0);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawMouseOver(MatrixStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
        drawStringOnMouseOver(matrixStack, gui, Localization.MODULES.ATTACHMENTS.EXPERIENCE_LEVEL.translate(String.valueOf(getExperienceAmount()), String.valueOf(1500)) + "\n" + Localization.MODULES.ATTACHMENTS.EXPERIENCE_EXTRACT.translate() + "\n" + Localization.MODULES.ATTACHMENTS.EXPERIENCE_PLAYER_LEVEL.translate(String.valueOf(getClientPlayer().experienceLevel)), x, y, getContainerRect());
    }

    @Override
    public int numberOfGuiData()
    {
        return 1;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(MatrixStack matrixStack, GuiMinecart gui)
    {
        drawString(matrixStack, gui, Localization.MODULES.ATTACHMENTS.EXPERIENCE.translate(), 8, 6, 4210752);
    }

    private int[] getContainerRect()
    {
        return new int[]{10, 15, 26, 65};
    }

    private int[] getContentRect(final float part)
    {
        final int[] cont = getContainerRect();
        final int normalHeight = cont[3] - 4;
        final int currentHeight = (int) (normalHeight * part);
        return new int[]{cont[0] + 2, cont[1] + 2 + normalHeight - currentHeight, cont[2] - 4, currentHeight, normalHeight};
    }

    @OnlyIn(Dist.CLIENT)
    private void drawContent(MatrixStack matrixStack, GuiMinecart gui, final int x, final int y, final int id)
    {
        final int lowerLevel = id * 1500 / 3;
        final int currentLevel = getExperienceAmount() - lowerLevel;
        float part = 3.0f * currentLevel / 1500.0f;
        if (part > 1.0f)
        {
            part = 1.0f;
        }
        final int[] content = getContentRect(part);
        drawImage(matrixStack, gui, content, 4 + content[2] * (id + 1), content[4] - content[3]);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawBackground(MatrixStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource("/gui/experience.png");
        for (int i = 0; i < 3; ++i)
        {
            drawContent(matrixStack, gui, x, y, i);
        }
        drawImage(matrixStack, gui, getContainerRect(), 0, inRect(x, y, getContainerRect()) ? 65 : 0);
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (inRect(x, y, getContainerRect()))
        {
            sendPacket(0);
        }
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @Override
    public boolean hasSlots()
    {
        return false;
    }

    @Override
    public int guiWidth()
    {
        return 70;
    }

    @Override
    public int guiHeight()
    {
        return 84;
    }

    @Override
    protected int numberOfPackets()
    {
        return 1;
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final PlayerEntity player)
    {
        player.giveExperiencePoints(Math.min(getExperienceAmount(), 50));
        setExperienceAmount(Math.min(getExperienceAmount(), 50));
    }

    @Override
    protected void Load(final CompoundNBT tagCompound, final int id)
    {
        setExperienceAmount(tagCompound.getShort(generateNBTName("Experience", id)));
    }

    @Override
    protected void Save(final CompoundNBT tagCompound, final int id)
    {
        tagCompound.putShort(generateNBTName("Experience", id), (short) getExperienceAmount());
    }
}
