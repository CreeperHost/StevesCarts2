package vswe.stevescarts.helpers;

import net.minecraft.resources.Identifier;
import vswe.stevescarts.Constants;

import java.util.Locale;

@Deprecated
public class ResourceHelper {
    public static Identifier getResource(final String path) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures" + path.toLowerCase(Locale.ROOT));
    }

    public static Identifier getResourceFromPath(final String path) {
        return Identifier.withDefaultNamespace("textures" + path.toLowerCase(Locale.ROOT));
    }
}
