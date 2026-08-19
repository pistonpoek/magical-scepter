package io.github.pistonpoek.magicalscepter.predicate.item;

import com.mojang.serialization.Codec;
import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import net.minecraft.advancements.predicates.SingleComponentItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentType;

import java.util.Optional;

/**
 * Predicate for the scepter contents component.
 *
 * @param scepters List of scepter registry entries to test scepter contents with.
 */
public record ScepterContentsPredicate(
        HolderSet<Scepter> scepters) implements SingleComponentItemPredicate<ScepterContentsComponent> {
    public static final Codec<ScepterContentsPredicate> CODEC = RegistryCodecs.homogeneousList(ModRegistryKeys.SCEPTER)
            .xmap(ScepterContentsPredicate::new, ScepterContentsPredicate::scepters);

    @Override
    public DataComponentType<ScepterContentsComponent> componentType() {
        return ModDataComponentTypes.SCEPTER_CONTENTS;
    }

    @Override
    public boolean matches(ScepterContentsComponent scepterContentsComponent) {
        Optional<Holder<Scepter>> optional = scepterContentsComponent.scepter();
        return optional.isPresent() && this.scepters.contains(optional.get());
    }
}
