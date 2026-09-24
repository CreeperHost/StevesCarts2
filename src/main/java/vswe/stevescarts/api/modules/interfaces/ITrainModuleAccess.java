package vswe.stevescarts.api.modules.interfaces;

/**
 * Implemented by modules that allow their cart to access modules and storage on
 * every connected cart in its train.
 */
public interface ITrainModuleAccess {
    default boolean grantsTrainModuleAccess() {
        return true;
    }
}
