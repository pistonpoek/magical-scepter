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
 *
 * @see net.minecraft.entity.damage.DamageTypes
 */
public class ModDamageTypes {
    public static final List<RegistryKey<DamageType>> KEYS = List.of();

    /**
     * Create a damage source for the specified damage type registry key.
     *
     * @param dynamicRegistryManager Registry manager to use for retrieving the damage type.
     * @param key                    Damage type registry key to create a damage source for.
     * @return Damage source created with the specified registry key.
     */
    public static DamageSource of(DynamicRegistryManager dynamicRegistryManager, RegistryKey<DamageType> key) {
        return new DamageSource(dynamicRegistryManager.getOrThrow(RegistryKeys.DAMAGE_TYPE).getOrThrow(key));
    }

    /**
     * Add mod damage types to the specified registry during bootstrap.
     *
     * @param registry Damage type registrable to add damage types to.
     */
    public static void bootstrap(Registerable<DamageType> registry) {
    }
}
