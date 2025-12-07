package io.github.pistonpoek.magicalscepter.predicate.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.component.ScepterExperienceComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.component.ComponentSubPredicate;

/**
 * Predicate for the scepter experience component.
 *
 * @param experience Integer range to test the scepter experience with.
 */
public record ScepterExperiencePredicate(
        NumberRange.IntRange experience) implements ComponentSubPredicate<ScepterExperienceComponent> {
    public static final Codec<ScepterExperiencePredicate> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    NumberRange.IntRange.CODEC.optionalFieldOf("experience",
                            NumberRange.IntRange.ANY).forGetter(ScepterExperiencePredicate::experience)
            ).apply(instance, ScepterExperiencePredicate::new)
    );

    @Override
    public ComponentType<ScepterExperienceComponent> getComponentType() {
        return ModDataComponentTypes.SCEPTER_EXPERIENCE;
    }

    @Override
    public boolean test(ScepterExperienceComponent scepterExperienceComponent) {
        return this.experience.test(scepterExperienceComponent.experience());
    }

}
