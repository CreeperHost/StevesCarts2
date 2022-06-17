package vswe.stevescarts.modules;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.client.guis.buttons.ButtonBase;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.containers.slots.SlotBase;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.ButtonComparator;
import vswe.stevescarts.helpers.NBTHelper;
import vswe.stevescarts.helpers.SimulationInfo;
import vswe.stevescarts.modules.data.ModuleData;
import vswe.stevescarts.network.PacketHandler;
import vswe.stevescarts.network.packets.PacketMinecartButton;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * The base for all modules. This is what's used by the cart to add features, models and interfaces for the cart. should not be
 * confused with ModuleData which is the data used for adding a module to the cart in the Cart Assembler.
 *
 * @author Vswe
 */
public abstract class ModuleBase
{
    private EntityMinecartModular cart;
    @Nonnull
    private NonNullList<ItemStack> cargo;
    private int offSetX;
    private int offSetY;
    private int guiDataOffset;
    private int packetOffset;
    private ArrayList<ButtonBase> buttons;
    protected int slotGlobalStart;
    private byte moduleId;
    private ArrayList<ModelCartbase> models;
    protected ArrayList<SlotBase> slotList;
    private int moduleButtonId;

    /**
     * Creates a new instance of this module, the module will be created at the given cart.
     *
     * @param cart The cart this module is created on
     */
    public ModuleBase(final EntityMinecartModular cart)
    {
        moduleButtonId = 0;
        this.cart = cart;
        cargo = NonNullList.withSize(getInventorySize(), ItemStack.EMPTY);
    }

    /**
     * Initializes the modules, this is done after all modules has been added to the cart, and given proper IDs and everything.
     */
    public void init()
    {
        if (useButtons())
        {
            buttons = new ArrayList<>();
            loadButtons();
            buttonVisibilityChanged();
        }
    }

    /**
     * Initializes the modules, this is done after all modules has been added to the cart but before most of the initializing code
     */
    public void preInit()
    {
    }

    /**
     * Get the cart this module is a part of
     *
     * @return The cart this module was created at
     */
    public EntityMinecartModular getCart()
    {
        return cart;
    }

    /**
     * If this module is part of a placeholder cart, a placeholder cart is a client side only cart used in the cart assembler.
     *
     * @return If this module is a placeholder module
     */
    public boolean isPlaceholder()
    {
        return getCart().isPlaceholder;
    }

    /**
     * If isPlaceholder returns true you can get the object controlling the simulation of the client only cart.
     *
     * @return The Simulation Info object used to simulate the cart
     */
    protected SimulationInfo getSimInfo()
    {
        return getCart().placeholderAsssembler.getSimulationInfo();
    }

    /**
     * Sets the modular id of this module, this is basically the id of the {@link ModuleData} used to create this module.
     *
     * @param val The module id
     */
    public void setModuleId(final byte val)
    {
        moduleId = val;
    }

    /**
     * Returns which modular id this module is associated with
     *
     * @return The module id
     */
    public byte getModuleId()
    {
        return moduleId;
    }

    /**
     * Is called when the cart's inventory has been changed
     */
    public void onInventoryChanged()
    {
    }

    /**
     * Used to get where to start draw the interface, this is calculated by the cart.
     *
     * @return The x offset of the interface
     */
    public int getX()
    {
        if (doStealInterface())
        {
            return 0;
        }
        return offSetX;
    }

    /**
     * Used to get where to start draw the interface, this is calculated by the cart.
     *
     * @return The y offset of the interface
     */
    public int getY()
    {
        if (doStealInterface())
        {
            return 0;
        }
        return offSetY;
    }

    /**
     * Used to set where the interface of this module starts, this is set by the cart
     *
     * @param val The x offset to use
     */
    public void setX(final int val)
    {
        offSetX = val;
    }

    /**
     * Used to set where the interface of this module starts, this is set by the cart
     *
     * @param val The y offset to use
     */
    public void setY(final int val)
    {
        offSetY = val;
    }

    /**
     * Returns the amount of stacks that this module can store. This will use hasSlots, getInventoryWidth and
     * getInventoryHeight to calculate the size, this can however be overridden for more advanced usages.
     *
     * @return The size of the inventory of this module
     */
    public int getInventorySize()
    {
        if (!hasSlots())
        {
            return 0;
        }
        return getInventoryWidth() * getInventoryHeight();
    }

    /**
     * Returns the size this module wants to allocate in the interface. One shouldn't draw anything outside this area.
     *
     * @return The width of the module's interface
     */
    public int guiWidth()
    {
        return 15 + getInventoryWidth() * 18;
    }

    /**
     * Returns the size this module wants to allocate in the interface. One shouldn't draw anything outside this area.
     *
     * @return The height of the module's interface
     */
    public int guiHeight()
    {
        return 27 + getInventoryHeight() * 18;
    }

    /**
     * The width of slots in the basic slot allocation. Used by the default getInventorySize to make standard
     * slot allocation easier
     *
     * @return The number of slots next to each other
     */
    protected int getInventoryWidth()
    {
        return 3;
    }

    /**
     * The height of slots in the basic slot allocation. Used by the default getInventorySize to make standard
     * slot allocation easier
     *
     * @return The number of slots on top of each other
     */
    protected int getInventoryHeight()
    {
        return 1;
    }

    /**
     * Called by the interface when the user has pressed a key on the keyboard
     *
     * @param extraInformation Extra information of special keys
     */
    public void keyPress(final GuiMinecart gui, final int id, final int extraInformation)
    {
    }

    /**
     * Get the list of slots used by this module. These have already been generated by generateSlots
     *
     * @return The ArrayList of SlotBase with the slots
     */
    public ArrayList<SlotBase> getSlots()
    {
        return slotList;
    }

