package vswe.stevescarts.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.Constants;

import java.util.HashMap;

public class ResourceHelper
{
    private static HashMap<String, ResourceLocation> resources;
    private static HashMap<String, ResourceLocation> pathResources;

    public static ResourceLocation getResource(final String path)
    {
        return new ResourceLocation(Constants.MOD_ID, "textures" + path.toLowerCase());
    }

    public static ResourceLocation getResourceFromPath(final String path)
    {
        return new ResourceLocation("textures" + path.toLowerCase());
    }

    public static void bindResource(final ResourceLocation resource)
    {
        if (resource != null)
        {
            ResourceLocation lowercaseLocation = new ResourceLocation(resource.getNamespace().toLowerCase(), resource.getPath().toLowerCase());
            RenderSystem.setShaderTexture(0, lowercaseLocation);
        }
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
