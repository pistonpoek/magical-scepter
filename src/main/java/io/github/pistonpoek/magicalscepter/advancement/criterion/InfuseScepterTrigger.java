package io.github.pistonpoek.magicalscepter.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.ScepterPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Advancement criterion that triggers when a player infuses a scepter.
 */
public class InfuseScepterTrigger extends SimpleCriterionTrigger<InfuseScepterTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    /**
     * Trigger the infuse scepter trigger.
     *
     * @param player  Player infusing the scepter.
     * @param scepter Scepter that was infused.
     */
    public void trigger(ServerPlayer player, Holder<Scepter> scepter) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(scepter));
    }

    /**
     * Trigger instance that can be used to narrow the infuse scepter trigger.
     *
     * @param player  Optional loot context predicate to check on the casting player.
     * @param scepter Optional scepter predicate to check on the scepter used when casting.
     */
    public record TriggerInstance(Optional<Holder<LootItemCondition>> player,
                                  Optional<ScepterPredicate> scepter) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        LootItemCondition.CODEC.optionalFieldOf("player")
                                .forGetter(TriggerInstance::player),
                        ScepterPredicate.CODEC.optionalFieldOf("scepter")
                                .forGetter(TriggerInstance::scepter)
                ).apply(instance, TriggerInstance::new)
        );

        /**
         * Create an advancement trigger with the infuse scepter trigger instance for a scepter predicate.
         *
         * @param scepter Scepter predicate to create the trigger with.
         * @return Advancement trigger with an infuse scepter trigger.
         */
        public static Criterion<TriggerInstance> create(@Nullable ScepterPredicate scepter) {
            return ModCriteria.INFUSE_SCEPTER.createCriterion(
                    new TriggerInstance(Optional.empty(), Optional.ofNullable(scepter)));
        }

        /**
         * Create an advancement trigger with the infuse scepter trigger instance for a specified scepter.
         *
         * @param scepter Scepter registry entry to create the trigger with.
         * @return Advancement trigger with an infuse scepter trigger.
         */
        public static Criterion<TriggerInstance> create(Holder<Scepter> scepter) {
            return ModCriteria.INFUSE_SCEPTER.createCriterion(
                    new TriggerInstance(Optional.empty(),
                            Optional.of(ScepterPredicate.of(scepter))));
        }

        /**
         * Check if the specified scepter matches the trigger instance scepter predicate.
         *
         * @param scepter Scepter to test for the trigger instance scepter predicate.
         * @return Truth assignment, if the scepter fulfills the trigger instance.
         */
        public boolean matches(Holder<Scepter> scepter) {
            return this.scepter.isEmpty() || (this.scepter.get()).test(scepter);
        }
    }
}

