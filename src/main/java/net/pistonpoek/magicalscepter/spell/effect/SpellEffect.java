package net.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.pistonpoek.magicalscepter.registry.ModRegistries;

import java.util.function.Function;

public interface SpellEffect {
    Codec<SpellEffect> CODEC = ModRegistries.SPELL_EFFECT_TYPE.getCodec().dispatch(SpellEffect::getCodec, Function.identity());

    static void register(Registry<MapCodec<? extends SpellEffect>> registry) {
        Registry.register(registry, "play_sound", PlaySoundSpellEffect.CODEC);
        Registry.register(registry, "spawn_particle", SpawnParticleSpellEffect.CODEC);
        Registry.register(registry, "apply_mob_effect", ApplyMobEffectSpellEffect.CODEC);
        Registry.register(registry, "summon_entity", SummonEntitySpellEffect.CODEC);
        Registry.register(registry, "shoot_projectile", ShootProjectileSpellEffect.CODEC);
    }

    void apply(ServerWorld world, Entity user, Vec3d pos);

    MapCodec<? extends SpellEffect> getCodec();
}