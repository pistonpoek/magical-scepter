package net.pistonpoek.magical_scepter.item.scepter.infusion;

import net.minecraft.entity.damage.DamageSource;

public class InfusableScepter implements ScepterInfusion {
    @Override
    public boolean isInfusable() {
        return true;
    }

    @Override
    public boolean test(DamageSource damageSource) {
        return false;
    }
}
