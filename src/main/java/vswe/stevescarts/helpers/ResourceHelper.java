package vswe.stevescarts.helpers;

import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.Constants;

import java.util.Locale;

@Deprecated
public class ResourceHelper
{
    public static ResourceLocation getResource(final String path)
    {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures" + path.toLowerCase(Locale.ROOT));
    }

    public static ResourceLocation getResourceFromPath(final String path)
    {
        return ResourceLocation.withDefaultNamespace("textures" + path.toLowerCase(Locale.ROOT));
    }
}
