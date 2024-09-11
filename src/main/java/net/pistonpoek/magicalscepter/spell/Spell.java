package net.pistonpoek.magicalscepter.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.pistonpoek.magicalscepter.MagicalScepter;
import net.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.scepter.Scepter;

import java.util.Optional;

public record Spell(int experienceCost, int cooldown,
                    Optional<RegistryEntry<SoundEvent>> sound) {
    public static final Codec<Spell> BASE_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.INT.fieldOf("experience_cost").forGetter(Spell::experienceCost),
                            Codec.INT.fieldOf("cooldown").forGetter(Spell::cooldown),
                            SoundEvent.ENTRY_CODEC.optionalFieldOf("sound").forGetter(Spell::sound)
                    )
                    .apply(instance, Spell::new)
    );
    public static final Codec<RegistryEntry<Spell>> ENTRY_CODEC = RegistryFixedCodec.of(ModRegistryKeys.SPELL);
    public static final Codec<Spell> CODEC = Codec.withAlternative(BASE_CODEC, ENTRY_CODEC, RegistryEntry::value);
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<Spell>> ENTRY_PACKET_CODEC =
            PacketCodecs.registryEntry(ModRegistryKeys.SPELL);
    public static final PacketCodec<RegistryByteBuf, Spell> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

    public boolean isInstant() {
        return true;
    }

    public int getCastDuration() {
        return 0;
    }

    @Environment(EnvType.SERVER)
    public void castSpell(LivingEntity caster) {
        MagicalScepter.LOGGER.info("Casting spell");
        //caster.getWorld().syncWorldEvent(null, WorldEvents.BLAZE_SHOOTS, caster.getBlockPos(), 0);
    }

    @Environment(EnvType.CLIENT)
    public void displaySpell(World world, LivingEntity caster, int remainingCastTicks) {
        MagicalScepter.LOGGER.info("Displaying spell" + (sound.map(soundEventRegistryEntry -> " with sound " + soundEventRegistryEntry.value().getId().toShortTranslationKey()).orElse("")));
        Random random = world.random;
        sound.ifPresent(sound -> caster.playSound(sound.value(), 3.0F,
                (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F)
        );
    }

    public void updateSpell(LivingEntity caster, int remainingCastTicks) {

    }

    public void endSpell(LivingEntity caster, int remainingCastTicks) {
        MagicalScepter.LOGGER.info("End spell");
    }
}
