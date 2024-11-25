package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;
import io.github.pistonpoek.magicalscepter.registry.ModRegistries;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import io.github.pistonpoek.magicalscepter.spell.effect.projectile.ShootProjectileSpellEffect;

import java.util.function.Function;

public interface SpellEffect {
    Codec<SpellEffect> CODEC = ModRegistries.SPELL_EFFECT_TYPE.getCodec().dispatch(SpellEffect::getCodec, Function.identity());

    static void register(Registry<MapCodec<? extends SpellEffect>> registry) {
        Registry.register(registry, ModIdentifier.of("play_sound"), PlaySoundSpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("spawn_particles"), SpawnParticleSpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("apply_mob_effect"), ApplyMobEffectSpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("summon_entity"), SummonEntitySpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("shoot_projectile"), ShootProjectileSpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("teleport"), TeleportSpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("damage"), DamageSpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("move"), MoveSpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("rotate"), RotateSpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("run_function"), RunFunctionSpellEffect.CODEC);

        // TODO add run_function
        // done? TODO add motion (Rotation rotation, FloatProvider power, bool consider_knockback_resistance)

        // TODO add explode (power, ...) see ExplodeEnchantmentEffect?
        // TODO ignite (duration)

        // Alter blocks?

//        Registry.register(registry, "all_of", AllOfEnchantmentEffects.EntityEffects.CODEC);
//        Registry.register(registry, "damage_entity", DamageEntityEnchantmentEffect.CODEC);
//        Registry.register(registry, "damage_item", DamageItemEnchantmentEffect.CODEC);
//        Registry.register(registry, "explode", ExplodeEnchantmentEffect.CODEC);
//        Registry.register(registry, "ignite", IgniteEnchantmentEffect.CODEC);
//        Registry.register(registry, "replace_block", ReplaceBlockEnchantmentEffect.CODEC);
//        Registry.register(registry, "replace_disk", ReplaceDiskEnchantmentEffect.CODEC);
//        Registry.register(registry, "run_function", RunFunctionEnchantmentEffect.CODEC);
//        Registry.register(registry, "set_block_properties", SetBlockPropertiesEnchantmentEffect.CODEC);
    }
// TODO adding entity target to the method, maybe utilize spellContext to keep method adjustable.
    void apply(SpellContext context);

    MapCodec<? extends SpellEffect> getCodec();
}