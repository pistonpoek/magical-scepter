package io.github.pistonpoek.magicalscepter.entity.ai.goal;

import io.github.pistonpoek.magicalscepter.entity.mob.SorcererEntity;
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

/**
 * Entity goal to refill scepters to magical scepters,
 * for example used by {@link SorcererEntity}.
 *
 * @param <T> Entity type to use this goal for.
 */
public class ScepterRefillGoal<T extends LivingEntity> extends Goal {
    private final T actor;

    /**
     * Construct a scepter refill goal for a single entity.
     *
     * @param actor Entity to construct the goal for.
     */
    public ScepterRefillGoal(T actor) {
        this.actor = actor;
    }

    @Override
    public boolean canStart() {
        return isHoldingScepter();
    }

    @Override
    public void start() {
        // Get the scepter stack to refill.
        Hand hand = LivingEntityHand.get(this.actor, ScepterHelper.SCEPTER);
        ItemStack scepterStack = this.actor.getStackInHand(hand);

        // Get the scepter entry that should be added to the scepter stack.
        Registry<Scepter> scepterRegistry = this.actor.getRegistryManager().getOrThrow(ModRegistryKeys.SCEPTER);
        RegistryEntry<Scepter> magicalScepter = scepterRegistry.getOrThrow(Scepters.MAGICAL_KEY);

        // Create the new magical scepter stack.
        ItemStack magicalScepterStack = ScepterHelper.createMagicalScepter(scepterStack, magicalScepter);

        // Update the stack of the actor with the new magical scepter stack.
        this.actor.setStackInHand(hand, magicalScepterStack);
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }

    /**
     * Check if the actor is holding a scepter.
     *
     * @return Truth assignment, if actor is holding a scepter.
     */
    protected boolean isHoldingScepter() {
        return this.actor.isHolding(ScepterHelper.SCEPTER);
    }
}
