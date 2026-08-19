package io.github.pistonpoek.magicalscepter.registry.tag;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.tags.DamageTypeTags
 */
public class ModDamageTypeTags {
    public static final TagKey<DamageType> BYPASSES_REPULSION = of("bypasses_repulsion");
    public static final TagKey<DamageType> BYPASSES_STABILITY = of("bypasses_stability");

    public static final TagKey<DamageType> BLAZE_INFUSION = of("infuses_blaze_scepter");
    public static final TagKey<DamageType> BREEZE_INFUSION = of("infuses_breeze_scepter");
    public static final TagKey<DamageType> DRAGON_INFUSION = of("infuses_dragon_scepter");
    public static final TagKey<DamageType> EVOKER_INFUSION = of("infuses_evoker_scepter");
    public static final TagKey<DamageType> GHAST_INFUSION = of("infuses_ghast_scepter");
    public static final TagKey<DamageType> GUARDIAN_INFUSION = of("infuses_guardian_scepter");
    public static final TagKey<DamageType> SHULKER_INFUSION = of("infuses_shulker_scepter");
    public static final TagKey<DamageType> WARDEN_INFUSION = of("infuses_warden_scepter");
    public static final TagKey<DamageType> WITHER_INFUSION = of("infuses_wither_scepter");

    /**
     * Get the damage type tag key for the specified name.
     *
     * @param name String name to get damage type tag for.
     * @return Biome tag key for the specified name.
     */
    private static TagKey<DamageType> of(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, ModIdentifier.of(name));
    }
}