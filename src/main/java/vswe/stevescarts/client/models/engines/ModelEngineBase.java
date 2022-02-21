package vswe.stevescarts.client.models.engines;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import vswe.stevescarts.client.models.ModelCartbase;

public abstract class ModelEngineBase extends ModelCartbase
{
    protected ModelPart anchor;

    public ModelEngineBase()
    {
        //TODO
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        AddRenderer(anchor = partdefinition.bake(0, 0));
        anchor.setPos(10.5f, 0.5f, -0.0f);
        anchor.yRot = -1.5707964f;
    }
}
