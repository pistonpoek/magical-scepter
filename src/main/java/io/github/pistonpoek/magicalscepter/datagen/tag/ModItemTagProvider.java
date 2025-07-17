package io.github.pistonpoek.magicalscepter.datagen.tag;

import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.registry.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.data.tag.vanilla.VanillaItemTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.data.tag.vanilla.VanillaItemTagProvider
 */
public class ModItemTagProvider extends VanillaItemTagProvider {
    /**
     * Construct a mod item tag provider for data generation.
     *
     * @param output Data output to generate item tag data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModItemTagProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        this.builder(ModItemTags.REFRACTOR_PREFERRED_WEAPONS)
                .add(ModItems.SCEPTER)
                .add(ModItems.MAGICAL_SCEPTER);

        this.builder(ModItemTags.SCEPTERS)
                .add(ModItems.ARCANE_SCEPTER)
                .add(ModItems.SCEPTER)
                .add(ModItems.MAGICAL_SCEPTER);

        this.builder(ModItemTags.SCEPTER_ENCHANTABLE)
                .addTag(ModItemTags.SCEPTERS);

        this.builder(ItemTags.DURABILITY_ENCHANTABLE)
                .addTag(ModItemTags.SCEPTERS);

        this.builder(ItemTags.VANISHING_ENCHANTABLE)
                .addTag(ModItemTags.SCEPTERS);
    }
}