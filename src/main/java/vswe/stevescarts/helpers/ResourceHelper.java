package vswe.stevescarts.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.Constants;

import java.util.HashMap;
import java.util.Locale;

public class ResourceHelper
{
    private static HashMap<String, ResourceLocation> resources;
    private static HashMap<String, ResourceLocation> pathResources;

    public static ResourceLocation getResource(final String path)
    {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures" + path.toLowerCase(Locale.ROOT));
    }

    public static ResourceLocation getResourceFromPath(final String path)
    {
        return ResourceLocation.withDefaultNamespace("textures" + path.toLowerCase(Locale.ROOT));
    }

    public static void bindResource(final ResourceLocation resource)
    {
        //TODO Replace with updated texture handling
//        if (resource != null)
//        {
//            ResourceLocation lowercaseLocation = ResourceLocation.fromNamespaceAndPath(resource.getNamespace(), resource.getPath());
//            RenderSystem.setShaderTexture(0, lowercaseLocation);
//        }
    }

    public static void bindResource(final String path)
    {
        if (ResourceHelper.resources.containsKey(path))
        {
            bindResource(ResourceHelper.resources.get(path));
        }
        else
        {
            final ResourceLocation resource = getResource(path);
            ResourceHelper.resources.put(path, resource);
            bindResource(resource);
        }
    }

    public static void bindResourcePath(final String path)
    {
        if (ResourceHelper.pathResources.containsKey(path))
        {
            bindResource(ResourceHelper.pathResources.get(path));
        }
        else
        {
            final ResourceLocation resource = getResourceFromPath(path);
            ResourceHelper.pathResources.put(path, resource);
            bindResource(resource);
        }
    }

    static
    {
        ResourceHelper.resources = new HashMap<>();
        ResourceHelper.pathResources = new HashMap<>();
    }
}
