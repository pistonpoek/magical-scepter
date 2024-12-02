package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;
import io.github.pistonpoek.magicalscepter.registry.ModRegistries;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;

import java.util.function.Function;

public interface SpellEffect {
    Codec<SpellEffect> CODEC = ModRegistries.SPELL_EFFECT_TYPE.getCodec().dispatch(SpellEffect::getCodec, Function.identity());

    static void register(Registry<MapCodec<? extends SpellEffect>> registry) {
        Registry.register(registry, ModIdentifier.of("play_sound"), PlaySoundSpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("spawn_particles"), SpawnParticleSpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("apply_mob_effect"), ApplyMobEffectSpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("summon_entity"), SummonEntitySpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("teleport"), TeleportSpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("damage"), DamageSpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("move"), MoveSpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("rotate"), RotateSpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("run_function"), RunFunctionSpellEffect.CODEC);
    }

    void apply(SpellContext context);

    MapCodec<? extends SpellEffect> getCodec();
}