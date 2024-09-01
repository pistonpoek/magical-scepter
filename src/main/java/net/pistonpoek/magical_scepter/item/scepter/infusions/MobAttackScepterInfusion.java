package net.pistonpoek.magical_scepter.item.scepter.infusions;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;

import java.util.Collection;

public class MobAttackScepterInfusion extends WrittenScepter {
    private final EntityType<?> attackEntityType;
    private final Collection<EntityType<?>> sourceEntityType;

    public MobAttackScepterInfusion(EntityType<?> attackEntityType, Collection<EntityType<?>> sourceEntityType) {
        this.attackEntityType = attackEntityType;
        this.sourceEntityType = sourceEntityType;
    }

    @Override
    public boolean test(DamageSource damageSource) {
        if (damageSource.getAttacker() == null || damageSource.getSource() == null) {
            return false;
        }

        EntityType<?> attackType = damageSource.getAttacker().getType();
        EntityType<?> sourceType = damageSource.getSource().getType();
        return attackEntityType == attackType && sourceEntityType.contains(sourceType);

    }
}
