package io.github.pistonpoek.magicalscepter.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Advancement criterion that triggers when a spell is cast by a player.
 */
public class CastSpellTrigger extends SimpleCriterionTrigger<CastSpellTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    /**
     * Trigger the cast spell criterion.
     *
     * @param player Player that is casting the spell.
     * @param stack  Item stack used when casting the spell.
     */
    public void trigger(ServerPlayer player, ItemStack stack) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(stack));
    }

    /**
     * Trigger instance that can be used to narrow the cast spell trigger.
     *
     * @param player Optional loot context predicate to check on the casting player.
     * @param item   Optional item predicate to check on the item stack used when casting.
     */
    public record TriggerInstance(Optional<Holder<LootItemCondition>> player, Optional<ItemPredicate> item)
            implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        LootItemCondition.CODEC.optionalFieldOf("player")
                                .forGetter(TriggerInstance::player),
                        ItemPredicate.CODEC.optionalFieldOf("item")
                                .forGetter(TriggerInstance::item)
                ).apply(instance, TriggerInstance::new)
        );

        /**
         * Create an advancement criterion with the cast spell trigger instance for an item predicate.
         *
         * @param item Item predicate to create the trigger with.
         * @return Advancement criterion with a cast spell trigger.
         */
        public static Criterion<TriggerInstance> create(@Nullable ItemPredicate item) {
            return ModCriteria.CAST_SCEPTER.createCriterion(
                    new TriggerInstance(Optional.empty(), Optional.ofNullable(item)));
        }

        /**
         * Create an advancement trigger with the cast spell trigger instance for a specified item.
         *
         * @param item Item convertible to create the trigger with.
         * @return Advancement criterion with a cast spell trigger.
         */
        public static Criterion<TriggerInstance> create(ItemLike item) {
            return ModCriteria.CAST_SCEPTER.createCriterion(
                    new TriggerInstance(Optional.empty(),
                            Optional.of(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, item).build())));
        }

        /**
         * Check if the specified item stack matches the trigger instance item predicate.
         *
         * @param stack Item stack to test for the trigger instance item predicate.
         * @return Truth assignment, if the stack fulfills the trigger instance.
         */
        public boolean matches(ItemStack stack) {
            return this.item.isEmpty() || (this.item.get()).test(stack);
        }
    }
}

