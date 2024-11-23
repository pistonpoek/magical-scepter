package io.github.pistonpoek.magicalscepter.advancement.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.ScepterPredicate;

public class InfuseScepterCriterion extends AbstractCriterion<InfuseScepterCriterion.Conditions> {
    @Override
    public Codec<InfuseScepterCriterion.Conditions> getConditionsCodec() {
        return InfuseScepterCriterion.Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, RegistryEntry<Scepter> scepter) {
        this.trigger(player, conditions -> conditions.matches(scepter));
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<ScepterPredicate> scepter) implements AbstractCriterion.Conditions {
        public static final Codec<InfuseScepterCriterion.Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(InfuseScepterCriterion.Conditions::player),
                                ScepterPredicate.CODEC.optionalFieldOf("scepter").forGetter(InfuseScepterCriterion.Conditions::scepter)
                        )
                        .apply(instance, InfuseScepterCriterion.Conditions::new)
        );

        public static AdvancementCriterion<InfuseScepterCriterion.Conditions> create(Optional<ScepterPredicate> scepter) {
            return ModCriteria.INFUSE_SCEPTER.create(new InfuseScepterCriterion.Conditions(Optional.empty(), scepter));
        }

        public static AdvancementCriterion<InfuseScepterCriterion.Conditions> create(RegistryEntry<Scepter> scepter) {
            return ModCriteria.INFUSE_SCEPTER
                    .create(new InfuseScepterCriterion.Conditions(Optional.empty(), Optional.of(ScepterPredicate.of(scepter))));
        }

        public boolean matches(RegistryEntry<Scepter> scepter) {
            return this.scepter.isEmpty() || (this.scepter.get()).test(scepter);
        }
    }
}

