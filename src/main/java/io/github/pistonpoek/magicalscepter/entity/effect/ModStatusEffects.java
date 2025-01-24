package io.github.pistonpoek.magicalscepter.entity.effect;

import io.github.pistonpoek.magicalscepter.mixson.MixsonEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.entity.effect.StatusEffects
 */
public class ModStatusEffects {
    public static final RegistryEntry<StatusEffect> STABILITY = registerEffect("stability",
            new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 0x074857)
                    .addAttributeModifier(EntityAttributes.KNOCKBACK_RESISTANCE,
                        ModIdentifier.of("effect.stability"),
                        0.3f, EntityAttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(EntityAttributes.EXPLOSION_KNOCKBACK_RESISTANCE,
                        ModIdentifier.of("effect.stability"),
                        0.3f, EntityAttributeModifier.Operation.ADD_VALUE));
    public static final RegistryEntry<StatusEffect> REPULSION = registerEffect("repulsion",
            new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 0xB2B27F));

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {

    }

    public static boolean allowDamage(LivingEntity entity, DamageSource source, float amount) {
        Entity sourceEntity = source.getSource();
        return !(sourceEntity instanceof PersistentProjectileEntity
                && entity.hasStatusEffect(ModStatusEffects.REPULSION));
    }

    private static RegistryEntry<StatusEffect> registerEffect(String id, StatusEffect effect) {
        RegistryEntry<StatusEffect> statusEffect = register(id, effect);
        MixsonEvents.registerEffectModification(ModIdentifier.of(id));
        return statusEffect;
    }

    private static RegistryEntry<StatusEffect> register(String id, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, ModIdentifier.of(id), statusEffect);
    }
}
