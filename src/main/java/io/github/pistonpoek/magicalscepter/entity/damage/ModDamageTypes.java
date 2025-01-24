package io.github.pistonpoek.magicalscepter.entity.damage;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.List;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.entity.damage.DamageTypes
 */
public class ModDamageTypes {
    public static final List<RegistryKey<DamageType>> DAMAGE_TYPE_KEYS = List.of();

    public static DamageSource of(DynamicRegistryManager dynamicRegistryManager, RegistryKey<DamageType> key) {
        return new DamageSource(dynamicRegistryManager.getOrThrow(RegistryKeys.DAMAGE_TYPE).getOrThrow(key));
    }

    public static void bootstrap(Registerable<DamageType> registry) {
    }
}
