package io.github.pistonpoek.magicalscepter.mixin;

import io.github.pistonpoek.magicalscepter.world.event.ModGameEvent;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.Vibrations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Consumer;

@Mixin(Vibrations.class)
public interface VibrationsMixin {
    /**
     * Add mod frequencies at static initialization.
     *
     * @param consumer Consumer for existing frequencies.
     * @return Wrap the consumer to accept additional mod frequencies.
     */
    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;make(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;"))
    private static Consumer<Reference2IntOpenHashMap<RegistryKey<GameEvent>>>
    addFrequencies(Consumer<Reference2IntOpenHashMap<RegistryKey<GameEvent>>> consumer) {
        return (frequencies) -> {
            consumer.accept(frequencies);
            frequencies.put(ModGameEvent.SPELL_CAST.registryKey(), 3);
        };
    }
}