    /**
     * Generates the slots used for this module, this is used both for the Container and the Interface. For most modules
     * just leave this and use getSlot instead (as well as setting getInventoryWidth and getInventoryHeight)
     *
     * @param slotCount The number of slots that has already been added to the cart. This is for generating the corred slot id
     * @return The number of slots that the cart have added after this module has generated its slots.
     */
    public int generateSlots(int slotCount)
    {
        slotGlobalStart = slotCount;
        slotList = new ArrayList<>();
        for (int j = 0; j < getInventoryHeight(); ++j)
        {
            for (int i = 0; i < getInventoryWidth(); ++i)
            {
                slotList.add(getSlot(slotCount++, i, j));
            }
        }
        return slotCount;
    }

    /**
     * Returns a new slot with the given id, x and y coordinate. This is used to generate the slots easier. Just override this
     * function and return a new slots depending on where it's located. Shouldn't be used if you're overriding generateSlots
     *
     * @param slotId The id of the slot to be created
     * @param x      The x value of the slot, this is not the interface coordinate but just which column it's in.
     * @param y      The y value of the slot, this is not hte interface coordinate but just which row it's in.
     * @return The created SlotBase
     */
    protected SlotBase getSlot(final int slotId, final int x, final int y)
    {
        return null;
    }

    /**
     * Whether this module has slots or not. By default a module is thought to have slots if it has an interface. This is
     * however overridden if it's not the case.
     *
     * @return If it should use slots or not
     */
    public boolean hasSlots()
    {
        return hasGui();
    }

    /**
     * Called every time the cart is being updated.
     */
    public void update()
    {
    }

    /**
     * Returns if this module has enough fuel to keep the cart going one tick more. This should however be moved to engineModuleBase
     *
     * @param consumption The amount of fuel units the cart wants to consume
     * @return If it has fuel or not
     */
    public boolean hasFuel(final int consumption)
    {
        return false;
    }

    /**
     * The maximum speed this module allows the cart to move in. The maximum speed of the cart will therefore be set to the lowest
     * value all of it's modules allow.
     *
     * @return The maximum speed of the cart
     */
    public float getMaxSpeed()
    {
        return 1.1f;
    }

    /**
     * Returns the Y value this cart should try to be on. By returning -1 this module won't care about where the cart should be.
     * If no modules do care about this the cart will just continue where it already is.
     *
     * @return The Y value
     */
    public int getYTarget()
    {
        return -1;
    }

    /**
     * Called when the cart travels over a rail. Used to allow modules to react to specific rails.
     *
     * @param pos Blockpos in the world
     */
    public void moveMinecartOnRail(BlockPos pos)
    {
    }

    /**
     * Used to get the ItemStack in a specific slot of this module
     *
     * @param slot The slot id, this is the local id for this module.
     * @return The ItemStack in the slot, could of course be null
     */
    @Nonnull
    public ItemStack getStack(final int slot)
    {
        return cargo.get(slot);
    }

    /**
     * Used to set the ItemStack in specific slot of this module.
     *
     * @param slot The slot id, this is the local id for this module.
     * @param item The ItemStack to be set.
     */
    public void setStack(final int slot, @Nonnull ItemStack item)
    {
        cargo.set(slot, item);
    }

    /**
     * Used to try to merge/add the ItemStack in specific slots of this module.
     *
     * @param slotStart The slot start id, this is the local id for this module.
     * @param slotEnd   The slot end id, this is the local id for this module.
     * @param item      The ItemStack to be set.
     */
    public void addStack(final int slotStart, final int slotEnd, @Nonnull ItemStack item)
    {
        getCart().addItemToChest(item, slotGlobalStart + slotStart, slotGlobalStart + slotEnd);
    }

    /**
     * Used to try to merge/add the ItemStack in a specific slot of this module.
     *
     * @param slot The slot id, this is the local id for this module.
     * @param item The ItemStack to be set.
     */
    public void addStack(final int slot, @Nonnull ItemStack item)
    {
        addStack(slot, slot, item);
    }

    /**
     * Used to prevent the cart to drop things when it breaks. If any module returns false the cart won't drop anything.
     *
     * @return If this module allows the cart to drop on death
     */
    public boolean dropOnDeath()
    {
        return true;
    }

    /**
     * Called when the cart breaks
     */
    public void onDeath()
    {
    }

    /**
     * Whether the cart should allocate room for this interface. By default this also allocates slots, see hasSlots
     *
     * @return If the module is using an interface
     */
    public boolean hasGui()
    {
        return false;
    }

    /**
     * If the module should draw any foreground, it is done here.
     *
     * @param gui The GUI that will draw the interface
     */
    @OnlyIn(Dist.CLIENT)
    public void drawForeground(PoseStack matrixStack, final GuiMinecart gui)
    {
    }

    /**
     * Draws a one lined string in the center of the given rectangle. It will handle scrolling as well as module offset.
     *
     * @param gui  The gui to draw it on.
     * @param str  The string to be drawn.
     * @param rect The rectangle
     * @param c    The color to be used
     */
    @OnlyIn(Dist.CLIENT)
    public void drawString(PoseStack matrixStack, final GuiMinecart gui, final String str, final int[] rect, final int c)
    {
        if (rect.length < 4)
        {
            return;
        }
        drawString(matrixStack, gui, str, rect[0] + (rect[2] - Minecraft.getInstance().font.width(str)) / 2, rect[1] + (rect[3] - Minecraft.getInstance().font.lineHeight + 3) / 2, c);
    }

    /**
     * Draws a string at the given location. It will handle scrolling as well as module offset.
     *
     * @param gui The gui to draw it on.
     * @param str The string to be draw
     * @param x   The local x coordinate
     * @param y   The local y coordinate
     * @param c   The color to be used
     */
    @OnlyIn(Dist.CLIENT)
    public void drawString(PoseStack matrixStack, final GuiMinecart gui, final String str, final int x, final int y, final int c)
    {
        drawString(matrixStack, gui, str, gui.getGuiLeft() + x, gui.getGuiTop() + y, -1, false, c);
    }

