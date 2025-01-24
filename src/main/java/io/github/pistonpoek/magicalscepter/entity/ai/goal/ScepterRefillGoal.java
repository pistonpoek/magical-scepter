package io.github.pistonpoek.magicalscepter.entity.ai.goal;

import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import io.github.pistonpoek.magicalscepter.util.LivingEntityHand;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;

public class ScepterRefillGoal<T extends LivingEntity> extends Goal {
    private final T actor;

    public ScepterRefillGoal(T actor) {
        this.actor = actor;
    }

    @Override
    public boolean canStart() {
        return isHoldingScepter();
    }

    @Override
    public void start() {
        Hand hand = LivingEntityHand.get(this.actor, ScepterHelper.IS_SCEPTER);
        ItemStack scepterStack = this.actor.getStackInHand(hand);
        Registry<Scepter> scepterRegistry = this.actor.getRegistryManager().getOrThrow(ModRegistryKeys.SCEPTER);
        RegistryEntry<Scepter> magicalScepter = scepterRegistry.getOrThrow(Scepters.MAGICAL_KEY);
        ItemStack magicalScepterStack = ScepterHelper.createMagicalScepter(scepterStack, magicalScepter);
        this.actor.setStackInHand(hand, magicalScepterStack);
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }

    protected boolean isHoldingScepter() {
        return this.actor.isHolding(ScepterHelper.IS_SCEPTER);
    }
}
