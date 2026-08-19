package io.github.pistonpoek.magicalscepter.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.parrot.Parrot;

@Mixin(Parrot.class)
public interface ParrotEntityMixin {

    @Accessor("MOB_SOUND_MAP")
    static Map<EntityType<?>, SoundEvent> getMobSounds() {
        throw new AssertionError("Mixin failed to inject accessor!");
    }
}