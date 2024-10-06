package net.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistries;
import net.pistonpoek.magicalscepter.spell.effect.projectile.ShootProjectileSpellEffect;

import java.util.function.Function;

public interface SpellEffect {
    Codec<SpellEffect> CODEC = ModRegistries.SPELL_EFFECT_TYPE.getCodec().dispatch(SpellEffect::getCodec, Function.identity());

    static void register(Registry<MapCodec<? extends SpellEffect>> registry) {
        Registry.register(registry, ModIdentifier.of("play_sound"), PlaySoundSpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("spawn_particles"), SpawnParticlesSpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("apply_mob_effect"), ApplyMobEffectSpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("summon_entity"), SummonEntitySpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("shoot_projectile"), ShootProjectileSpellEffect.CODEC);
        Registry.register(registry, ModIdentifier.of("random_teleport"), RandomTeleportSpellEffect.CODEC);
        // run_function
        // explode
        //


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

    void apply(ServerWorld world, LivingEntity entity, Vec3d position, float pitch, float yaw);

    MapCodec<? extends SpellEffect> getCodec();
}