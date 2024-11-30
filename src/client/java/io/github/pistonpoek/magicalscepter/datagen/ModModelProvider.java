package io.github.pistonpoek.magicalscepter.datagen;

import io.github.pistonpoek.magicalscepter.mixin.client.data.ItemModelGeneratorMixin;
import io.github.pistonpoek.magicalscepter.scepter.ScepterTintSource;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
    public static final Model HANDHELD_SCEPTER = new Model(
            Optional.of(Identifier.ofVanilla("item/handheld")),
            Optional.empty(), TextureKey.LAYER0, TextureKey.LAYER1);

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.SCEPTER, Models.HANDHELD);

        if (itemModelGenerator instanceof ItemModelGeneratorMixin scepterItemModelGenerator) {
            registerScepter(scepterItemModelGenerator, ModItems.MAGICAL_SCEPTER, HANDHELD_SCEPTER);
        }

        itemModelGenerator.registerSpawnEgg(ModItems.REFRACTOR_SPAWN_EGG, 9804699, 6307420);
    }

    public static void registerScepterTinted(ItemModelGeneratorMixin itemModelGenerator,
                                             Item item, Identifier model) {
        itemModelGenerator.getOutput().accept(item, ItemModels.tinted(model, new ScepterTintSource()));
    }

    public static void registerScepter(ItemModelGeneratorMixin itemModelGenerator,
                                       Item item, Model model) {
        Identifier identifier = uploadTwoLayers(
                itemModelGenerator,
                item, model,
                ModelIds.getItemSubModelId(item, "_overlay"),
                ModelIds.getItemModelId(item));
        registerScepterTinted(itemModelGenerator, item, identifier);
    }

    public static Identifier uploadTwoLayers(ItemModelGeneratorMixin itemModelGenerator,
                                             Item item, Model model,
                                             Identifier layer0, Identifier layer1) {
        return model.upload(item, TextureMap.layered(layer0, layer1), itemModelGenerator.getModelCollector());
    }
}