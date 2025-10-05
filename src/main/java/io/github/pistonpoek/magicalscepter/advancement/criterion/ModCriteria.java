package io.github.pistonpoek.magicalscepter.advancement.criterion;

import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.advancement.criterion.Criteria
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
    public static <T extends Criterion<?>> T register(String identifier, T criterion) {
        return Registry.register(Registries.CRITERION, identifier, criterion);
    }
}
