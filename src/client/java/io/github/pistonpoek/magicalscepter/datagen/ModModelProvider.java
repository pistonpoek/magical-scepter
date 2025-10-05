package io.github.pistonpoek.magicalscepter.datagen;

import io.github.pistonpoek.magicalscepter.item.ArcaneScepterItem;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.mixin.client.data.ItemModelGeneratorMixin;
import io.github.pistonpoek.magicalscepter.predicate.component.ModComponentPredicateTypes;
import io.github.pistonpoek.magicalscepter.predicate.item.ScepterExperiencePredicate;
import io.github.pistonpoek.magicalscepter.scepter.ScepterTintSource;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.client.render.item.property.bool.ComponentBooleanProperty;
import net.minecraft.item.Item;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.component.ComponentPredicate;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.client.data.ModelProvider
 * @see net.minecraft.client.data.Models
 */
@Environment(EnvType.CLIENT)
public class ModModelProvider extends FabricModelProvider {
    public static final Model HANDHELD_SCEPTER = new Model(
            Optional.of(Identifier.ofVanilla("item/handheld")),
            Optional.empty(), TextureKey.LAYER0, TextureKey.LAYER1);

    /**
     * Construct a mod model provider for data generation.
     *
     * @param output Data output to generate mod model data into.
     */
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.SCEPTER, Models.HANDHELD);

        Identifier filledArcaneScepter = itemModelGenerator.registerSubModel(ModItems.ARCANE_SCEPTER, "_filled", Models.HANDHELD);
        Identifier arcaneScepter = itemModelGenerator.upload(ModItems.ARCANE_SCEPTER, Models.HANDHELD);
        itemModelGenerator.registerCondition(
                ModItems.ARCANE_SCEPTER,
                new ComponentBooleanProperty(
                    new ComponentPredicate.Typed<>(
                            ModComponentPredicateTypes.SCEPTER_EXPERIENCE,
                            new ScepterExperiencePredicate(NumberRange.IntRange.atLeast(ArcaneScepterItem.EXPERIENCE_STEP))
                    )
                ),
                ItemModels.basic(filledArcaneScepter),
                ItemModels.basic(arcaneScepter)
        );

        if (itemModelGenerator instanceof ItemModelGeneratorMixin scepterItemModelGenerator) {
            registerScepter(scepterItemModelGenerator, ModItems.MAGICAL_SCEPTER, HANDHELD_SCEPTER);
        }

        itemModelGenerator.register(ModItems.SORCERER_SPAWN_EGG, Models.GENERATED);
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
        itemModelGenerator.getOutput().accept(item, ItemModels.tinted(model, new ScepterTintSource()));
    }

    /**
     * Register a scepter model.
     *
     * @param itemModelGenerator Item model generator to output the generated item model into.
     * @param item               Item to create item model for.
     * @param model              Identifier to register the item model at.
     */
    public static void registerScepter(ItemModelGeneratorMixin itemModelGenerator,
                                       Item item, Model model) {
        Identifier identifier = uploadTwoLayers(
                itemModelGenerator,
                item, model,
                ModelIds.getItemSubModelId(item, "_overlay"),
                ModelIds.getItemModelId(item));
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
     * @see Model
     */
    public static Identifier uploadTwoLayers(ItemModelGeneratorMixin itemModelGenerator,
                                             Item item, Model model,
                                             Identifier layer0, Identifier layer1) {
        return model.upload(item, TextureMap.layered(layer0, layer1), itemModelGenerator.getModelCollector());
    }
}