package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.pistonpoek.magicalscepter.registry.ModRegistries;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.registry.Registry;

import java.util.function.Function;

/**
 * TODO
 */
public interface SpellEffect {
    Codec<SpellEffect> CODEC = ModRegistries.SPELL_EFFECT_TYPE.getCodec().dispatch(SpellEffect::getCodec, Function.identity());

    /**
     * TODO
     *
     * @param registry
     */
    static void register(Registry<MapCodec<? extends SpellEffect>> registry) {
        Registry.register(registry, ModIdentifier.of("play_sound"), PlaySoundSpellEffect.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("spawn_particles"), SpawnParticleSpellEffect.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("apply_mob_effect"), ApplyMobEffectSpellEffect.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("remove_mob_effect"), RemoveMobEffectSpellEffect.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("summon_entity"), SummonEntitySpellEffect.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("teleport"), TeleportSpellEffect.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("damage"), DamageSpellEffect.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("move"), MoveSpellEffect.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("rotate"), RotateSpellEffect.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("ignite"), IgniteSpellEffect.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("explode"), ExplodeSpellEffect.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("replace_block"), ReplaceBlockSpellEffect.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("run_function"), RunFunctionSpellEffect.MAP_CODEC);
    }

    /**
     * TODO
     *
     * @param context
     */
    void apply(SpellContext context);

    /**
     * TODO
     *
     * @return
     */
    MapCodec<? extends SpellEffect> getCodec();
}