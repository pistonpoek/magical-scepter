package io.github.pistonpoek.magicalscepter.mixin;

import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import io.github.pistonpoek.magicalscepter.sound.ModSoundEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ParrotEntity.class)
public class ParrotEntityMixin {
    @Shadow
    @Final
    static Map<EntityType<?>, SoundEvent> MOB_SOUNDS;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void addMobSounds(CallbackInfo ci) {
        MOB_SOUNDS.put(ModEntityType.SORCERER, ModSoundEvents.ENTITY_PARROT_IMITATE_SORCERER);
    }
}