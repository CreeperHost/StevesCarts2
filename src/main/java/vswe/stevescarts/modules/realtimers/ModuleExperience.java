package vswe.stevescarts.modules.realtimers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.api.modules.ModuleBase;

import java.util.List;

public class ModuleExperience extends ModuleBase
{
    private static final int MAX_EXPERIENCE_AMOUNT = 1500;
    private EntityDataAccessor<Integer> EXPERIENCE;

    public ModuleExperience(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public void update() {
        if (getCart().level().isClientSide) return;

        List<Entity> list = getCart().level().getEntities(getCart(), getCart().getBoundingBox().inflate(3.0, 1.0, 3.0));
        for (Entity entity : list) {
            if (entity instanceof ExperienceOrb) {
                addExperience(((ExperienceOrb) entity).getValue());
                if (getExperienceAmount() > MAX_EXPERIENCE_AMOUNT) {
                    setExperienceAmount(MAX_EXPERIENCE_AMOUNT);
                } else {
                    entity.remove(Entity.RemovalReason.DISCARDED);
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
        int val = getDw(EXPERIENCE);
        if (val < 0)
        {
            return val + 256;
        }
        return val;
    }

    private void setExperienceAmount(int val)
    {
        if (!isPlaceholder())
        {
            updateDw(EXPERIENCE, val);
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
        EXPERIENCE = createDw(EntityDataSerializers.INT);
        registerDw(EXPERIENCE, 0);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawMouseOver(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        drawStringOnMouseOver(guiGraphics, gui, Localization.MODULES.ATTACHMENTS.EXPERIENCE_LEVEL.translate(String.valueOf(getExperienceAmount()), String.valueOf(1500)) + "\n" +
                Localization.MODULES.ATTACHMENTS.EXPERIENCE_EXTRACT.translate() + "\n" +
                Localization.MODULES.ATTACHMENTS.EXPERIENCE_EXTRACT_ALL.translate() + "\n" +
                Localization.MODULES.ATTACHMENTS.EXPERIENCE_PLAYER_LEVEL.translate(String.valueOf(getClientPlayer().experienceLevel)), x, y, getContainerRect());
    }

    @Override
    public int numberOfGuiData()
    {
        return 1;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        drawString(guiGraphics, gui, Localization.MODULES.ATTACHMENTS.EXPERIENCE.translate(), 8, 6, 4210752);
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
    private void drawContent(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y, final int id)
    {
        final int lowerLevel = id * 1500 / 3;
        final int currentLevel = getExperienceAmount() - lowerLevel;
        float part = 3.0f * currentLevel / 1500.0f;
        if (part > 1.0f)
        {
            part = 1.0f;
        }
        final int[] content = getContentRect(part);
        drawImage(guiGraphics, gui, content, 4 + content[2] * (id + 1), content[4] - content[3]);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawBackground(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource("/gui/experience.png");
        for (int i = 0; i < 3; ++i)
        {
            drawContent(guiGraphics, gui, x, y, i);
        }
        drawImage(guiGraphics, gui, getContainerRect(), 0, inRect(x, y, getContainerRect()) ? 65 : 0);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (inRect(x, y, getContainerRect()))
        {
            sendPacket(0, (byte) (Screen.hasShiftDown() ? 1 : 0));
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
    protected void receivePacket(final int id, final byte[] data, final Player player)
    {
        int give = data[0] == 1 ? getExperienceAmount() : Math.min(getExperienceAmount(), 50);
        player.giveExperiencePoints(give);
        setExperienceAmount(getExperienceAmount() - give);
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id)
    {
        setExperienceAmount(tagCompound.getShort(generateNBTName("Experience", id)));
    }

    @Override
    protected void Save(final CompoundTag tagCompound, final int id)
    {
        tagCompound.putShort(generateNBTName("Experience", id), (short) getExperienceAmount());
    }
}
