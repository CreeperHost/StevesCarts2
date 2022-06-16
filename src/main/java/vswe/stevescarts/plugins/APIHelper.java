package vswe.stevescarts.plugins;

import vswe.stevescarts.api.ISCHelpers;
import vswe.stevescarts.api.farms.ICropModule;
import vswe.stevescarts.api.farms.ITreeModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by modmuss50 on 15/11/16.
 */
@Deprecated(forRemoval = true)
public class APIHelper implements ISCHelpers
{

    public static List<ITreeModule> treeModules = new ArrayList<>();
    public static List<ICropModule> cropModules = new ArrayList<>();

    @Override
    public void registerTree(ITreeModule treeModule)
    {
        treeModules.add(treeModule);
    }

    @Override
    public void registerCrop(ICropModule cropModule)
    {
        cropModules.add(cropModule);
    }
}
