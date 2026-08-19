package io.github.pistonpoek.magicalscepter.sound;

import io.github.pistonpoek.magicalscepter.entity.spell.SpellGuardianBeamEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvents;

@Environment(EnvType.CLIENT)
public class SpellGuardianBeamSoundInstance extends AbstractTickableSoundInstance {
    private static final float BASE_VOLUME = 0.0F;
    private static final float PROGRESS_VOLUME_MULTIPLIER = 1.0F;
    private static final float BASE_PITCH = 0.7F;
    private static final float PROGRESS_PITCH_MULTIPLIER = 0.5F;
    private final SpellGuardianBeamEntity entity;

    /**
     * Constructs a spell guardian beam sound instance.
     *
     * @param entity Spell guardian beam entity to create sound instance for.
     */
    public SpellGuardianBeamSoundInstance(SpellGuardianBeamEntity entity) {
        super(SoundEvents.GUARDIAN_ATTACK, entity.getSoundSource(), SoundInstance.createUnseededRandom());
        this.entity = entity;
        this.attenuation = Attenuation.NONE;
        this.looping = true;
        this.delay = 0;
    }

    @Override
    public boolean isStopped() {
        return !this.entity.isSilent();
    }

    @Override
    public void tick() {
        if (!this.entity.isRemoved()) {
            this.x = (float) this.entity.getX();
            this.y = (float) this.entity.getY();
            this.z = (float) this.entity.getZ();
            float progress = this.entity.getProgress(0.0F);
            this.volume = BASE_VOLUME + PROGRESS_VOLUME_MULTIPLIER * progress * progress;
            this.pitch = BASE_PITCH + PROGRESS_PITCH_MULTIPLIER * progress;
        } else {
            this.stop();
        }
    }
}
