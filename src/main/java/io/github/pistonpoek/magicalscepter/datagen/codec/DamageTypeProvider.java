package io.github.pistonpoek.magicalscepter.datagen.codec;

import io.github.pistonpoek.magicalscepter.entity.damage.ModDamageTypes;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Mod data provider for damage types.
 *
 * @see ModDamageTypes
 */
public class DamageTypeProvider extends FabricCodecDataProvider<DamageType> {
    /**
     * Construct a mod damage type provider for data generation.
     *
     * @param output           Data output to generate damage type data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public DamageTypeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture, DataOutput.OutputType.DATA_PACK,
                ModRegistryKeys.directory(RegistryKeys.DAMAGE_TYPE), DamageType.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, DamageType> provider, RegistryWrapper.WrapperLookup registries) {
        RegistryEntryLookup<DamageType> damageTypeLookup = registries.getOrThrow(RegistryKeys.DAMAGE_TYPE);

        for (RegistryKey<DamageType> damageTypeKey : ModDamageTypes.KEYS) {
            addDamageType(provider, damageTypeLookup, damageTypeKey);
        }
    }

    /**
     * Add a damage type to the specified provider.
     *
     * @param provider Provider to add the damage type to.
     * @param lookup   Registry entry lookup for the damage type.
     * @param key      Registry key to add to the damage type provider.
     */
    private static void addDamageType(BiConsumer<Identifier, DamageType> provider,
                                      RegistryEntryLookup<DamageType> lookup,
                                      RegistryKey<DamageType> key) {
        provider.accept(key.getValue(), lookup.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Damage Type";
    }
}
