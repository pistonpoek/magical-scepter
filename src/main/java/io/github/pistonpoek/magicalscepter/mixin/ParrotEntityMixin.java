package io.github.pistonpoek.magicalscepter.mixin;

import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import io.github.pistonpoek.magicalscepter.sound.ModSoundEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(ParrotEntity.class)
public class ParrotEntityMixin {
    @Shadow
    @Final
    static Map<EntityType<?>, SoundEvent> MOB_SOUNDS;

    static {
        MOB_SOUNDS.put(ModEntityType.REFRACTOR, ModSoundEvents.ENTITY_PARROT_IMITATE_REFRACTOR);
    }
}