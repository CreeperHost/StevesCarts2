package vswe.stevescarts.datagen;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jspecify.annotations.NonNull;
import vswe.stevescarts.Constants;
import vswe.stevescarts.init.ModBlocks;
import vswe.stevescarts.init.ModItems;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class GeneratorModels extends ModelProvider {

    public GeneratorModels(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void registerModels(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels) {
        blockModels.createTrivialBlock(ModBlocks.CART_ASSEMBLER.get(), TexturedModel.CUBE_TOP_BOTTOM.updateTexture((mapping) -> {
            mapping.put(TextureSlot.TOP, getMaterial("cart_assembler_top"));
            mapping.put(TextureSlot.SIDE, getMaterial("cart_assembler_side_1"));
            mapping.put(TextureSlot.BOTTOM, getMaterial("cart_assembler_bot"));
        }));

        blockModels.createTrivialBlock(ModBlocks.MODULE_TOGGLER.get(), TexturedModel.CUBE_TOP_BOTTOM.updateTexture((mapping) -> {
            mapping.put(TextureSlot.TOP, getMaterial("module_toggler_top"));
            mapping.put(TextureSlot.SIDE, getMaterial("module_toggler_side"));
            mapping.put(TextureSlot.BOTTOM, getMaterial("module_toggler_bot"));
        }));

        blockModels.createTrivialBlock(
                ModBlocks.CARGO_MANAGER.get(),
                TexturedModel.CUBE.updateTexture(
                        (mapping) -> {
                            mapping.put(TextureSlot.PARTICLE, getMaterial("cargo_manager_top"));
                            mapping.put(TextureSlot.UP, getMaterial("cargo_manager_top"));
                            mapping.put(TextureSlot.DOWN, getMaterial("cargo_manager_bot"));
                            mapping.put(TextureSlot.SOUTH, getMaterial("cargo_manager_blue"));
                            mapping.put(TextureSlot.NORTH, getMaterial("cargo_manager_yellow"));
                            mapping.put(TextureSlot.EAST, getMaterial("cargo_manager_red"));
                            mapping.put(TextureSlot.WEST, getMaterial("cargo_manager_green"));
                        }
                ).updateTemplate((_ -> ModelTemplates.CUBE))
        );

        blockModels.createTrivialBlock(
                ModBlocks.LIQUID_MANAGER.get(),
                TexturedModel.CUBE.updateTexture(
                        (mapping) -> {
                            mapping.put(TextureSlot.PARTICLE, getMaterial("liquid_manager_top"));
                            mapping.put(TextureSlot.UP, getMaterial("liquid_manager_top"));
                            mapping.put(TextureSlot.DOWN, getMaterial("liquid_manager_red"));
                            mapping.put(TextureSlot.SOUTH, getMaterial("liquid_manager_blue"));
                            mapping.put(TextureSlot.NORTH, getMaterial("liquid_manager_yellow"));
                            mapping.put(TextureSlot.EAST, getMaterial("liquid_manager_red"));
                            mapping.put(TextureSlot.WEST, getMaterial("liquid_manager_green"));
                        }
                ).updateTemplate((_ -> ModelTemplates.CUBE))
        );

        blockModels.createTrivialBlock(
                ModBlocks.EXTERNAL_DISTRIBUTOR.get(),
                TexturedModel.CUBE.updateTexture(
                        (mapping) -> {
                            mapping.put(TextureSlot.PARTICLE, getMaterial("cargo_distributor_blue"));
                            mapping.put(TextureSlot.UP, getMaterial("cargo_distributor_orange"));
                            mapping.put(TextureSlot.DOWN, getMaterial("cargo_distributor_purple"));
                            mapping.put(TextureSlot.SOUTH, getMaterial("cargo_distributor_blue"));
                            mapping.put(TextureSlot.NORTH, getMaterial("cargo_distributor_yellow"));
                            mapping.put(TextureSlot.EAST, getMaterial("cargo_distributor_red"));
                            mapping.put(TextureSlot.WEST, getMaterial("cargo_distributor_green"));
                        }
                ).updateTemplate((_ -> ModelTemplates.CUBE))
        );

        createStraightRail(ModBlocks.JUNCTION.get(), "junction_rail", blockModels, itemModels);
        createStraightRail(ModBlocks.ADVANCED_DETECTOR.get(), "advanced_detector_rail", blockModels, itemModels);

        // Metal Blocks
        blockModels.createTrivialCube(ModBlocks.REINFORCED_METAL.get());
        blockModels.createTrivialCube(ModBlocks.GALGADORIAN_METAL.get());
        blockModels.createTrivialCube(ModBlocks.ENHANCED_GALGADORIAN_METAL.get());

        // Upgrades
        createUpgrade(ModBlocks.BATTERIES, "batteries", blockModels);
        createUpgrade(ModBlocks.POWER_CRYSTAL, "power_crystal", blockModels);
        createUpgrade(ModBlocks.KNOWLEDGE, "module_knowledge", blockModels);
        createUpgrade(ModBlocks.INDUSTRIAL_ESPIONAGE, "industrial_espionage", blockModels);
        createUpgrade(ModBlocks.EXPERIENCED_ASSEMBLER, "experienced_assembler", blockModels);
        createUpgrade(ModBlocks.NEW_ERA, "new_era", blockModels);
        createUpgrade(ModBlocks.COTWO_FRIENDLY, "co2_friendly", blockModels);
        createUpgrade(ModBlocks.GENERIC_ENGINE, "generic_engine", blockModels);
        createUpgrade(ModBlocks.MODULE_INPUT, "module_input", blockModels);
        createUpgrade(ModBlocks.PRODUCTION_LINE, "production_line", blockModels);
        createUpgrade(ModBlocks.CART_DEPLOYER, "cart_deployer", blockModels);
        createUpgrade(ModBlocks.CART_MODIFIER, "cart_modifier", blockModels);
        createUpgrade(ModBlocks.CART_CRANE, "cart_crane", blockModels);
        createUpgrade(ModBlocks.REDSTONE_CONTROL, "redstone_control", blockModels);
        createUpgrade(ModBlocks.CREATIVE_MODE, "creative_mode", blockModels);
        createUpgrade(ModBlocks.QUICK_DEMOLISHER, "quick_demolisher", blockModels);
        createUpgrade(ModBlocks.ENTROPY, "entropy", blockModels);
        createUpgrade(ModBlocks.MANAGER_BRIDGE, "manager_bridge", blockModels);
        createUpgrade(ModBlocks.THERMAL_ENGINE, "thermal_engine", blockModels);
        createUpgrade(ModBlocks.SOLAR_PANEL, "solar_panel", blockModels);

        ModItems.ITEMS.getEntries().forEach(entry -> {
            Item item = entry.get();

            // BlockItems are handled automatically by the block model generator.
            if (item instanceof BlockItem) {
                return;
            }

            itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
        });
    }

    private final ModelTemplate UPGRADE = ModelTemplates
            .create(TextureSlot.SIDE, TextureSlot.FRONT, TextureSlot.PARTICLE)
            .extend()
            .parent(mcLocation("block/block"))
            .element(elements -> elements
                    .from(0, 3, 3)
                    .to(2, 13, 13)
                    .allFaces((direction, builder) -> {
                        switch (direction) {
                            case NORTH -> builder.texture(TextureSlot.SIDE).uvs(0, 3, 2, 13).cullface(Direction.NORTH);
                            case EAST -> builder.texture(TextureSlot.FRONT).uvs(3, 3, 13, 13).cullface(Direction.EAST);
                            case SOUTH -> builder.texture(TextureSlot.SIDE).uvs(0, 3, 2, 13).cullface(Direction.SOUTH);
                            case WEST -> builder.texture(TextureSlot.FRONT).uvs(3, 3, 13, 13).cullface(Direction.WEST);
                            case UP -> builder.texture(TextureSlot.SIDE).uvs(0, 3, 2, 13);
                            case DOWN -> builder.texture(TextureSlot.SIDE).uvs(0, 3, 2, 13).cullface(Direction.DOWN);
                        }
                    })
            ).build();

    private void createUpgrade(Supplier<Block> block, String name, BlockModelGenerators blockModels) {
        blockModels.createTrivialBlock(
                block.get(),
                TexturedModel.CUBE.updateTexture(
                        (mapping) -> {
                            mapping.put(TextureSlot.SIDE, getMaterial("upgrade_side_0_icon"));
                            mapping.put(TextureSlot.FRONT, getMaterial("%s_icon".formatted(name)));
                            mapping.put(TextureSlot.PARTICLE, getMaterial("upgrade_side_0_icon"));
                        }
                ).updateTemplate((_ -> UPGRADE))
        );
    }

    private void createStraightRail(Block block, String texture, BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        TextureMapping mapping = TextureMapping.rail(getMaterial(texture));

        Identifier flatModel = ModelTemplates.RAIL_FLAT.create(block, mapping, itemModels.modelOutput);
        Identifier risingNEModel = ModelTemplates.RAIL_RAISED_NE.create(block, mapping, itemModels.modelOutput);
        Identifier risingSWModel = ModelTemplates.RAIL_RAISED_SW.create(block, mapping, itemModels.modelOutput);
        Identifier itemModel = ModelTemplates.FLAT_ITEM.create(block.asItem(), TextureMapping.layer0(getMaterial(texture)), itemModels.modelOutput);

        MultiVariant flat = BlockModelGenerators.plainVariant(flatModel);
        MultiVariant risingNE = BlockModelGenerators.plainVariant(risingNEModel);
        MultiVariant risingSW = BlockModelGenerators.plainVariant(risingSWModel);

        itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(itemModel));

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(
                                PropertyDispatch
                                        .initial(BlockStateProperties.RAIL_SHAPE_STRAIGHT)
                                        .generate(shape -> switch (shape) {
                                            case NORTH_SOUTH -> flat;
                                            case EAST_WEST -> flat.with(BlockModelGenerators.Y_ROT_90);
                                            case ASCENDING_EAST -> risingNE.with(BlockModelGenerators.Y_ROT_90);
                                            case ASCENDING_WEST -> risingSW.with(BlockModelGenerators.Y_ROT_90);
                                            case ASCENDING_NORTH -> risingNE;
                                            case ASCENDING_SOUTH -> risingSW;
                                            default -> throw new UnsupportedOperationException(
                                                    "Unsupported straight rail shape: " + shape
                                            );
                                        })
                        )
        );
    }

    private Material getMaterial(String name) {
        return new Material(modLocation("block/%s".formatted(name)));
    }

    @Override
    protected @NonNull Stream<? extends Holder<Block>> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream();
    }

    @Override
    protected @NonNull Stream<? extends Holder<Item>> getKnownItems() {
        return ModItems.ITEMS.getEntries().stream();
    }
}
