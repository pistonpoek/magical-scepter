package io.github.pistonpoek.magicalscepter.datagen.tag;

import io.github.pistonpoek.magicalscepter.registry.tag.ModStructureTags;
import io.github.pistonpoek.magicalscepter.world.gen.structure.ModStructureKeys;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.StructureTagsProvider;

import java.util.concurrent.CompletableFuture;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.data.tags.StructureTagsProvider
 */
public class ModStructureTagProvider extends StructureTagsProvider {
    /**
     * Construct a mod item tag provider for data generation.
     *
     * @param output           Data output to generate item tag data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModStructureTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        this.tag(ModStructureTags.ON_OLD_TAIGA_CABIN_MAPS)
                .add(ModStructureKeys.OLD_TAIGA_CABIN);
    }
}
