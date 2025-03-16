package io.github.pistonpoek.magicalscepter.datagen.type;

import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantments;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class EnchantmentProvider extends FabricCodecDataProvider<Enchantment> {
    /**
     * Construct a mod enchantment provider for data generation.
     *
     * @param output Data output to generate enchantment data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public EnchantmentProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture, DataOutput.OutputType.DATA_PACK,
                ModRegistryKeys.directory(RegistryKeys.ENCHANTMENT), Enchantment.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Enchantment> provider, RegistryWrapper.WrapperLookup registries) {
        RegistryEntryLookup<Enchantment> enchantmentLookup = registries.getOrThrow(RegistryKeys.ENCHANTMENT);

        for (RegistryKey<Enchantment> scepterKey: ModEnchantments.ENCHANTMENT_KEYS) {
            addEnchantment(provider, enchantmentLookup, scepterKey);
        }
    }

    /**
     * Add an enchantment to the specified provider.
     *
     * @param provider Provider to add the enchantment to.
     * @param lookup Registry entry lookup for the enchantment.
     * @param key Registry key to add to the enchantment provider.
     */
    private static void addEnchantment(BiConsumer<Identifier, Enchantment> provider,
                                   RegistryEntryLookup<Enchantment> lookup,
                                   RegistryKey<Enchantment> key) {
        provider.accept(key.getValue(), lookup.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Enchantment";
    }
}
