package vswe.stevescarts.api.modules.interfaces;

public interface IActivatorModule
{
    boolean isActive(final int p0);

    void doActivate(final int p0);

    void doDeActivate(final int p0);
}
