package io.github.pistonpoek.magicalscepter.util;

import io.github.pistonpoek.magicalscepter.mixin.ParrotEntityMixin;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;

public class ParrotEntityUtil {
    /**
     * Add sound for parrots to micmic specified entity type.
     *
     * @param entityType    Entity type to mimic.
     * @param sound         Parrot mimic sound event.
     */
    public static void AddMobSound(EntityType<?> entityType, SoundEvent sound) {
        ParrotEntityMixin.getMobSounds().put(entityType, sound);
    }
}
