package io.github.pistonpoek.magicalscepter.datagen.tag;

import io.github.pistonpoek.magicalscepter.registry.tag.ModBiomeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.concurrent.CompletableFuture;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.data.tag.vanilla.VanillaBiomeTagProvider
 */
public class ModBiomeTagProvider extends FabricTagProvider<Biome> {

    public ModBiomeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BIOME, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(ModBiomeTags.OLD_TAIGA_CABIN_HAS_STRUCTURE)
                .add(BiomeKeys.OLD_GROWTH_PINE_TAIGA)
                .add(BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA);
    }
}
