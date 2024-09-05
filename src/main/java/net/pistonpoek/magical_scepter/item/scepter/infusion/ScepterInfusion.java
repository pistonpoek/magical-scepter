package net.pistonpoek.magical_scepter.item.scepter.infusion;

import net.minecraft.entity.damage.DamageSource;

import java.util.function.Predicate;

public interface ScepterInfusion extends Predicate<DamageSource> {
    boolean isInfusable();
}
