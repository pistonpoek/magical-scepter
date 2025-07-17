package io.github.pistonpoek.magicalscepter.component;

import com.mojang.serialization.Codec;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;

import java.util.function.Consumer;

/**
 * Component that stores data value by the arcane scepter item.
 *
 * @param experience Experience stored currently in the item.
 */
public record ScepterExperienceComponent(int experience) implements TooltipAppender {
    public static final ScepterExperienceComponent DEFAULT = new ScepterExperienceComponent(0);
    public static final Codec<ScepterExperienceComponent> CODEC = Codec.INT.xmap(
            ScepterExperienceComponent::new,
            ScepterExperienceComponent::experience
    );
    public static final PacketCodec<ByteBuf, ScepterExperienceComponent> PACKET_CODEC = PacketCodecs.VAR_INT.xmap(
            ScepterExperienceComponent::new,
            ScepterExperienceComponent::experience
    );

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type) {
        textConsumer.accept(ModIdentifier.translatable("scepter.experience", experience));
    }
}
