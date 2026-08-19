package io.github.pistonpoek.magicalscepter.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

;

/**
 * Advancement criterion that triggers when a spell is cast by a player.
 */
public class CastSpellCriterion extends SimpleCriterionTrigger<CastSpellCriterion.Conditions> {
    @Override
    public Codec<CastSpellCriterion.Conditions> codec() {
        return CastSpellCriterion.Conditions.CODEC;
    }

    /**
     * Trigger the cast spell criterion.
     *
     * @param player Player that is casting the spell.
     * @param stack  Item stack used when casting the spell.
     */
    public void trigger(ServerPlayer player, ItemStack stack) {
        this.trigger(player, conditions -> conditions.matches(stack));
    }

    /**
     * Conditions that can be used to narrow the cast spell criterion.
     *
     * @param player Optional loot context predicate to check on the casting player.
     * @param item   Optional item predicate to check on the item stack used when casting.
     */
    public record Conditions(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item)
            implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<CastSpellCriterion.Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player")
                                .forGetter(CastSpellCriterion.Conditions::player),
                        ItemPredicate.CODEC.optionalFieldOf("item")
                                .forGetter(CastSpellCriterion.Conditions::item)
                ).apply(instance, CastSpellCriterion.Conditions::new)
        );

        /**
         * Create an advancement criterion with the cast spell conditions for an item predicate.
         *
         * @param item Item predicate to create the condition with.
         * @return Advancement criterion with a cast spell condition.
         */
        public static Criterion<Conditions> create(@Nullable ItemPredicate item) {
            return ModCriteria.CAST_SCEPTER.createCriterion(
                    new CastSpellCriterion.Conditions(Optional.empty(), Optional.ofNullable(item)));
        }

        /**
         * Create an advancement criterion with the cast spell conditions for a specified item.
         *
         * @param item Item convertible to create the condition with.
         * @return Advancement criterion with a cast spell condition.
         */
        public static Criterion<CastSpellCriterion.Conditions> create(ItemLike item) {
            return ModCriteria.CAST_SCEPTER.createCriterion(
                    new CastSpellCriterion.Conditions(Optional.empty(),
                            Optional.of(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, item).build())));
        }

        /**
         * Check if the specified item stack matches the conditions item predicate.
         *
         * @param stack Item stack to test for the conditions item predicate.
         * @return Truth assignment, if the stack fulfills the conditions.
         */
        public boolean matches(ItemStack stack) {
            return this.item.isEmpty() || (this.item.get()).test(stack);
        }
    }
}

