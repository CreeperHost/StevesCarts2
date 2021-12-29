package vswe.stevescarts.api;

import vswe.stevescarts.api.farms.ICropModule;
import vswe.stevescarts.api.farms.ITreeModule;

/**
 * Created by modmuss50 on 15/11/16.
 */
@Deprecated
public interface ISCHelpers
{

    /**
     * Used to add a new tree
     *
     * @param treeModule
     */
    public void registerTree(ITreeModule treeModule);

    /**
     * Used to add a new Crop
     *
     * @param cropModule
     */
    public void registerCrop(ICropModule cropModule);

}
