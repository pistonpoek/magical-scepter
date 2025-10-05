package io.github.pistonpoek.magicalscepter.datagen.tag;

import io.github.pistonpoek.magicalscepter.registry.tag.ModBiomeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.tag.vanilla.VanillaBiomeTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.biome.BiomeKeys;

import java.util.concurrent.CompletableFuture;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.data.tag.vanilla.VanillaBiomeTagProvider
 */
public class ModBiomeTagProvider extends VanillaBiomeTagProvider {
    /**
     * Construct a mod biome tag provider for data generation.
     *
     * @param output           Data output to generate biome tag data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModBiomeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getTagBuilder(ModBiomeTags.OLD_TAIGA_CABIN_HAS_STRUCTURE)
                .add(BiomeKeys.OLD_GROWTH_PINE_TAIGA.getValue())
                .add(BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA.getValue());
    }
}
