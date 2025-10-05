package io.github.pistonpoek.magicalscepter.predicate.item;

import com.mojang.serialization.Codec;
import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import net.minecraft.component.ComponentType;
import net.minecraft.predicate.component.ComponentSubPredicate;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;

import java.util.Optional;

public record ScepterContentsPredicate(
        RegistryEntryList<Scepter> scepters) implements ComponentSubPredicate<ScepterContentsComponent> {
    public static final Codec<ScepterContentsPredicate> CODEC = RegistryCodecs.entryList(ModRegistryKeys.SCEPTER)
            .xmap(ScepterContentsPredicate::new, ScepterContentsPredicate::scepters);

    @Override
    public ComponentType<ScepterContentsComponent> getComponentType() {
        return ModDataComponentTypes.SCEPTER_CONTENTS;
    }

    public boolean test(ScepterContentsComponent scepterContentsComponent) {
        Optional<RegistryEntry<Scepter>> optional = scepterContentsComponent.scepter();
        return optional.isPresent() && this.scepters.contains(optional.get());
    }
}
