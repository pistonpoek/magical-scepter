package io.github.pistonpoek.magicalscepter.datagen.codec;

import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantments;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Mod data provider for enchantments.
 *
 * @see ModEnchantments
 */
public class EnchantmentProvider extends FabricCodecDataProvider<Enchantment> {
    /**
     * Construct a mod enchantment provider for data generation.
     *
     * @param output           Data output to generate enchantment data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public EnchantmentProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, PackOutput.Target.DATA_PACK,
                ModRegistryKeys.directory(Registries.ENCHANTMENT), Enchantment.DIRECT_CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Enchantment> provider, HolderLookup.Provider registries) {
        HolderGetter<Enchantment> enchantmentLookup = registries.lookupOrThrow(Registries.ENCHANTMENT);

        for (ResourceKey<Enchantment> scepterKey : ModEnchantments.KEYS) {
            addEnchantment(provider, enchantmentLookup, scepterKey);
        }
    }

    /**
     * Add an enchantment to the specified provider.
     *
     * @param provider Provider to add the enchantment to.
     * @param lookup   Registry entry lookup for the enchantment.
     * @param key      Registry key to add to the enchantment provider.
     */
    private static void addEnchantment(BiConsumer<Identifier, Enchantment> provider,
                                       HolderGetter<Enchantment> lookup,
                                       ResourceKey<Enchantment> key) {
        provider.accept(key.identifier(), lookup.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Enchantment";
    }
}
