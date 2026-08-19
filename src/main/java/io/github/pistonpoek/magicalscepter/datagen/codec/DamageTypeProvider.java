package io.github.pistonpoek.magicalscepter.datagen.codec;

import io.github.pistonpoek.magicalscepter.entity.damage.ModDamageTypes;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
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
    public DamageTypeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, PackOutput.Target.DATA_PACK,
                ModRegistryKeys.directory(Registries.DAMAGE_TYPE), DamageType.DIRECT_CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, DamageType> provider, HolderLookup.Provider registries) {
        HolderGetter<DamageType> damageTypeLookup = registries.lookupOrThrow(Registries.DAMAGE_TYPE);

        for (ResourceKey<DamageType> damageTypeKey : ModDamageTypes.KEYS) {
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
                                      HolderGetter<DamageType> lookup,
                                      ResourceKey<DamageType> key) {
        provider.accept(key.identifier(), lookup.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Damage Type";
    }
}
