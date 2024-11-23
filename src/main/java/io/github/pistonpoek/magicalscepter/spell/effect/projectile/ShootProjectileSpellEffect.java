package io.github.pistonpoek.magicalscepter.spell.effect.projectile;

import com.mojang.serialization.MapCodec;

import java.util.function.Function;

import io.github.pistonpoek.magicalscepter.spell.effect.SpellEffect;
import net.minecraft.registry.Registry;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;
import io.github.pistonpoek.magicalscepter.registry.ModRegistries;

public interface ShootProjectileSpellEffect extends SpellEffect {
    MapCodec<ShootProjectileSpellEffect> CODEC = ModRegistries.SPELL_EFFECT_PROJECTILE.getCodec()
            .dispatchMap("projectile", ShootProjectileSpellEffect::getProjectileCodec, Function.identity());

    static void register(Registry<MapCodec<? extends ShootProjectileSpellEffect>> registry) {
        Registry.register(registry, ModIdentifier.of("dragon_fireball"), DragonFireballSpellProjectile.CODEC);
        Registry.register(registry, ModIdentifier.of("fireball"), FireballSpellProjectile.CODEC);
        Registry.register(registry, ModIdentifier.of("shulker_bullet"), ShulkerBulletSpellProjectile.CODEC);
        Registry.register(registry, ModIdentifier.of("small_fireball"), SmallFireballSpellProjectile.CODEC);
        Registry.register(registry, ModIdentifier.of("wind_charge"), WindChargeSpellProjectile.CODEC);
        Registry.register(registry, ModIdentifier.of("wither_skull"), WitherSkullSpellProjectile.CODEC);
    }

    // TODO add owner option to summoning?

    MapCodec<? extends ShootProjectileSpellEffect> getProjectileCodec();

    default MapCodec<? extends SpellEffect> getCodec() {
        return CODEC;
    }
}

