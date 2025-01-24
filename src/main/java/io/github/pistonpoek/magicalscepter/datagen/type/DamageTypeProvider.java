package io.github.pistonpoek.magicalscepter.datagen.type;

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

public class DamageTypeProvider extends FabricCodecDataProvider<DamageType> {
    public DamageTypeProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture, DataOutput.OutputType.DATA_PACK,
                ModRegistryKeys.directory(RegistryKeys.DAMAGE_TYPE), DamageType.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, DamageType> provider, RegistryWrapper.WrapperLookup registries) {
        RegistryEntryLookup<DamageType> damageTypeLookup = registries.getOrThrow(RegistryKeys.DAMAGE_TYPE);

        for (RegistryKey<DamageType> damageTypeKey: ModDamageTypes.DAMAGE_TYPE_KEYS) {
            addDamageType(provider, damageTypeLookup, damageTypeKey);
        }
    }

    private static void addDamageType(BiConsumer<Identifier, DamageType> provider,
                                   RegistryEntryLookup<DamageType> lookup,
                                   RegistryKey<DamageType> key) {
        provider.accept(key.getValue(), lookup.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "ModDamageTypeProvider";
    }
}
