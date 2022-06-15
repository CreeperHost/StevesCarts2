package vswe.stevescarts.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

/**
 * @link https://github.com/mezz/JustEnoughItems/blob/1.15/src/main/java/mezz/jei/plugins/vanilla/ingredients/fluid/FluidStackRenderer.java
 */

@OnlyIn(Dist.CLIENT)
public class FluidRenderer
{
    public static final FluidRenderer INSTANCE = new FluidRenderer(1000, 16, 16, 16);

    private static final int TEX_WIDTH = 16;
    private static final int TEX_HEIGHT = 16;

    private final int capacityMb;
    private final int width;
    private final int height;
    private final int minHeight;

    public FluidRenderer(int capacityMb, int width, int height, int minHeight)
    {
        this.capacityMb = capacityMb;
        this.width = width;
        this.height = height;
        this.minHeight = minHeight;
    }

    public void render(final int xPosition, final int yPosition, @Nonnull FluidStack fluidStack)
    {
        //        RenderSystem.enableBlend();
        //        RenderSystem.enableAlphaTest();

        drawFluid(xPosition, yPosition, fluidStack);

        //TODO
//        RenderSystem.color4f(1, 1, 1, 1);
        //
        //        RenderSystem.disableAlphaTest();
        //        RenderSystem.disableBlend();
    }

    private void drawFluid(final int xPosition, final int yPosition, @Nonnull FluidStack fluidStack)
    {
        //TODO
//        if (fluidStack.isEmpty())
//        {
//            return;
//        }
//
//        Fluid fluid = fluidStack.getFluid();
//
//        TextureAtlasSprite fluidStillSprite = getStillFluidSprite(fluidStack);
//
//        FluidAttributes attributes = fluid.getAttributes();
//        int fluidColor = attributes.getColor(fluidStack);
//
//        int amount = fluidStack.getAmount();
//        int scaledAmount = (amount * height) / capacityMb;
//        if (amount > 0 && scaledAmount < minHeight)
//        {
//            scaledAmount = minHeight;
//        }
//        if (scaledAmount > height)
//        {
//            scaledAmount = height;
//        }
//
//        drawTiledSprite(xPosition, yPosition, width, height, fluidColor, scaledAmount, fluidStillSprite);
    }

    private void drawTiledSprite(final int xPosition, final int yPosition, final int tiledWidth, final int tiledHeight, int color, int scaledAmount, TextureAtlasSprite sprite)
    {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        setGLColorFromInt(color);

        final int xTileCount = tiledWidth / TEX_WIDTH;
        final int xRemainder = tiledWidth - (xTileCount * TEX_WIDTH);
        final int yTileCount = scaledAmount / TEX_HEIGHT;
        final int yRemainder = scaledAmount - (yTileCount * TEX_HEIGHT);

        final int yStart = yPosition + tiledHeight;

        for (int xTile = 0; xTile <= xTileCount; xTile++)
        {
            for (int yTile = 0; yTile <= yTileCount; yTile++)
            {
                int width = (xTile == xTileCount) ? xRemainder : TEX_WIDTH;
                int height = (yTile == yTileCount) ? yRemainder : TEX_HEIGHT;
                int x = xPosition + (xTile * TEX_WIDTH);
                int y = yStart - ((yTile + 1) * TEX_HEIGHT);
                if (width > 0 && height > 0)
                {
                    int maskTop = TEX_HEIGHT - height;
                    int maskRight = TEX_WIDTH - width;

                    drawTextureWithMasking(x, y, sprite, maskTop, maskRight, 100);
                }
            }
        }
    }

    private static TextureAtlasSprite getStillFluidSprite(FluidStack fluidStack)
    {
        Fluid fluid = fluidStack.getFluid();

        //TODO
//        FluidAttributes attributes = fluid.getAttributes();
//        ResourceLocation fluidStill = attributes.getStillTexture(fluidStack);
//        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStill);
        return null;
    }

    public static void setGLColorFromInt(int color)
    {
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        float alpha = ((color >> 24) & 0xFF) / 255F;

        //TODO
//        RenderSystem.color4f(red, green, blue, alpha);
    }

    private static void drawTextureWithMasking(double xCoord, double yCoord, TextureAtlasSprite textureSprite, int maskTop, int maskRight, double zLevel)
    {
        double uMin = textureSprite.getU0();
        double uMax = textureSprite.getU1();
        double vMin = textureSprite.getV0();
        double vMax = textureSprite.getV1();
        uMax = uMax - (maskRight / 16.0 * (uMax - uMin));
        vMax = vMax - (maskTop / 16.0 * (vMax - vMin));

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(xCoord, yCoord + 16, zLevel).uv((float) uMin, (float) vMax).endVertex();
        bufferBuilder.vertex(xCoord + 16 - maskRight, yCoord + 16, zLevel).uv((float) uMax, (float) vMax).endVertex();
        bufferBuilder.vertex(xCoord + 16 - maskRight, yCoord + maskTop, zLevel).uv((float) uMax, (float) vMin).endVertex();
        bufferBuilder.vertex(xCoord, yCoord + maskTop, zLevel).uv((float) uMin, (float) vMin).endVertex();
        tessellator.end();
    }
}
