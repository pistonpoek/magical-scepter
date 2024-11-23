package io.github.pistonpoek.magicalscepter.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import io.github.pistonpoek.magicalscepter.item.ModItems;

import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        Identifier scepterId = ModelIds.getItemModelId(ModItems.SCEPTER);
        Models.HANDHELD.upload(scepterId, TextureMap.layer0(scepterId), itemModelGenerator.writer);

        Model HANDHELD_TWO_LAYERS =
                new Model(Optional.of(Identifier.ofVanilla("item/handheld")),
                        Optional.empty(), TextureKey.LAYER0, TextureKey.LAYER1);

        Identifier magicalScepterId = ModelIds.getItemModelId(ModItems.MAGICAL_SCEPTER);
        HANDHELD_TWO_LAYERS.upload(magicalScepterId,
                TextureMap.layered(magicalScepterId.withSuffixedPath("_overlay"), magicalScepterId),
                itemModelGenerator.writer);

        Model SPAWN_EGG = new Model(Optional.of(ModelIds.getMinecraftNamespacedItem("template_spawn_egg")), Optional.empty());

        Identifier refractorSpawnEggId = ModelIds.getItemModelId(ModItems.REFRACTOR_SPAWN_EGG);
        SPAWN_EGG.upload(refractorSpawnEggId, new TextureMap(), itemModelGenerator.writer);
    }
}