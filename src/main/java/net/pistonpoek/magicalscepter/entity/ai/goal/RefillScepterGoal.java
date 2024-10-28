package net.pistonpoek.magicalscepter.entity.ai.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

public class RefillScepterGoal<T extends LivingEntity> extends Goal {
    private final T actor;

    public RefillScepterGoal(T actor) {
        this.actor = actor;
    }

    @Override
    public boolean canStart() {
        return false;
    }
}
