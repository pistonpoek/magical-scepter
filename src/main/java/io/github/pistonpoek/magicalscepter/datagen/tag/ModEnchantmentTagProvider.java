package io.github.pistonpoek.magicalscepter.datagen.tag;

import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantments;
import net.minecraft.data.DataOutput;
import net.minecraft.data.tag.vanilla.VanillaEnchantmentTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EnchantmentTags;

import java.util.concurrent.CompletableFuture;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.data.tag.vanilla.VanillaEnchantmentTagProvider
 */
public class ModEnchantmentTagProvider extends VanillaEnchantmentTagProvider {
    /**
     * Construct a mod enchantment tag provider for data generation.
     *
     * @param output Data output to generate enchantment tag data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModEnchantmentTagProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.builder(EnchantmentTags.NON_TREASURE)
                .add(ModEnchantments.INSIGHT_KEY);
    }
}
