package net.pistonpoek.magicalscepter.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.pistonpoek.magicalscepter.scepter.Scepter;
import net.pistonpoek.magicalscepter.spell.Spell;

import java.util.Optional;

public record ScepterContentsComponent(Optional<RegistryEntry<Scepter>> scepter,
                                       Optional<Boolean> infusable,
                                       Optional<Integer> customColor,
                                       Optional<RegistryEntry<Spell>> customAttackSpell,
                                       Optional<RegistryEntry<Spell>> customProtectSpell) {
    public static final ScepterContentsComponent DEFAULT = new ScepterContentsComponent(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    public static final Codec<ScepterContentsComponent> BASE_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Scepter.ENTRY_CODEC.optionalFieldOf("scepter").forGetter(ScepterContentsComponent::scepter),
                            Codec.BOOL.optionalFieldOf("infusable").forGetter(ScepterContentsComponent::infusable),
                            Codec.INT.optionalFieldOf("custom_color").forGetter(ScepterContentsComponent::customColor),
                            Spell.ENTRY_CODEC.optionalFieldOf("custom_attack_spell").forGetter(ScepterContentsComponent::customAttackSpell),
                            Spell.ENTRY_CODEC.optionalFieldOf("custom_protect_spell").forGetter(ScepterContentsComponent::customProtectSpell)
                    )
                    .apply(instance, ScepterContentsComponent::new)
    );

    public static final Codec<ScepterContentsComponent> CODEC = Codec.withAlternative(BASE_CODEC,
            Scepter.ENTRY_CODEC, ScepterContentsComponent::with);
    public static final PacketCodec<RegistryByteBuf, ScepterContentsComponent> PACKET_CODEC = PacketCodec.tuple(
            Scepter.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional),
            ScepterContentsComponent::scepter,
            PacketCodecs.BOOL.collect(PacketCodecs::optional),
            ScepterContentsComponent::infusable,
            PacketCodecs.INTEGER.collect(PacketCodecs::optional),
            ScepterContentsComponent::customColor,
            Spell.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional),
            ScepterContentsComponent::customAttackSpell,
            Spell.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional),
            ScepterContentsComponent::customProtectSpell,
            ScepterContentsComponent::new
    );

    public static ScepterContentsComponent with(RegistryEntry<Scepter> scepter) {
        return new ScepterContentsComponent(Optional.of(scepter), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }
}
