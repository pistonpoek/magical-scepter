package net.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.random.Random;

public record PlaySoundSpellEffect(RegistryEntry<SoundEvent> soundEvent, FloatProvider volume, FloatProvider pitch)
        implements SpellEffect {
    public static final MapCodec<PlaySoundSpellEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            SoundEvent.ENTRY_CODEC.fieldOf("sound").forGetter(PlaySoundSpellEffect::soundEvent),
                            FloatProvider.createValidatedCodec(1.0E-5F, 10.0F).fieldOf("volume").forGetter(PlaySoundSpellEffect::volume),
                            FloatProvider.createValidatedCodec(1.0E-5F, 2.0F).fieldOf("pitch").forGetter(PlaySoundSpellEffect::pitch)
                    )
                    .apply(instance, PlaySoundSpellEffect::new)
    );

    @Override
    public void apply(ServerWorld world, Entity entity, Vec3d position, Vec3d rotation) {
        Random random = entity.getRandom();
        if (!entity.isSilent()) {
            world.playSound(null, position.getX(), position.getY(), position.getZ(), this.soundEvent,
                    entity.getSoundCategory(), this.volume.get(random), this.pitch.get(random));
        }
    }

    @Override
    public MapCodec<PlaySoundSpellEffect> getCodec() {
        return CODEC;
    }
}