    @OnlyIn(Dist.CLIENT)
    public void drawString(PoseStack matrixStack, GuiMinecart gui, final String str, final int x, final int y, final int w, final boolean center, final int c)
    {
        final int j = gui.getGuiLeft();
        final int k = gui.getGuiTop();
        final int[] rect = {x, y, w, 8};
        if (!doStealInterface())
        {
            handleScroll(rect);
        }
        if (rect[3] == 8)
        {
            if (center)
            {
                Minecraft.getInstance().font.draw(matrixStack, str, rect[0] + (rect[2] - Minecraft.getInstance().font.width(str)) / 2 + getX(), rect[1] + getY(), c);
            }
            else
            {
                Minecraft.getInstance().font.draw(matrixStack, str, rect[0] + getX(), rect[1] + getY(), c);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void drawStringWithShadow(PoseStack matrixStack, final GuiMinecart gui, final String str, final int x, final int y, final int c)
    {
        final int j = gui.getGuiLeft();
        final int k = gui.getGuiTop();
        final int[] rect = {x, y, 0, 8};
        if (!doStealInterface())
        {
            handleScroll(rect);
        }
        if (rect[3] == 8)
        {
            Minecraft.getInstance().font.drawShadow(matrixStack, str, rect[0] + getX(), rect[1] + getY(), c);
        }
    }

    /**
     * Draws a multiline string at the given location. It will handle scrolling as well as module offset.
     *
     * @param gui The gui to draw it on
     * @param str The string to be drawn
     * @param x   The local x coordinate
     * @param y   The local y coordinate
     * @param w   The maximum width of the text area
     * @param c   The color to be used
     */
    @OnlyIn(Dist.CLIENT)
    public void drawSplitString(final GuiMinecart gui, final String str, final int x, final int y, final int w, final int c)
    {
        drawSplitString(gui, str, x, y, w, false, c);
    }

    @OnlyIn(Dist.CLIENT)
    public void drawSplitString(final GuiMinecart gui, final String str, final int x, final int y, final int w, final boolean center, final int c)
    {
        //		final List newlines = gui.getFontRenderer().listFormattedStringToWidth(str, w);
        //		for (int i = 0; i < newlines.size(); ++i) {
        //			final String line = newlines.get(i).toString();
        //			drawString(gui, line, x, y + i * 8, w, center, c);
        //		}
    }

    @OnlyIn(Dist.CLIENT)
    public void drawItemInInterface(final GuiMinecart gui, @Nonnull ItemStack item, final int x, final int y)
    {
        final int[] rect = {x, y, 16, 16};
        handleScroll(rect);
        if (rect[3] == 16)
        {
            final ItemRenderer renderitem = Minecraft.getInstance().getItemRenderer();
            renderitem.renderGuiItem(item, gui.getGuiLeft() + rect[0] + getX(), gui.getGuiTop() + rect[1] + getY());
        }
    }

    /**
     * Draw an image in the given interface, using the current texture and using the given dimensions.
     *
     * @param gui     The gui to draw it on
     * @param targetX The local x coordinate to draw it on
     * @param targetY The local y coordinate to draw it on
     * @param srcX    The x coordinate in the source file
     * @param srcY    The y coordinate in the source file
     * @param sizeX   The width of the image
     * @param sizeY   The height of the image
     */
    @OnlyIn(Dist.CLIENT)
    public void drawImage(PoseStack matrixStack, final GuiMinecart gui, final int targetX, final int targetY, final int srcX, final int srcY, final int sizeX, final int sizeY)
    {
        drawImage(matrixStack, gui, targetX, targetY, srcX, srcY, sizeX, sizeY, GuiMinecart.RENDER_ROTATION.NORMAL);
    }

    /**
     * Draw an image in the given interface, using the current texture and using the given dimensions.
     *
     * @param gui      The gui to draw it on
     * @param targetX  The local x coordinate to draw it on
     * @param targetY  The local y coordinate to draw it on
     * @param srcX     The x coordinate in the source file
     * @param srcY     The y coordinate in the source file
     * @param sizeX    The width of the image
     * @param sizeY    The height of the image
     * @param rotation The rotation this will be drawn with
     */
    @OnlyIn(Dist.CLIENT)
    public void drawImage(PoseStack matrixStack, final GuiMinecart gui, final int targetX, final int targetY, final int srcX, final int srcY, final int sizeX, final int sizeY, final GuiMinecart.RENDER_ROTATION rotation)
    {
        drawImage(matrixStack, gui, new int[]{targetX, targetY, sizeX, sizeY}, srcX, srcY, rotation);
    }


    /**
     * Draw an image in the given interface, using the current texture and using the given dimentiosn.
     *
     * @param gui  The gui to draw it on
     * @param rect The rectangle indicating where to draw it {targetX, targetY, sizeX, sizeY}
     * @param srcX The x coordinate in the source file
     * @param srcY They y coordinate in the source file
     */
    @OnlyIn(Dist.CLIENT)
    public void drawImage(PoseStack matrixStack, final GuiMinecart gui, final int[] rect, final int srcX, final int srcY)
    {
        drawImage(matrixStack, gui, rect, srcX, srcY, GuiMinecart.RENDER_ROTATION.NORMAL);
    }

    /**
     * Draw an image in the given interface, using the current texture and using the given dimentiosn.
     *
     * @param gui      The gui to draw it on
     * @param rect     The rectangle indicating where to draw it {targetX, targetY, sizeX, sizeY}
     * @param srcX     The x coordinate in the source file
     * @param srcY     They y coordinate in the source file
     * @param rotation The rotation this will be drawn with
     */
    @OnlyIn(Dist.CLIENT)
    public void drawImage(PoseStack matrixStack, final GuiMinecart gui, int[] rect, final int srcX, int srcY, final GuiMinecart.RENDER_ROTATION rotation)
    {
        if (rect.length < 4)
        {
            return;
        }
        rect = cloneRect(rect);
        if (!doStealInterface())
        {
            srcY -= handleScroll(rect);
        }
        if (rect[3] > 0)
        {
            gui.drawTexturedModalRect(matrixStack, gui.getGuiLeft() + rect[0] + getX(), gui.getGuiTop() + rect[1] + getY(), srcX, srcY, rect[2], rect[3], rotation);
        }
    }

    /**
     * Draw an icon in the given interface, using the current texture and using the given dimensions.
     *
     * @param gui     The gui to draw it on
     * @param icon    The Icon to draw
     * @param targetX The local x coordinate to draw it on
     * @param targetY The local y coordinate to draw it on
     * @param srcX    The x coordinate in the source file
     * @param srcY    The y coordinate in the source file
     * @param sizeX   The width of the image
     * @param sizeY   The height of the image
     */
    @OnlyIn(Dist.CLIENT)
    public void drawImage(final GuiMinecart gui, final TextureAtlasSprite icon, final int targetX, final int targetY, final int srcX, final int srcY, final int sizeX, final int sizeY)
    {
        this.drawImage(gui, icon, new int[]{targetX, targetY, sizeX, sizeY}, srcX, srcY);
    }

    /**
     * Draw an image in the given interface, using the current texture and using the given dimentiosn.
     *
     * @param gui  The gui to draw it on
     * @param rect The rectangle indicating where to draw it {targetX, targetY, sizeX, sizeY}
     * @param srcX The x coordinate in the source file
     * @param srcY They y coordinate in the source file
     */
    @OnlyIn(Dist.CLIENT)
    public void drawImage(final GuiMinecart gui, final TextureAtlasSprite icon, int[] rect, final int srcX, int srcY)
    {
        if (rect.length < 4)
        {
            return;
        }
        rect = this.cloneRect(rect);
        if (!this.doStealInterface())
        {
            srcY -= this.handleScroll(rect);
        }
        if (rect[3] > 0)
        {
            //TODO
            //			gui.blit(icon, gui.getGuiLeft() + rect[0] + this.getX(), gui.getGuiTop() + rect[1] + this.getY(), rect[2] / 16.0f, rect[3] / 16.0f, srcX / 16.0f, srcY / 16.0f);
        }
    }

    /**
     * Scrolls a given rectangle accordingly to the scrollbar in the interface
     *
     * @param rect The rectangle to scroll {targetX, targetY, sizeX, sizeY}
     * @return The start offset caused by the scroll, i.e. if the middle part of the rectangle is the topmost visible part. Used to
     * change the srcY when drawing images for instance, see drawImage.
     */
    public int handleScroll(final int[] rect)
    {
        rect[1] -= getCart().getRealScrollY();
        int y = rect[1] + getY();
        if (y < 4)
        {
            int dif = y - 4;
            rect[3] += dif;
            y = 4;
            rect[1] = y - getY();
            return dif;
        }
        if (y + rect[3] > EntityMinecartModular.MODULAR_SPACE_HEIGHT)
        {
            rect[3] = Math.max(0, EntityMinecartModular.MODULAR_SPACE_HEIGHT - y);
            return 0;
        }
        return 0;
    }

    /**
     * Clones a rectangle
     *
     * @param rect The rectangle to be clones {targetX, targetY, sizeX, sizeY}
     * @return The cloned rectangle {targetX, targetY, sizeX, sizeY}
     */
    protected int[] cloneRect(final int[] rect)
    {
        return new int[]{rect[0], rect[1], rect[2], rect[3]};
    }

    /**
     * Whether the module is using client/server buttons. Currently not used
     *
     * @return whether buttons are used or not
     */
    public boolean useButtons()
    {
        return false;
    }

    /**
     * Called when a client/server button changes visibility state
     */
    public final void buttonVisibilityChanged()
    {
        buttons.sort(ButtonComparator.INSTANCE);
        ButtonBase.LOCATION lastLoc = null;
        int id = 0;
        for (final ButtonBase button : buttons)
        {
            if (button.isVisible())
            {
                if (lastLoc != null && button.getLocation() != lastLoc)
                {
                    id = 0;
                }
                lastLoc = button.getLocation();
                button.setCurrentID(id);
                ++id;
            }
        }
    }

    /**
     * Allows the module to override the direction the cart is going. This mechanic is not finished and hence won't work perfectly.
     *
     * @param pos The blockpos in the world
     * @return The direction to go, default means that the module won't change it
     */
    public RAILDIRECTION getSpecialRailDirection(BlockPos pos)
    {
        return RAILDIRECTION.DEFAULT;
    }

    /**
     * Initializing any server/client buttons
     */
    protected void loadButtons()
    {
    }

    /**
     * Adds a new server/client button to the module
     *
     * @param button The button to be added
     */
    public final void addButton(final ButtonBase button)
    {
        button.setIdInModule(moduleButtonId++);
        buttons.add(button);
    }

    /**
     * Generates an NBT name from the base name and the number of the module. This is to prevent conflicts between
     * two modules requesting the same value.
     *
     * @param name The base name of the NBT name
     * @param id   The number of the module
     * @return The string to be used as an NBT name
     */
    public String generateNBTName(final String name, final int id)
    {
        return "module" + id + name;
    }

    /**
     * Handles the writing of the NBT data when the world is being saved
     *
     * @param tagCompound The tag compound to write the data to
     * @param id          The number of this module
     */
    public final void writeToNBT(final CompoundTag tagCompound, final int id)
    {
        if (getInventorySize() > 0)
        {
            final ListTag items = new ListTag();
            for (int i = 0; i < getInventorySize(); ++i)
            {
                if (!getStack(i).isEmpty())
                {
                    final CompoundTag item = new CompoundTag();
                    item.putByte("Slot", (byte) i);
                    getStack(i).save(item);
                    items.add(item);
                }
            }
            tagCompound.put(generateNBTName("Items", id), items);
        }
        Save(tagCompound, id);
    }

    /**
     * Allows a module to save specific data when world is saved
     *
     * @param tagCompound The NBT tag compound to write to
     * @param id          The number of the module
     */
    protected void Save(final CompoundTag tagCompound, final int id)
    {
    }

    /**
     * Handles the reading of the NBT data when the world is being loaded
     *
     * @param tagCompound The tag compound to read the data from
     * @param id          The number of this module
     */
    public final void readFromNBT(final CompoundTag tagCompound, final int id)
    {
        if (getInventorySize() > 0)
        {
            final ListTag items = tagCompound.getList(generateNBTName("Items", id), NBTHelper.COMPOUND.getId());
            for (int i = 0; i < items.size(); ++i)
            {
                final CompoundTag item = items.getCompound(i);
                final int slot = item.getByte("Slot") & 0xFF;
                if (slot >= 0 && slot < getInventorySize())
                {
                    setStack(slot, ItemStack.of(item));
                }
            }
        }
        Load(tagCompound, id);
    }

    /**
     * Allows a module to load specific data when world is loaded
     *
     * @param tagCompound The NBT tag compound to read from
     * @param id          The number of the module
     */
    protected void Load(final CompoundTag tagCompound, final int id)
    {
    }

    /**
     * Draw the text of server/client buttons
     *
     * @param gui The gui to draw on
     */
    @OnlyIn(Dist.CLIENT)
    public final void drawButtonText(PoseStack matrixStack, GuiMinecart gui)
    {
        for (final ButtonBase button : buttons)
        {
            button.drawButtonText(matrixStack, gui, this);
        }
    }

    /**
     * Draw the graphics of server/client buttons
     *
     * @param gui The gui to draw on
     * @param x   The x coordinate of the mouse
     * @param y   The y coordinate of the mouse
     */
    @OnlyIn(Dist.CLIENT)
    public final void drawButtons(PoseStack matrixStack, final GuiMinecart gui, final int x, final int y)
    {
        for (final ButtonBase button : buttons)
        {
            button.drawButton(matrixStack, gui, this, x, y);
        }
    }

    /**
     * Draw the mouse overlay of server/client buttons
     *
     * @param gui The gui to draw on
     * @param x   The x coordinate of the mouse
     * @param y   The y coordinate of the mouse
     */
    @OnlyIn(Dist.CLIENT)
    public final void drawButtonOverlays(PoseStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
        for (final ButtonBase button : buttons)
        {
            if (button.isVisible())
            {
                drawStringOnMouseOver(matrixStack, gui, button.toString(), x, y, button.getBounds());
            }
        }
    }

    /**
     * Handles mouse click of server/client buttons
     *
     * @param gui         The gui to draw on
     * @param x           The x coordinate of the mouse
     * @param y           The y coordinate of the mouse
     * @param mousebutton The button which was pressed
     */
    @OnlyIn(Dist.CLIENT)
    public final void mouseClickedButton(final GuiMinecart gui, final int x, final int y, final int mousebutton)
    {
        for (final ButtonBase button : buttons)
        {
            if (inRect(x, y, button.getBounds()))
            {
                button.computeOnClick(gui, mousebutton);
            }
        }
    }

    /**
     * Sends a packet to the server that a server/client button has been pressed
     *
     * @param button    The button that was pressed
     * @param clickinfo The information about the click, which mouse button was clicked, if the shift key was down, etc.
     */
    public void sendButtonPacket(final ButtonBase button, final byte clickinfo)
    {
        final byte id = (byte) button.getIdInModule();
        sendPacket(totalNumberOfPackets() - 1, new byte[]{id, clickinfo});
    }

    /**
     * Used to draw background for a module
     *
     * @param gui The gui to draw on
     * @param x   The x coordinate of the mouse
     * @param y   The y coordinate of the mouse
     */
    @OnlyIn(Dist.CLIENT)
    public void drawBackground(PoseStack matrixStack, final GuiMinecart gui, final int x, final int y)
    {
    }

    @OnlyIn(Dist.CLIENT)
    public void drawBackgroundItems(final GuiMinecart gui, final int x, final int y)
    {
    }

    /**
     * Used to handle mouse clicks on the module's interface
     *
     * @param gui    The gui that was clicked
     * @param x      The x coordinate of the mouse
     * @param y      The y coordinate of the mouse
     * @param button The button that was pressed on the mouse
     */
    @OnlyIn(Dist.CLIENT)
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
    }

    /**
     * Used to handle mouse movement and move releases in the module's interface
     *
     * @param gui    The gui that is being used
     * @param x      The x coordinate of the mouse
     * @param y      The y coordinate of the mouse
     * @param button The button that was released, or -1 if the cursor is just being moved
     */
    @OnlyIn(Dist.CLIENT)
    public void mouseMovedOrUp(final GuiMinecart gui, final int x, final int y, final int button)
    {
    }

    /**
     * Used to draw mouse over text for a module
     *
     * @param gui The gui to draw on
     * @param x   The x coordinate of the mouse
     * @param y   The y coordiante of the mouse
     */
    public void drawMouseOver(PoseStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
    }

    /**
     * Detects if the given mouse coordinates are within the given rectangle
     *
     * @param x     The mouse x coordinate
     * @param y     The mouse y coordinate
     * @param x1    The x coordinate of the rectangle
     * @param y1    The y coordinate of the rectangle
     * @param sizeX The width of the rectangle
     * @param sizeY The height of the rectangle
     * @return If the mouse was inside the rectangle
     */
    protected boolean inRect(final int x, final int y, final int x1, final int y1, final int sizeX, final int sizeY)
    {
        return inRect(x, y, new int[]{x1, y1, sizeX, sizeY});
    }

    /**
     * Detects if the given mouse coordinates are within the given rectangle
     *
     * @param x    The mouse x coordinate
     * @param y    The mouse y coordinate
     * @param rect The rectangle to check for {x,y,width, height}
     * @return If the mouse was inside the rectangle
     */
    public boolean inRect(final int x, final int y, int[] rect)
    {
        if (rect.length < 4)
        {
            return false;
        }
        rect = cloneRect(rect);
        if (!doStealInterface())
        {
            handleScroll(rect);
        }
        return x >= rect[0] && x <= rect[0] + rect[2] && y >= rect[1] && y <= rect[1] + rect[3];
    }

    /**
     * Let's the module handle when damage is caused to the cart
     *
     * @param source The source of the damage
     * @param val    The damage
     * @return True if the cart should take the damage, False to prevent the damage
     */
    public boolean receiveDamage(DamageSource source, float val)
    {
        return true;
    }

    /**
     * Tells the cart to turn around, if this module is allowed to tell the cart to do so.
     */
    protected void turnback()
    {
        for (final ModuleBase module : getCart().getModules())
        {
            if (module != this && module.preventTurnback())
            {
                return;
            }
        }
        getCart().turnback();
    }

    /**
     * Allows a module to take all control of a cart's turn back condition
     *
     * @return True to prevent other modules from turning the cart around
     */
    protected boolean preventTurnback()
    {
        return false;
    }

    /**
     * The number of packets this module will use, this includes the packet used for system/client buttons
     *
     * @return The packet count
     */
    public final int totalNumberOfPackets()
    {
        return numberOfPackets() + (useButtons() ? 1 : 0);
    }

    /**
     * The number of normal packets this module will allocate
     *
     * @return The packet count
     */
    protected int numberOfPackets()
    {
        return 0;
    }

    /**
     * Gets the packet offset used as a header to determine which module owns a packet. This is done by the cart.
     *
     * @return The packet offset
     */
    public int getPacketStart()
    {
        return packetOffset;
    }

    /**
     * Sets the packet offset used as a header to determine which module own a packet. This is done by the cart.
     *
     * @param val The packet offset
     */
    public void setPacketStart(int val)
    {
        packetOffset = val;
    }

    /**
     * Sends a packet from the client to the server
     *
     * @param id The local id of the packet
     */
    protected void sendPacket(int id)
    {
        sendPacket(id, new byte[0]);
    }

    /**
     * Sends a packet from the client to the server
     *
     * @param id   The local id of the packet
     * @param data An extra byte sent along
     */
    public void sendPacket(int id, byte data)
    {
        sendPacket(id, new byte[]{data});
    }

    /**
     * Sends a packet from the client to the server
     *
     * @param id   The local id of the packet
     * @param data A byte array of data sent along
     */
    public void sendPacket(int id, byte[] data)
    {
        PacketHandler.sendToServer(new PacketMinecartButton(cart.getId(), getPacketStart() + id, data));
    }

    /**
     * Sends a packet from the server to a player's client
     *
     * @param id     The local id of the packet
     * @param player The player to send it to
     */
    protected void sendPacket(int id, Player player)
    {
        sendPacket(id, new byte[0], player);
    }

    /**
     * Sends a packet from the server to a player's client
     *
     * @param id     The local id of the packet
     * @param data   An extra byte sent along
     * @param player The player to send it to
     */
    protected void sendPacket(int id, byte data, Player player)
    {
        sendPacket(id, new byte[]{data}, player);
    }

    /**
     * Sends a packet from the server to a player's client
     *
     * @param id     The local id of the packet
     * @param data   A byte array of data sent along
     * @param player The player to send it to
     */
    protected void sendPacket(int id, byte[] data, Player player)
    {
        //TODO
        //		PacketStevesCarts.sendPacketToPlayer(getPacketStart() + id, data, player, getCart());
    }

    /**
     * Sends a packet from the server to all players around the cart
     *
     * @param id The local id of the packet
     */
    protected void sendPacketAround(int id)
    {
        sendPacketAround(id, new byte[0]);
    }

    /**
     * Sends a packet from the server to all players around the cart
     *
     * @param id   The local id of the packet
     * @param data An extra byte sent along
     */
    protected void sendPacketAround(int id, byte data)
    {
        sendPacketAround(id, new byte[]{data});
    }

    /**
     * Sends a packet from the server to all players around the cart
     *
     * @param id   The local id of the packet
     * @param data A byte array of data sent along
     */
    protected void sendPacketAround(int id, byte[] data)
    {
        //TODO
        //		PacketStevesCarts.sendPacketToAllAround(getPacketStart() + id, data, getCart());
    }

    /**
     * Receive a normal packet on the server or the client
     *
     * @param id     The local id of the packet
     * @param data   The byte array of extra data, could be empty
     * @param player The player who sent or received the packet
     */
    protected void receivePacket(int id, byte[] data, Player player)
    {
    }

    /**
     * Handles a packet received on the server or the client and sends it where it should be handled
     *
     * @param id     The local id of the packet
     * @param data   The byte array of extra data, could be empty
     * @param player The player who sent or received the packet
     */
    public final void delegateReceivedPacket(int id, byte[] data, Player player)
    {
        if (id < 0)
        {// || id >= totalNumberOfPackets()) {
            return;
        }
        if (id == totalNumberOfPackets() - 1 && useButtons())
        {
            int buttonId = data[0];
            if (buttonId < 0)
            {
                buttonId += 256;
            }
            for (final ButtonBase button : buttons)
            {
                if (button.getIdInModule() == buttonId)
                {
                    final byte buttoninformation = data[1];
                    final boolean isCtrlDown = (buttoninformation & 0x40) != 0x0;
                    final boolean isShiftDown = (buttoninformation & 0x80) != 0x0;
                    final int mousebutton = buttoninformation & 0x3F;
                    if (button.isVisible() && button.isEnabled())
                    {
                        button.onServerClick(player, mousebutton, isCtrlDown, isShiftDown);
                        break;
                    }
                    break;
                }
            }
        }
        else
        {
            receivePacket(id, data, player);
        }
    }

    /**
     * The number of datamangers this module wants to use
     *
     * @return The amount of datamangers
     */
    public int numberOfDataWatchers()
    {
        return 0;
    }

    /**
     * Used to initiate the datamangers
     */
    public void initDw()
    {
    }

    /**
     * Register a dataparameter to the datamanger
     *
     * @param key   The local datamanger key
     * @param value The value to add
     */
    protected final <T> void registerDw(EntityDataAccessor<T> key, T value)
    {
        for (SynchedEntityData.DataItem<?> entry : getCart().getDataManager().getAll())
        {
            if (entry.getAccessor() == key)
            {
                return;
            }
        }
        getCart().getDataManager().define(key, value);
    }

    /**
     * Updates a datamanger
     *
     * @param key   The local datamanger key
     * @param value The value to update it to
     */
    protected final <T> void updateDw(EntityDataAccessor<T> key, T value)
    {
        getCart().getDataManager().set(key, value);
    }

    /**
     * Get a datamanger
     *
     * @param key The local datamanger key
     * @return The value of the datamanger
     */
    protected <T> T getDw(EntityDataAccessor<T> key)
    {
        return getCart().getDataManager().get(key);
    }

    protected <T> EntityDataAccessor<T> createDw(EntityDataSerializer<T> serializer)
    {
        return serializer.createAccessor(cart.getNextDataWatcher());
    }

    /**
     * The amount of Gui data this module want to use. Gui data is used for sending information from the server to the client
     * when the specific client has the the interface open
     *
     * @return The number of Gui data
     */
    public int numberOfGuiData()
    {
        return 0;
    }

    /**
     * Get the gui data offset. This is used as a header to know which module owns a specific gui data.
     *
     * @return
     */
    public int getGuiDataStart()
    {
        return guiDataOffset;
    }

    /**
     * Set the gui data offset. This is used as a header to know which module owns a specific gui data. This is set by the cart.
     *
     * @param val
     */
    public void setGuiDataStart(final int val)
    {
        guiDataOffset = val;
    }

    /**
     * Updates the gui data for a bunch of players. This is the part that actually updates the values. It's however the other
     * updateGuiData which handles most parts
     *
     * @param con     The containers that i used
     * @param players The players to update
     * @param id      The global gui data id
     * @param data    The data to update to
     */
    private final void updateGuiData(Container con, List<ContainerListener> players, final int id, final short data)
    {
        for (final ContainerListener player : players)
        {
            //TODO
            //			player.sendWindowProperty(con, id, data);
        }
    }

    /**
     * Updates the gui data sing the supplied info, this is what is being called from a module and it's also the function that
     * actually handles the update.
     *
     * @param info The information about the update, should be formatted as follows: {Container, Players, isNew}
     * @param id   The local gui data id
     * @param data The data to update to
     */
    public final void updateGuiData(final Object[] info, final int id, final short data)
    {
        //TODO ContainerMinecart
        //		final ContainerMinecart con = (ContainerMinecart) info[0];
        //		if (con == null) {
        //			return;
        //		}
        //		final int globalId = id + getGuiDataStart();
        //		final List players = (List) info[1];
        //		boolean flag;
        //		boolean isNew = (boolean) info[2];
        //		if (!isNew) {
        //			if (con.cache != null) {
        //				final Short val = con.cache.get((short) globalId);
        //				isNew = (val == null || val != data);
        //			} else {
        //				isNew = true;
        //			}
        //		}
        //		if (isNew) {
        //			if (con.cache == null) {
        //				con.cache = new HashMap<>();
        //			}
        //			updateGuiData(con, players, globalId, data);
        //			con.cache.put((short) globalId, data);
        //		}
    }

    /**
     * Initializes everything when a player has opened the interface
     *
     * @param con    The container used by the interface
     * @param player The player that opened it
     */
    public final void initGuiData(final Container con, final ContainerListener player)
    {
        final ArrayList players = new ArrayList();
        players.add(player);
        checkGuiData(con, players, true);
    }

    /**
     * Used to send gui data information
     *
     * @param info The information that should be sent as the first parameter to updateGuiData
     */
    protected void checkGuiData(final Object[] info)
    {
    }

    /**
     * Prepares the gui data check
     *
     * @param con     The container to be used
     * @param players The players that should be receive the information
     * @param isNew   If this data is new or not
     */
    public final void checkGuiData(final Container con, final List players, final boolean isNew)
    {
        if (con == null)
        {
            return;
        }
        checkGuiData(new Object[]{con, players, isNew});
    }

    /**
     * Receive gui data on the client side
     *
     * @param id   The local gui data id
     * @param data The value of the gui data
     */
    public void receiveGuiData(final int id, final short data)
    {
    }

    /**
     * Get the consumption for this module
     *
     * @param isMoving A flag telling you if the cart is moving or not
     * @return The consumption
     */
    public int getConsumption(final boolean isMoving)
    {
        return 0;
    }

    public void setModels(final ArrayList<ModelCartbase> models)
    {
        this.models = models;
    }

    public ArrayList<ModelCartbase> getModels()
    {
        return models;
    }

    public boolean haveModels()
    {
        return models != null;
    }

    /**
     * Draw a specific mouse over string if the mouse is in a specific rectangle
     *
     * @param gui The gui to draw on
     * @param str The string to be drawn
     * @param x   The x coordinate of the mouse
     * @param y   the y coordinate of the mouse
     * @param x1  The x coordinate of the rectangle
     * @param y1  The y coordinate of the rectangle
     * @param w   The width of the rectangle
     * @param h   The height of the rectangle
     */
    @OnlyIn(Dist.CLIENT)
    public final void drawStringOnMouseOver(PoseStack matrixStack, GuiMinecart gui, final String str, final int x, final int y, final int x1, final int y1, final int w, final int h)
    {
        drawStringOnMouseOver(matrixStack, gui, str, x, y, new int[]{x1, y1, w, h});
    }

    /**
     * Draw a specific mouse over string if the mouse is in a specific rectangle
     *
     * @param gui  The gui to draw on
     * @param str  The string to be drawn
     * @param x    The x coordinate of the mouse
     * @param y    The y coordinate of the mouse
     * @param rect The rectangle that the mouse has to be in, defin as {x,y,width,height}
     */
    @OnlyIn(Dist.CLIENT)
    public final void drawStringOnMouseOver(PoseStack matrixStack, final GuiMinecart gui, final String str, int x, int y, final int[] rect)
    {
        if (!inRect(x, y, rect))
        {
            return;
        }
        x += getX();
        y += getY();
        gui.drawMouseOver(matrixStack, str, x, y);
    }

    /**
     * Draws an image overlay on the screen. Observe that this is not when a special interface is open.
     *
     * @param rect    The rectangle for the image's dimensions {targetX, targetY, width, height}
     * @param sourceX The x coordinate in the source file
     * @param sourceY The y coordinate in the source file
     */
    protected void drawImage(final int[] rect, final int sourceX, final int sourceY)
    {
        drawImage(rect[0], rect[1], sourceX, sourceY, rect[2], rect[3]);
    }

    /**
     * Draws an image overlay on the screen. Observe that this is not when a special interface is open.
     *
     * @param targetX The x coordinate of the image
     * @param targetY The y coordinate of the image
     * @param sourceX The x coordinate in the source file
     * @param sourceY The y coordinate in the source file
     * @param width   The width of the image
     * @param height  The height of the image
     */
    protected void drawImage(int targetX, int targetY, int sourceX, int sourceY, int width, int height)
    {
        final float var7 = 0.00390625f;
        final float var8 = 0.00390625f;
        final Tesselator tess = Tesselator.getInstance();
        //TODO this is a later problem
        //		BufferBuilder buff = tess.getBuffer();
        //		buff.begin(7, DefaultVertexFormats.POSITION_TEX);
        //		buff.pos((double)(targetX + 0), 	(double)(targetY + height), 	-90D).tex((double)((float)(sourceX + 0) * var7), 		(double)((float)(sourceY + height) * var8)).endVertex();
        //		buff.pos((double)(targetX + width),	(double)(targetY + height), 	-90D).tex((double)((float)(sourceX + width) * var7), 	(double)((float)(sourceY + height) * var8)).endVertex();
        //		buff.pos((double)(targetX + width),	(double)(targetY + 0), 			-90D).tex((double)((float)(sourceX + width) * var7), 	(double)((float)(sourceY + 0) * var8)).endVertex();
        //		buff.pos((double)(targetX + 0), 	(double)(targetY + 0), 			-90D).tex((double)((float)(sourceX + 0) * var7), 		(double)((float)(sourceY + 0) * var8)).endVertex();
        //		tess.draw();
    }

    @OnlyIn(Dist.CLIENT)
    protected Player getClientPlayer()
    {
        if (Minecraft.getInstance() != null)
        {
            return Minecraft.getInstance().player;
        }
        return null;
    }

    /**
     * Used to render graphical overlays on the screen
     *
     * @param minecraft The mincraft instance to use with the rendering
     */
    @OnlyIn(Dist.CLIENT)
    public void renderOverlay(PoseStack matrixStack, Minecraft minecraft)
    {
    }

    /**
     * Allows a module to stop the engines, won't stop modules using the engine though
     *
     * @return True if the module is forcing the engines to stop
     */
    public boolean stopEngines()
    {
        return false;
    }

    /**
     * Allows a module to stop the cart from being rendered
     *
     * @return False if the cart sohuldn't be rendered
     */
    public boolean shouldCartRender()
    {
        return true;
    }

    /**
     * Allows a module to tell the cart to use a specific push factor
     *
     * @return the push factor, or -1 to use the default value
     */
    public double getPushFactor()
    {
        return -1.0;
    }

    /**
     * Allows a module to change the color of the cart
     *
     * @return The color of the cart {Red 0.0F to 1.0F, Green 0.0F to 1.0F, Blue 0.0F to 1.0F}
     */
    public float[] getColor()
    {
        return new float[]{1.0f, 1.0f, 1.0f};
    }

    /**
     * Allows a module to change the y offset the mounted entity should be at
     *
     * @param rider The mounted entity
     * @return The offset, or 0 if this module don't wish to change the offset.
     */
    public float mountedOffset(final Entity rider)
    {
        return 0.0f;
    }

    /**
     * Determines if a block counts as air by the modules, for example a cart will count snow as air, or long grass or the like
     *
     * @param pos The Blockpos of the block
     * @return If this block counts as air by the modules
     */
    protected boolean countsAsAir(BlockPos pos)
    {
        if (getCart().level.getBlockState(pos).isAir())
        {
            return true;
        }
        Block b = getCart().level.getBlockState(pos).getBlock();
        return b instanceof SnowLayerBlock || b instanceof FlowerBlock || b instanceof VineBlock;
    }

    /**
     * Called when the cart is passing a vanilla activator rail
     *
     * @param x      The X coordinate of the rail
     * @param y      The Y coordinate of the rail
     * @param z      The Z coordinate of the rail
     * @param active If the rail is active or not
     */
    public void activatedByRail(final int x, final int y, final int z, final boolean active)
    {
    }


    /**
     * Return the {@link ModuleData} which represents this module
     *
     * @return The datacc
     */
    public ModuleData getData()
    {
        return ModuleData.getList().get(getModuleId());
    }

    /**
     * Allows a module to steal the whole interface, preventing any other module from using the
     * interface. This is not meant to be permanent, use it when a lot of interface is required,
     * then when the user clicks on something to close it then return false again.
     *
     * @return If module steal the interface
     */
    public boolean doStealInterface()
    {
        return false;
    }

    public boolean hasExtraData()
    {
        return false;
    }

    public byte getExtraData()
    {
        return 0;
    }

    public void setExtraData(final byte b)
    {
    }

    protected FakePlayer getFakePlayer()
    {
        return FakePlayerFactory.getMinecraft((ServerLevel) getCart().level);
    }

    public boolean disableStandardKeyFunctionality()
    {
        return false;
    }

    public void addToLabel(final ArrayList<String> label)
    {
    }

    public boolean onInteractFirst(final Player entityplayer)
    {
        return false;
    }

    public void postUpdate()
    {
    }

    public String getModuleName()
    {
        return I18n.get("item.stevescarts." + ModuleData.getList().get(getModuleId()).getRawName());
    }

    public enum RAILDIRECTION
    {
        DEFAULT, NORTH, WEST, SOUTH, EAST, LEFT, FORWARD, RIGHT
    }
}
