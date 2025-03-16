package io.github.pistonpoek.magicalscepter.datagen.tag;

import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.data.tag.vanilla.VanillaEntityTypeTagProvider
 */
public class ModEntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
    /**
     * Construct a mod entity type tag provider for data generation.
     *
     * @param output Data output to generate entity type tag data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModEntityTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        FabricTagBuilder IllagerTagBuilder =
                getOrCreateTagBuilder(EntityTypeTags.ILLAGER);
        IllagerTagBuilder.add(ModEntityType.REFRACTOR);
    }
}
