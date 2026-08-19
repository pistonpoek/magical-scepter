package io.github.pistonpoek.magicalscepter.entity.damage;

import java.util.List;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.world.damagesource.DamageTypes
 */
public class ModDamageTypes {
    public static final List<ResourceKey<DamageType>> KEYS = List.of();

    /**
     * Create a damage source for the specified damage type registry key.
     *
     * @param dynamicRegistryManager Registry manager to use for retrieving the damage type.
     * @param key                    Damage type registry key to create a damage source for.
     * @return Damage source created with the specified registry key.
     */
    public static DamageSource of(RegistryAccess dynamicRegistryManager, ResourceKey<DamageType> key) {
        return new DamageSource(dynamicRegistryManager.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key));
    }

    /**
     * Add mod damage types to the specified registry during bootstrap.
     *
     * @param registry Damage type registrable to add damage types to.
     */
    public static void bootstrap(BootstrapContext<DamageType> registry) {
    }
}
