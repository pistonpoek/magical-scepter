package io.github.pistonpoek.magicalscepter.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.ScepterPredicate;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Advancement criterion that triggers when a player infuses a scepter.
 */
public class InfuseScepterCriterion extends SimpleCriterionTrigger<InfuseScepterCriterion.Conditions> {
    @Override
    public Codec<InfuseScepterCriterion.Conditions> codec() {
        return InfuseScepterCriterion.Conditions.CODEC;
    }

    /**
     * Trigger the infuse scepter criterion.
     *
     * @param player  Player infusing the scepter.
     * @param scepter Scepter that was infused.
     */
    public void trigger(ServerPlayer player, Holder<Scepter> scepter) {
        this.trigger(player, conditions -> conditions.matches(scepter));
    }

    /**
     * Conditions that can be used to narrow the infuse scepter criterion.
     *
     * @param player  Optional loot context predicate to check on the casting player.
     * @param scepter Optional scepter predicate to check on the scepter used when casting.
     */
    public record Conditions(Optional<ContextAwarePredicate> player,
                             Optional<ScepterPredicate> scepter) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<InfuseScepterCriterion.Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player")
                                .forGetter(InfuseScepterCriterion.Conditions::player),
                        ScepterPredicate.CODEC.optionalFieldOf("scepter")
                                .forGetter(InfuseScepterCriterion.Conditions::scepter)
                ).apply(instance, InfuseScepterCriterion.Conditions::new)
        );

        /**
         * Create an advancement criterion with the infuse scepter conditions for a scepter predicate.
         *
         * @param scepter Scepter predicate to create the condition with.
         * @return Advancement criterion with an infuse scepter condition.
         */
        public static Criterion<Conditions> create(@Nullable ScepterPredicate scepter) {
            return ModCriteria.INFUSE_SCEPTER.createCriterion(
                    new InfuseScepterCriterion.Conditions(Optional.empty(), Optional.ofNullable(scepter)));
        }

        /**
         * Create an advancement criterion with the infuse scepter conditions for a specified scepter.
         *
         * @param scepter Scepter registry entry to create the condition with.
         * @return Advancement criterion with an infuse scepter condition.
         */
        public static Criterion<InfuseScepterCriterion.Conditions> create(Holder<Scepter> scepter) {
            return ModCriteria.INFUSE_SCEPTER.createCriterion(
                    new InfuseScepterCriterion.Conditions(Optional.empty(),
                            Optional.of(ScepterPredicate.of(scepter))));
        }

        /**
         * Check if the specified scepter matches the conditions scepter predicate.
         *
         * @param scepter Scepter to test for the conditions scepter predicate.
         * @return Truth assignment, if the scepter fulfills the conditions.
         */
        public boolean matches(Holder<Scepter> scepter) {
            return this.scepter.isEmpty() || (this.scepter.get()).test(scepter);
        }
    }
}

