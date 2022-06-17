package vswe.stevescarts.client.models;

import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;

@Deprecated(forRemoval = true)
public abstract class ModelWire extends ModelCartbase
{
    private static ResourceLocation texture;

    public ModelWire()
    {
        super();
    }

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelWire.texture;
    }

    @Override
    protected int getTextureWidth()
    {
        return 32;
    }

    @Override
    protected int getTextureHeight()
    {
        return 2;
    }

    protected int baseZ()
    {
        return 0;
    }

    protected void CreateEnd(final int x, final int y)
    {
        CreateEnd(x, y, baseZ());
    }

    protected void CreateEnd(final int x, final int y, final int z)
    {
        //TODO
        //        final ModelRenderer end = new ModelRenderer(this, 28, 0);
        //        AddRenderer(end);
        //        end.addBox(0.5f, 0.5f, 0.5f, 1, 1, 1, 0.0f);
        //        end.setPos(-7.5f + y, -5.5f - z, -5.5f + x);
    }

    protected void CreateWire(final int x1, final int y1, final int x2, final int y2)
    {
        CreateWire(x1, y1, baseZ(), x2, y2, baseZ());
    }

    protected void CreateWire(int x1, int y1, int z1, int x2, int y2, int z2)
    {
        if (x1 != x2 && y1 != y2 && z1 != z2)
        {
            return;
        }
        boolean rotate;
        boolean rotateZ;
        int length;
        if (y1 != y2)
        {
            rotate = false;
            rotateZ = false;
            length = y2 - y1 + 1;
            if (length < 0)
            {
                length *= -1;
                final int y3 = y1;
                y1 = y2;
                y2 = y3;
            }
        }
        else if (z1 != z2)
        {
            rotate = false;
            rotateZ = true;
            length = z2 - z1 + 1;
            if (length < 0)
            {
                length *= -1;
                final int z3 = z1;
                z1 = z2;
                z2 = z3;
            }
        }
        else
        {
            rotate = true;
            rotateZ = false;
            length = x2 - x1 + 1;
            if (length < 0)
            {
                length *= -1;
                final int x3 = x1;
                x1 = x2;
                x2 = x3;
            }
        }
        if (length > 13)
        {
            return;
        }
        //TODO
        //        final ModelRenderer wire = new ModelRenderer(this, 0, 0);
        //        AddRenderer(wire);
        //        wire.addBox(length / 2.0f, 0.5f, 0.5f, length, 1, 1, 0.0f);
        //        if (rotateZ)
        //        {
        //            //TODO RotationPoint
        //            wire.setPos(-7.5f + y1, -4.0f + length / 2.0f - z1, -5.5f + x1);
        //            wire.zRot = 4.712389f;
        //        }
        //        else if (rotate)
        //        {
        //            wire.setPos(-5.5f + y1, -5.5f - z1, -5.0f - length / 2.0f + x1);
        //            wire.yRot = 4.712389f;
        //        }
        //        else
        //        {
        //            wire.setPos(-7.0f - length / 2.0f + y1, -5.5f - z1, -5.5f + x1);
        //        }
    }

    static
    {
        ModelWire.texture = ResourceHelper.getResource("/models/wireModel.png");
    }
}
