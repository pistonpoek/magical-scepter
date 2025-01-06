package io.github.pistonpoek.magicalscepter.sound;


import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.entity.projectile.GuardianBoltEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundEvents;

@Environment(EnvType.CLIENT)
public class GuardianBoltSoundInstance extends MovingSoundInstance {
    private static final float BASE_VOLUME = 0.0F;
    private static final float PROGRESS_VOLUME_MULTIPLIER = 1.0F;
    private static final float BASE_PITCH = 0.7F;
    private static final float PROGRESS_PITCH_MULTIPLIER = 0.5F;
    private final GuardianBoltEntity guardianBolt;

    public GuardianBoltSoundInstance(GuardianBoltEntity guardianBolt) {
        super(SoundEvents.ENTITY_GUARDIAN_ATTACK, guardianBolt.getSoundCategory(), SoundInstance.createRandom());
        this.guardianBolt = guardianBolt;
        this.attenuationType = SoundInstance.AttenuationType.NONE;
        this.repeat = true;
        this.repeatDelay = 0;
    }

    @Override
    public boolean canPlay() {
        return !this.guardianBolt.isSilent();
    }

    @Override
    public void tick() {
        if (!this.guardianBolt.isRemoved()) {
            this.x = (float)this.guardianBolt.getX();
            this.y = (float)this.guardianBolt.getY();
            this.z = (float)this.guardianBolt.getZ();
            float progress = this.guardianBolt.getProgress(0.0F);
            this.volume = BASE_VOLUME + PROGRESS_VOLUME_MULTIPLIER * progress * progress;
            this.pitch = BASE_PITCH + PROGRESS_PITCH_MULTIPLIER * progress;
        } else {
            this.setDone();
        }
    }
}
