package vswe.stevescarts.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import vswe.stevescarts.client.creativetabs.CreativeTabSC2Modules;
import vswe.stevescarts.init.ModItems;
import vswe.stevescarts.modules.data.ModuleData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemCartModule extends Item
{
    public ModuleData moduleData;

    public ItemCartModule(ModuleData moduleData)
    {
        super(new Item.Properties().tab(CreativeTabSC2Modules.INSTANCE));
        this.moduleData = moduleData;
        setRegistryName(getModuleName().trim());
    }

    @Override
    public Component getName(ItemStack stack)
    {
        return new TranslatableComponent("item.stevescarts." + moduleData.getRawName());
    }

    public String getModuleName()
    {
        String name = moduleData.getName();
        if (name.isEmpty())
        {
            return "Unknown SC2 module";
        }
        return name;
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> textComponents, TooltipFlag flag)
    {
        if (moduleData != null)
        {
            moduleData.addInformation(textComponents, stack.getTag());
            textComponents.add(new TextComponent("ID " + moduleData.getID()));
        }
        else if (!stack.isEmpty() && stack.getItem() instanceof ItemCartModule)
        {
            textComponents.add(new TranslatableComponent("Module id " + moduleData.getID()));
        }
        else
        {
            textComponents.add(new TranslatableComponent("Unknown module id"));
        }
        super.appendHoverText(stack, world, textComponents, flag);
    }

    public ModuleData getModuleData()
    {
        return moduleData;
    }

    public static ItemStack createModuleStack(int id)
    {
        for (ItemCartModule module : ModItems.MODULES)
        {
            if (module.moduleData.getID() == id)
            {
                return new ItemStack(module);
            }
        }
        return ItemStack.EMPTY;
    }


    //
    //	public ModuleData getModuleData(@Nonnull ItemStack itemstack) {
    //		return getModuleData(itemstack, false);
    //	}
    //
    //	public ModuleData getModuleData(@Nonnull ItemStack itemstack, final boolean ignoreSize) {
    //		if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemCartModule && (ignoreSize || TileEntityCartAssembler.getSlotStatus(itemstack) != TileEntityCartAssembler.getRemovedSize())) {
    //			return ModuleData.getList().get((byte) itemstack.getItemDamage());
    //		}
    //		return null;
    //	}
    //
    public void addExtraDataToCart(final CompoundTag save, @Nonnull ItemStack module, final int i)
    {
        if (module.getTag() != null && module.getTag().contains("Data"))
        {
            save.putByte("Data" + i, module.getTag().getByte("Data"));
        }
        else
        {
            ItemCartModule itemCartModule = (ItemCartModule) module.getItem();
            final ModuleData data = itemCartModule.getModuleData();
            if (data.isUsingExtraData())
            {
                save.putByte("Data" + i, data.getDefaultExtraData());
            }
        }
    }
    //
    //	public void addExtraDataToModule(final NBTTagCompound save, final ModuleBase module, final int i) {
    //		if (module.hasExtraData()) {
    //			save.setByte("Data" + i, module.getExtraData());
    //		}
    //	}
    //
    //	public void addExtraDataToModule(@Nonnull ItemStack module, final NBTTagCompound info, final int i) {
    //		NBTTagCompound save = module.getTagCompound();
    //		if (save == null) {
    //			module.setTagCompound(save = new NBTTagCompound());
    //		}
    //		if (info != null && info.hasKey("Data" + i)) {
    //			save.setByte("Data", info.getByte("Data" + i));
    //		} else {
    //			final ModuleData data = getModuleData(module, true);
    //			if (data.isUsingExtraData()) {
    //				save.setByte("Data", data.getDefaultExtraData());
    //			}
    //		}
    //	}
    //
    //	@Override
    //	public Map<Integer, ResourceLocation> getModels() {
    //		Map<Integer, ResourceLocation> map = new HashMap<>();
    //		for (ModuleData data: ModuleData.getList().values()) {
    //			String name = data.getRawName();
    //			if (name != null) {
    //				map.put((int) data.getID(), new ResourceLocation(Constants.MOD_ID, "module_" + name));
    //			}
    //		}
    //		return map;
    //	}

}
