package vswe.stevescarts.client.models;

import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;

public class ModelNote extends ModelCartbase
{
    private static ResourceLocation texture;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelNote.texture;
    }

    @Override
    protected int getTextureHeight()
    {
        return 32;
    }

    public ModelNote()
    {
        AddSpeaker(false);
        AddSpeaker(true);
    }

    private void AddSpeaker(final boolean opposite)
    {
        //TODO
        //        final ModelRenderer noteAnchor = new ModelRenderer(this);
        //        AddRenderer(noteAnchor);
        //        final ModelRenderer base = new ModelRenderer(this, 0, 0);
        //        fixSize(base);
        //        noteAnchor.addChild(base);
        //        base.addBox(8.0f, 6.0f, 6.0f, 16, 12, 12, 0.0f);
        //        base.setPos(-16.0f, -13.5f, -12.0f + 14.0f * (opposite ? 1 : -1));
    }

    static
    {
        ModelNote.texture = ResourceHelper.getResource("/models/noteModel.png");
    }
}
