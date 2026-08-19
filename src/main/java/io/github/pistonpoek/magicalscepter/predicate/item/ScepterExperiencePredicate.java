package io.github.pistonpoek.magicalscepter.predicate.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.component.ScepterExperienceComponent;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.predicates.SingleComponentItemPredicate;
import net.minecraft.core.component.DataComponentType;

/**
 * Predicate for the scepter experience component.
 *
 * @param experience Integer range to test the scepter experience with.
 */
public record ScepterExperiencePredicate(
        MinMaxBounds.Ints experience) implements SingleComponentItemPredicate<ScepterExperienceComponent> {
    public static final Codec<ScepterExperiencePredicate> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    MinMaxBounds.Ints.CODEC.optionalFieldOf("experience",
                            MinMaxBounds.Ints.ANY).forGetter(ScepterExperiencePredicate::experience)
            ).apply(instance, ScepterExperiencePredicate::new)
    );

    @Override
    public DataComponentType<ScepterExperienceComponent> componentType() {
        return ModDataComponentTypes.SCEPTER_EXPERIENCE;
    }

    @Override
    public boolean matches(ScepterExperienceComponent scepterExperienceComponent) {
        return this.experience.matches(scepterExperienceComponent.experience());
    }

}
