package io.github.pistonpoek.magicalscepter.entity.effect;

import io.github.pistonpoek.magicalscepter.mixson.MixsonEvents;
import io.github.pistonpoek.magicalscepter.registry.tag.ModDamageTypeTags;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.predicate.TagPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.entity.effect.StatusEffects
 */
public class ModStatusEffects {
    private static final Collection<Identifier> effects = new ArrayList<>();

    /**
     * Status effect that provides (explosion) knockback resistance to the applied entity.
     */
    public static final RegistryEntry<StatusEffect> STABILITY = registerEffect("stability",
            new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 0x074857)
                    .addAttributeModifier(EntityAttributes.KNOCKBACK_RESISTANCE,
                            ModIdentifier.of("effect.stability"),
                            0.3, EntityAttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(EntityAttributes.EXPLOSION_KNOCKBACK_RESISTANCE,
                            ModIdentifier.of("effect.stability"),
                            0.3, EntityAttributeModifier.Operation.ADD_VALUE));

    /**
     * Status effect that blocks projectiles from hitting the applied entity.
     */
    public static final RegistryEntry<StatusEffect> REPULSION = registerEffect("repulsion",
            new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 0xB2B27F));

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {
        for (Identifier effect : effects) {
            MixsonEvents.registerEffectModification(effect);
        }
    }

    /**
     * Determine if the specified entity should take the specified amount of damage for the specified damage source.
     *
     * @param entity Living entity that is about to take damage.
     * @param source Damage source that will damage the entity.
     * @param amount Amount of damage the entity is about to take.
     * @return Truth assignment, if damage should be taken.
     * @see net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AllowDamage
     */
    public static boolean allowDamage(LivingEntity entity, DamageSource source, float amount) {
        if (TagPredicate.expected(DamageTypeTags.IS_EXPLOSION).test(source.getTypeRegistryEntry())
                && TagPredicate.unexpected(ModDamageTypeTags.BYPASSES_STABILITY).test(source.getTypeRegistryEntry())
                && entity.hasStatusEffect(ModStatusEffects.STABILITY)) {
            return false;
        }

        Entity sourceEntity = source.getSource();
        if (sourceEntity instanceof PersistentProjectileEntity
                && TagPredicate.unexpected(ModDamageTypeTags.BYPASSES_REPULSION).test(source.getTypeRegistryEntry())
                && entity.hasStatusEffect(ModStatusEffects.REPULSION)) {
            return false;
        }

        return true;
    }

    /**
     * Register an effect for the specified identifier and add it to the all effects advancement.
     *
     * @param identifier String to create mod identifier with.
     * @param effect     Status effect to register.
     * @return Registered registry entry of the status effect.
     */
    private static RegistryEntry<StatusEffect> registerEffect(String identifier, StatusEffect effect) {
        effects.add(ModIdentifier.of(identifier));
        return register(identifier, effect);
    }

    /**
     * Register a status effect for the specified identifier.
     *
     * @param identifier String to create mod identifier with for the status effect.
     * @param effect     Status effect to register.
     * @return Registered registry entry of the status effect.
     */
    private static RegistryEntry<StatusEffect> register(String identifier, StatusEffect effect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, ModIdentifier.of(identifier), effect);
    }
}
