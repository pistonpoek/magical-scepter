package io.github.pistonpoek.magicalscepter.datagen.tag;

import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.registry.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.data.tag.vanilla.VanillaItemTagProvider
 */
public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    /**
     * Construct a mod item tag provider for data generation.
     *
     * @param output Data output to generate item tag data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        FabricTagBuilder refractorPreferredWeaponsTagBuilder =
                getOrCreateTagBuilder(ModItemTags.REFRACTOR_PREFERRED_WEAPONS);
        refractorPreferredWeaponsTagBuilder.add(ModItems.SCEPTER);
        refractorPreferredWeaponsTagBuilder.add(ModItems.MAGICAL_SCEPTER);
    }
}