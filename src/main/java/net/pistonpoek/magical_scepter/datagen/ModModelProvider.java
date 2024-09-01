package net.pistonpoek.magical_scepter.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.pistonpoek.magical_scepter.item.ModItems;

import java.util.Optional;

import static net.minecraft.data.client.Models.HANDHELD_ROD;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Add empty scepter item model
        Identifier emptyScepterId = ModelIds.getItemModelId(ModItems.EMPTY_SCEPTER);
        HANDHELD_ROD.upload(emptyScepterId, TextureMap.layer0(emptyScepterId), itemModelGenerator.writer);

        // Add scepter item model
        Identifier scepterId = ModelIds.getItemModelId(ModItems.SCEPTER);
        Model HANDHELD_ROD_TWO_LAYERS =
                new Model(Optional.of(Identifier.ofVanilla("item/handheld_rod")),
                        Optional.empty(), TextureKey.LAYER0, TextureKey.LAYER1);
        HANDHELD_ROD_TWO_LAYERS.upload(scepterId, TextureMap.layered(scepterId.withSuffixedPath("_overlay"), scepterId), itemModelGenerator.writer);
    }
}