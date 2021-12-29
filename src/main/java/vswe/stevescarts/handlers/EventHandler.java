package vswe.stevescarts.handlers;

public class EventHandler
{
    public EventHandler()
    {
        //		ForgeChunkManager.setForcedChunkLoadingCallback(StevesCarts.instance, this);
    }

    //	@Override
    //	public void ticketsLoaded(final List<ForgeChunkManager.Ticket> tickets, final World world) {
    //		for (final ForgeChunkManager.Ticket ticket : tickets) {
    //			final Entity entity = ticket.getEntity();
    //			if (entity instanceof EntityMinecartModular) {
    //				final EntityMinecartModular cart = (EntityMinecartModular) entity;
    //				cart.loadChunks(ticket);
    //			}
    //		}
    //	}

    //	@SubscribeEvent
    //	public void onRenderTick(final TickEvent.RenderTickEvent event) {
    //		if (event.phase == TickEvent.Phase.END) {
    //			renderOverlay();
    //		}
    //	}
    //
    //	@SideOnly(Side.CLIENT)
    //	private void renderOverlay() {
    //		final Minecraft minecraft = Minecraft.getMinecraft();
    //		final EntityPlayer player = minecraft.player;
    //		if (minecraft.currentScreen == null && player.getRidingEntity() != null && player.getRidingEntity() instanceof EntityMinecartModular) {
    //			((EntityMinecartModular) player.getRidingEntity()).renderOverlay(minecraft);
    //		}
    //	}
    //
    //	@SubscribeEvent
    //	public void enterChunk(final EntityEvent.EnteringChunk event) {
    //		if (!event.getEntity().isDead && event.getEntity() instanceof EntityMinecartModular) {
    //			((EntityMinecartModular) event.getEntity()).loadChunks(event.getNewChunkX(), event.getNewChunkZ());
    //		}
    //	}
    //
    //	@SubscribeEvent
    //	public void onCrafting(final PlayerEvent.ItemCraftedEvent event) {
    //		onCrafting(event.player, event.crafting, event.craftMatrix);
    //	}
    //
    //	private void onCrafting(final EntityPlayer player, @Nonnull ItemStack item, final IInventory craftMatrix) {
    //		if (item.getItem() == ModItems.COMPONENTS || item.getItem() == ModItems.MODULES) {
    //			for (int i = 0; i < craftMatrix.getSizeInventory(); ++i) {
    //				@Nonnull
    //				ItemStack sItem = craftMatrix.getStackInSlot(i);
    //				if (!sItem.isEmpty() && sItem.getItem().getContainerItem() != null) {
    //					craftMatrix.setInventorySlotContents(i, ItemStack.EMPTY);
    //				}
    //			}
    //		}
    //	}
}
