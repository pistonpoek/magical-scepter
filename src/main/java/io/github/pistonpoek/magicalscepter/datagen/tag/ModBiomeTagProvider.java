package io.github.pistonpoek.magicalscepter.datagen.tag;

import io.github.pistonpoek.magicalscepter.registry.tag.ModBiomeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import java.util.concurrent.CompletableFuture;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.data.tags.BiomeTagsProvider
 */
public class ModBiomeTagProvider extends BiomeTagsProvider {
    /**
     * Construct a mod biome tag provider for data generation.
     *
     * @param output           Data output to generate biome tag data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModBiomeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        this.getOrCreateRawBuilder(ModBiomeTags.OLD_TAIGA_CABIN_HAS_STRUCTURE)
                .addElement(Biomes.OLD_GROWTH_PINE_TAIGA.identifier())
                .addElement(Biomes.OLD_GROWTH_SPRUCE_TAIGA.identifier());
    }
}
