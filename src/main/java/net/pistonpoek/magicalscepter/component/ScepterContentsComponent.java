package net.pistonpoek.magicalscepter.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.pistonpoek.magicalscepter.scepter.Scepter;

import java.util.Optional;

public record ScepterContentsComponent(Optional<RegistryEntry<Scepter>> scepter) {
    public static final ScepterContentsComponent DEFAULT = new ScepterContentsComponent(Optional.empty());
    public static final Codec<ScepterContentsComponent> BASE_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Scepter.ENTRY_CODEC.optionalFieldOf("scepter").forGetter(ScepterContentsComponent::scepter)
                    )
                    .apply(instance, ScepterContentsComponent::new)
    );

    public static final Codec<ScepterContentsComponent> CODEC = Codec.withAlternative(BASE_CODEC,
            Scepter.ENTRY_CODEC, map -> new ScepterContentsComponent(Optional.of(map)));
    public static final PacketCodec<RegistryByteBuf, ScepterContentsComponent> PACKET_CODEC = PacketCodec.tuple(
            Scepter.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional),
            ScepterContentsComponent::scepter,
            ScepterContentsComponent::new
    );

    public static ScepterContentsComponent with(RegistryEntry<Scepter> scepter) {
        return new ScepterContentsComponent(Optional.ofNullable(scepter));
    }

    public static ItemStack createStack(Item item, RegistryEntry<Scepter> scepter) {
        ItemStack itemStack = new ItemStack(item);
        itemStack.set(ModDataComponentTypes.SCEPTER_CONTENTS, ScepterContentsComponent.with(scepter));
        return itemStack;
    }
}
