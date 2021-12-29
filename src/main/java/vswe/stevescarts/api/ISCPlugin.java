package vswe.stevescarts.api;

/**
 * Created by modmuss50 on 15/11/16.
 */
@Deprecated
public interface ISCPlugin
{

    /**
     * Called at the right time to add helpers
     *
     * @param plugins
     */
    default public void loadAddons(ISCHelpers plugins)
    {

    }

}
