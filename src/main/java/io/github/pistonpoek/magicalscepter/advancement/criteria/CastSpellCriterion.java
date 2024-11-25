package io.github.pistonpoek.magicalscepter.advancement.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

public class CastSpellCriterion extends AbstractCriterion<CastSpellCriterion.Conditions> {
    @Override
    public Codec<CastSpellCriterion.Conditions> getConditionsCodec() {
        return CastSpellCriterion.Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, ItemStack stack) {
        this.trigger(player, conditions -> conditions.matches(stack));
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> item) implements AbstractCriterion.Conditions {
        public static final Codec<CastSpellCriterion.Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(CastSpellCriterion.Conditions::player),
                                ItemPredicate.CODEC.optionalFieldOf("item").forGetter(CastSpellCriterion.Conditions::item)
                        )
                        .apply(instance, CastSpellCriterion.Conditions::new)
        );

        public static AdvancementCriterion<CastSpellCriterion.Conditions> create(Optional<ItemPredicate> item) {
            return ModCriteria.CAST_SCEPTER.create(new CastSpellCriterion.Conditions(Optional.empty(), item));
        }

        public static AdvancementCriterion<CastSpellCriterion.Conditions> create(ItemConvertible item) {
            return ModCriteria.CAST_SCEPTER
                    .create(new CastSpellCriterion.Conditions(Optional.empty(), Optional.of(ItemPredicate.Builder.create().items(item).build())));
        }

        public boolean matches(ItemStack stack) {
            return this.item.isEmpty() || (this.item.get()).test(stack);
        }
    }
}

