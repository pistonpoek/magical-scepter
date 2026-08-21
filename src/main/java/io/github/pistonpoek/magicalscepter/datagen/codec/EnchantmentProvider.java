package io.github.pistonpoek.magicalscepter.datagen.codec;

import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;

import java.util.concurrent.CompletableFuture;

/**
 * Mod data provider for enchantments.
 *
 * @see ModEnchantments
 */
public class EnchantmentProvider extends FabricDynamicRegistryProvider {
    /**
     * Construct a mod enchantment provider for data generation.
     *
     * @param output           Data output to generate enchantment data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public EnchantmentProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }
// TODO check should it be a dynamic registry or codec?
    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(Registries.ENCHANTMENT));
    }

    @Override
    public String getName() {
        return "Enchantment";
    }
}
