package net.pistonpoek.magical_scepter.item.scepter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.pistonpoek.magical_scepter.ModDataComponentTypes;

public record ScepterContentsComponent(Optional<RegistryEntry<Scepter>> scepter) {
    public static final ScepterContentsComponent DEFAULT = new ScepterContentsComponent(Optional.empty());
    public static final Codec<ScepterContentsComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Scepter.CODEC.optionalFieldOf("scepter").forGetter(ScepterContentsComponent::scepter)
                    )
                    .apply(instance, ScepterContentsComponent::new)
    );

    //public static final Codec<ScepterContentsComponent> CODEC = Codec.withAlternative(BASE_CODEC, Scepter.CODEC, ScepterContentsComponent::new);
    public static final PacketCodec<RegistryByteBuf, ScepterContentsComponent> PACKET_CODEC = PacketCodec.tuple(
            Scepter.PACKET_CODEC.collect(PacketCodecs::optional),
            ScepterContentsComponent::scepter,
            ScepterContentsComponent::new
    );

    public static ScepterContentsComponent with(RegistryEntry<Scepter> scepter) {
        return new ScepterContentsComponent(Optional.ofNullable(scepter));
    }

//    public static ItemStack createStack(Item item, RegistryEntry<Scepter> scepter) {
//        ItemStack itemStack = new ItemStack(item);
//        itemStack.set(ModDataComponentTypes.SCEPTER_CONTENTS, ScepterContentsComponent.with(scepter));
//        return itemStack;
//    }
}
