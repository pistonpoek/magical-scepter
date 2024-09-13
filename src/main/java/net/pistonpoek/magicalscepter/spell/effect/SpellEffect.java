package net.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.StringIdentifiable;
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
        Registry.register(registry, "random_teleport", RandomTeleportSpellEffect.CODEC);
//        Registry.register(registry, "all_of", AllOfEnchantmentEffects.EntityEffects.CODEC);
//        Registry.register(registry, "apply_mob_effect", ApplyMobEffectEnchantmentEffect.CODEC);
//        Registry.register(registry, "damage_entity", DamageEntityEnchantmentEffect.CODEC);
//        Registry.register(registry, "damage_item", DamageItemEnchantmentEffect.CODEC);
//        Registry.register(registry, "explode", ExplodeEnchantmentEffect.CODEC);
//        Registry.register(registry, "ignite", IgniteEnchantmentEffect.CODEC);
//        Registry.register(registry, "play_sound", PlaySoundEnchantmentEffect.CODEC);
//        Registry.register(registry, "replace_block", ReplaceBlockEnchantmentEffect.CODEC);
//        Registry.register(registry, "replace_disk", ReplaceDiskEnchantmentEffect.CODEC);
//        Registry.register(registry, "run_function", RunFunctionEnchantmentEffect.CODEC);
//        Registry.register(registry, "set_block_properties", SetBlockPropertiesEnchantmentEffect.CODEC);
//        Registry.register(registry, "spawn_particles", SpawnParticlesEnchantmentEffect.CODEC);
//        return Registry.register(registry, "summon_entity", SummonEntityEnchantmentEffect.CODEC);
    }


    // See SpawnParticlesEnchantmentEffect
    public enum Target implements StringIdentifiable {
        CASTER("caster"),
        ENTITY("entity"),
        POSITION("position");

        private final String name;

        Target(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return name;
        }
    }

    void apply(ServerWorld world, Entity caster, Vec3d pos);

    MapCodec<? extends SpellEffect> getCodec();
}