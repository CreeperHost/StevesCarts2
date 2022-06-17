package vswe.stevescarts.client.models;

@Deprecated(forRemoval = true)
public class ModelWireTop extends ModelWire
{
    public ModelWireTop()
    {
        CreateEnd(1, 0);
        CreateEnd(4, 0);
        CreateEnd(8, 1);
        CreateEnd(6, 2);
        CreateEnd(5, 4);
        CreateEnd(1, 6);
        CreateEnd(6, 6);
        CreateEnd(3, 7);
        CreateEnd(4, 8);
        CreateEnd(1, 9);
        CreateEnd(0, 11);
        CreateEnd(4, 11);
        CreateEnd(7, 11);
        CreateEnd(5, 12);
        CreateEnd(9, 12);
        CreateEnd(2, 13);
        CreateWire(1, 1, 1, 4);
        CreateWire(2, 2, 5, 2);
        CreateWire(4, 1, 4, 1);
        CreateWire(2, 4, 4, 4);
        CreateWire(3, 5, 3, 6);
        CreateWire(8, 2, 8, 8);
        CreateWire(7, 6, 7, 6);
        CreateWire(5, 8, 7, 8);
        CreateWire(7, 9, 7, 10);
        CreateWire(1, 7, 1, 8);
        CreateWire(1, 11, 3, 11);
        CreateWire(2, 12, 2, 12);
        CreateWire(6, 12, 8, 12);
    }
}
