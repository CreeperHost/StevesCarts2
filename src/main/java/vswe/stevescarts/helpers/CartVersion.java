package vswe.stevescarts.helpers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.items.ItemCarts;

import javax.annotation.Nonnull;
import java.util.ArrayList;

@Deprecated(forRemoval = true)
public abstract class CartVersion
{
    private static ArrayList<CartVersion> versions;

    public CartVersion()
    {
        CartVersion.versions.add(this);
    }

    public abstract void update(final ArrayList<Byte> p0);

    public static byte[] updateCart(final EntityMinecartModular cart, byte[] data)
    {
        if (cart.cartVersion != getCurrentVersion())
        {
            data = updateArray(data, cart.cartVersion);
            cart.cartVersion = (byte) getCurrentVersion();
        }
        return data;
    }

    private static byte[] updateArray(byte[] data, int version)
    {
        final ArrayList<Byte> modules = new ArrayList<>();
        for (final byte b : data)
        {
            modules.add(b);
        }
        while (version < getCurrentVersion())
        {
            CartVersion.versions.get(version++).update(modules);
        }
        data = new byte[modules.size()];
        for (int i = 0; i < data.length; ++i)
        {
            data[i] = modules.get(i);
        }
        return data;
    }

    public static void updateItemStack(@Nonnull ItemStack item)
    {
        if (!item.isEmpty() && item.getItem() instanceof ItemCarts)
        {
            final CompoundTag info = item.getTag();
            if (info != null)
            {
                final int version = info.getByte("CartVersion");
                if (version != getCurrentVersion())
                {
                    info.putByteArray("Modules", updateArray(info.getByteArray("Modules"), version));
                    addVersion(info);
                }
            }
        }
    }

    public static void addVersion(@Nonnull ItemStack item)
    {
        if (!item.isEmpty() && item.getItem() instanceof ItemCarts)
        {
            final CompoundTag info = item.getTag();
            if (info != null)
            {
                addVersion(info);
            }
        }
    }

    private static void addVersion(final CompoundTag info)
    {
        info.putByte("CartVersion", (byte) getCurrentVersion());
    }

    private static int getCurrentVersion()
    {
        return CartVersion.versions.size();
    }

    static
    {
        CartVersion.versions = new ArrayList<>();
        new CartVersion()
        {
            @Override
            public void update(final ArrayList<Byte> modules)
            {
                final int index = modules.indexOf((byte) 17);
                if (index != -1)
                {
                    modules.set(index, (byte) 16);
                }
                if (modules.contains((byte) 16))
                {
                    modules.add((byte) 64);
                }
            }
        };
        new CartVersion()
        {
            @Override
            public void update(final ArrayList<Byte> modules)
            {
            }
        };
    }
}
