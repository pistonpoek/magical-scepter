package io.github.pistonpoek.magicalscepter.datagen;

import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.mixin.client.data.ItemModelGeneratorMixin;
import io.github.pistonpoek.magicalscepter.scepter.ScepterTintSource;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.Optional;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.client.data.models.ModelProvider
 * @see net.minecraft.client.data.models.model.ModelTemplates
 */
@Environment(EnvType.CLIENT)
public class ModModelProvider extends FabricModelProvider {
    public static final ModelTemplate HANDHELD_SCEPTER = new ModelTemplate(
            Optional.of(Identifier.withDefaultNamespace("item/handheld")),
            Optional.empty(), TextureSlot.LAYER0, TextureSlot.LAYER1);

    /**
     * Construct a mod model provider for data generation.
     *
     * @param output Data output to generate mod model data into.
     */
    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(ModItems.ARCANE_SCEPTER, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.CHARGED_ARCANE_SCEPTER, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.SCEPTER, ModelTemplates.FLAT_HANDHELD_ITEM);

        if (itemModelGenerator instanceof ItemModelGeneratorMixin scepterItemModelGenerator) {
            registerScepter(scepterItemModelGenerator, ModItems.MAGICAL_SCEPTER, HANDHELD_SCEPTER);
        }

        itemModelGenerator.generateFlatItem(ModItems.SORCERER_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
    }

    /**
     * Register a tinted scepter model.
     *
     * @param itemModelGenerator Item model generator to output the generated item model into.
     * @param item               Item to create item model for.
     * @param model              Identifier to register the item model at.
     */
    public static void registerScepterTinted(ItemModelGeneratorMixin itemModelGenerator,
                                             Item item, Identifier model) {
        itemModelGenerator.getItemModelOutput().accept(item, ItemModelUtils.tintedModel(model, new ScepterTintSource()));
    }

    /**
     * Register a scepter model.
     *
     * @param itemModelGenerator Item model generator to output the generated item model into.
     * @param item               Item to create item model for.
     * @param model              Identifier to register the item model at.
     */
    public static void registerScepter(ItemModelGeneratorMixin itemModelGenerator,
                                       Item item, ModelTemplate model) {
        Identifier identifier = uploadTwoLayers(
                itemModelGenerator,
                item, model,
                ModelLocationUtils.getModelLocation(item, "_overlay"),
                ModelLocationUtils.getModelLocation(item));
        registerScepterTinted(itemModelGenerator, item, identifier);
    }

    /**
     * Upload a specified model with two texture layers for a specified item.
     *
     * @param itemModelGenerator Item model generator to output the generated item model into.
     * @param item               Item to upload item model for.
     * @param model              Model to upload for the item.
     * @param layer0             Identifier of the first layer.
     * @param layer1             Identifier of the second layer.
     * @return Identifier of the uploaded model.
     * @see ModelTemplate
     */
    public static Identifier uploadTwoLayers(ItemModelGeneratorMixin itemModelGenerator,
                                             Item item, ModelTemplate model,
                                             Identifier layer0, Identifier layer1) {
        return model.create(
                item,
                TextureMapping.layered(new Material(layer0), new Material(layer1)),
                itemModelGenerator.getModelOutput()
        );
    }
}