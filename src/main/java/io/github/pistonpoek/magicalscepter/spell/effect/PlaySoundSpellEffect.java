package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public record PlaySoundSpellEffect(Holder<SoundEvent> soundEvent, FloatProvider volume, FloatProvider pitch)
        implements SpellEffect {
    public static final MapCodec<PlaySoundSpellEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            SoundEvent.CODEC.fieldOf("sound").forGetter(PlaySoundSpellEffect::soundEvent),
                            FloatProviders.codec(1.0E-5F, 10.0F).fieldOf("volume").forGetter(PlaySoundSpellEffect::volume),
                            FloatProviders.codec(1.0E-5F, 2.0F).fieldOf("pitch").forGetter(PlaySoundSpellEffect::pitch)
                    )
                    .apply(instance, PlaySoundSpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        RandomSource random = context.getRandom();
        Entity target = context.target();
        ServerLevel world = context.getWorld();
        Vec3 position = context.position();

        if (!target.isSilent()) {
            world.playSound(null, position.x(), position.y(), position.z(), this.soundEvent,
                    target.getSoundSource(), this.volume.sample(random), this.pitch.sample(random));
        }
    }

    @Override
    public MapCodec<PlaySoundSpellEffect> getCodec() {
        return MAP_CODEC;
    }
}