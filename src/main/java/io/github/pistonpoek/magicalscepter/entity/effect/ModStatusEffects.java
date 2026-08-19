package io.github.pistonpoek.magicalscepter.entity.effect;

import io.github.pistonpoek.magicalscepter.mixson.MixsonEvents;
import io.github.pistonpoek.magicalscepter.registry.tag.ModDamageTypeTags;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.advancements.criterion.TagPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.world.effect.MobEffects
 */
public class ModStatusEffects {
    private static final Collection<Identifier> effects = new ArrayList<>();

    /**
     * Status effect that provides (explosion) knockback resistance to the applied entity.
     */
    public static final Holder<MobEffect> STABILITY = registerEffect("stability",
            new ModStatusEffect(MobEffectCategory.BENEFICIAL, 0x074857)
                    .addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE,
                            ModIdentifier.of("effect.stability"),
                            0.3, AttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE,
                            ModIdentifier.of("effect.stability"),
                            0.3, AttributeModifier.Operation.ADD_VALUE));

    /**
     * Status effect that blocks projectiles from hitting the applied entity.
     */
    public static final Holder<MobEffect> REPULSION = registerEffect("repulsion",
            new ModStatusEffect(MobEffectCategory.BENEFICIAL, 0xB2B27F));

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
        if (TagPredicate.is(DamageTypeTags.IS_EXPLOSION).matches(source.typeHolder())
                && TagPredicate.isNot(ModDamageTypeTags.BYPASSES_STABILITY).matches(source.typeHolder())
                && entity.hasEffect(ModStatusEffects.STABILITY)) {
            return false;
        }

        Entity sourceEntity = source.getDirectEntity();
        if (sourceEntity instanceof AbstractArrow
                && TagPredicate.isNot(ModDamageTypeTags.BYPASSES_REPULSION).matches(source.typeHolder())
                && entity.hasEffect(ModStatusEffects.REPULSION)) {
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
    private static Holder<MobEffect> registerEffect(String identifier, MobEffect effect) {
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
    private static Holder<MobEffect> register(String identifier, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, ModIdentifier.of(identifier), effect);
    }
}
