package io.github.pistonpoek.magicalscepter.advancement.criterion;

import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.advancements.triggers.CriteriaTriggers
 */
public class ModCriteria {
    public static final CastSpellCriterion CAST_SCEPTER = register("cast_scepter", new CastSpellCriterion());
    public static final InfuseScepterCriterion INFUSE_SCEPTER = register("infuse_scepter", new InfuseScepterCriterion());

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {

    }

    /**
     * Register a mod advancement criteria for a specified identifier.
     *
     * @param identifier String identifier to register criterion for.
     * @param criterion  Criterion to register.
     * @param <T>        Type of the criterion to register.
     * @return Registered criterion.
     */
    public static <T extends SimpleCriterionTrigger<?>> T register(String identifier, T criterion) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, identifier, criterion);
    }
}
