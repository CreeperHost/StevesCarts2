package vswe.stevescarts.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Added to class files to register them with SC's, use with ISCPlguin
 */
@Deprecated
@Target(ElementType.TYPE)
public @interface SCLoadingPlugin
{

    /**
     * Set this if the Plugin depends on a mod, no need to set this if you are adding support from within your own mod
     *
     * @return the mod id of the mod
     */
    String dependentMod() default "";

    /**
     * If the plugin is incompatible with a mod use this to stop it from being loaded.
     *
     * @returnthe mod id of the incompatible mod
     */
    String incompatibleMod() default "";
}
