package net.pistonpoek.magicalscepter.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.sound.SoundEvent;
import net.pistonpoek.magicalscepter.MagicalScepter;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.scepter.Scepter;

import java.util.Optional;

public record Spell(int experienceCost, int cooldown,
                    Optional<RegistryEntry<SoundEvent>> sound) {
    public static final Codec<Spell> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.INT.fieldOf("experience_cost").forGetter(Spell::experienceCost),
                            Codec.INT.fieldOf("cooldown").forGetter(Spell::cooldown),
                            SoundEvent.ENTRY_CODEC.optionalFieldOf("sound").forGetter(Spell::sound)
                    )
                    .apply(instance, Spell::new)
    );
    public static final Codec<RegistryEntry<Spell>> ENTRY_CODEC = RegistryFixedCodec.of(ModRegistryKeys.SPELL);
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<Spell>> ENTRY_PACKET_CODEC =
            PacketCodecs.registryEntry(ModRegistryKeys.SPELL);
    public static final PacketCodec<RegistryByteBuf, Spell> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

    public boolean isInstant() {
        return true;
    }

    public int getCastDuration() {
        return 0;
    }

    public void castSpell(LivingEntity caster) {
        MagicalScepter.LOGGER.info("Casting spell");
    }

    public void displaySpell(LivingEntity caster, int remainingCastTicks) {
        MagicalScepter.LOGGER.info("Displaying spell");
    }

    public void updateSpell(LivingEntity caster, int remainingCastTicks) {

    }

    public void endSpell(LivingEntity caster, int remainingCastTicks) {
        MagicalScepter.LOGGER.info("End spell");
    }
}
