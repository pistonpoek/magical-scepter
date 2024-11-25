package io.github.pistonpoek.magicalscepter.advancement.criteria;

import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModCriteria {
    public static final CastSpellCriterion CAST_SCEPTER = register("cast_scepter", new CastSpellCriterion());
    public static final InfuseScepterCriterion INFUSE_SCEPTER = register("infuse_scepter", new InfuseScepterCriterion());

    public static void init() {

    }

    public static <T extends Criterion<?>> T register(String id, T criterion) {
        return Registry.register(Registries.CRITERION, id, criterion);
    }
}
