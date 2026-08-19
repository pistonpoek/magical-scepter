package io.github.pistonpoek.magicalscepter.mixin;

import io.github.pistonpoek.magicalscepter.world.event.ModGameEvent;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;

@Mixin(VibrationSystem.class)
public interface VibrationsMixin {
    /**
     * Add mod frequencies at static initialization.
     *
     * @param consumer Consumer for existing frequencies.
     * @return Wrap the consumer to accept additional mod frequencies.
     */
    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;make(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;"))
    private static Consumer<Reference2IntOpenHashMap<ResourceKey<GameEvent>>>
    addFrequencies(Consumer<Reference2IntOpenHashMap<ResourceKey<GameEvent>>> consumer) {
        return (frequencies) -> {
            consumer.accept(frequencies);
            frequencies.put(ModGameEvent.SPELL_CAST.key(), 3);
        };
    }
}
