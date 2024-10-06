package net.pistonpoek.magicalscepter.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.pistonpoek.magicalscepter.scepter.Scepter;
import net.pistonpoek.magicalscepter.spell.Spell;
import net.pistonpoek.magicalscepter.spell.cast.transformer.CastTransformer;
import net.pistonpoek.magicalscepter.spell.effect.projectile.ShootProjectileSpellEffect;
import net.pistonpoek.magicalscepter.spell.effect.SpellEffect;
import net.pistonpoek.magicalscepter.spell.position.PositionSource;
import net.pistonpoek.magicalscepter.spell.rotation.RotationSource;

public class ModRegistryKeys {
    public static final RegistryKey<Registry<Scepter>> SCEPTER = of("scepter");
    public static final RegistryKey<Registry<Spell>> SPELL = of("spell");
    public static final RegistryKey<Registry<MapCodec<? extends SpellEffect>>> SPELL_EFFECT_TYPE =
            of("spell_effect_type");
    public static final RegistryKey<Registry<MapCodec<? extends ShootProjectileSpellEffect>>> SPELL_EFFECT_PROJECTILE =
            of("spell_effect_projectile");
    public static final RegistryKey<Registry<MapCodec<? extends CastTransformer>>> CAST_TRANSFORMER_TYPE =
            of("cast_transformer_type");
    public static final RegistryKey<Registry<MapCodec<? extends PositionSource>>> CAST_POSITION_SOURCE_TYPE =
            of("cast_position_source_type");
    public static final RegistryKey<Registry<MapCodec<? extends RotationSource>>> CAST_ROTATION_SOURCE_TYPE =
            of("cast_rotation_source_type");

    private static <T> RegistryKey<Registry<T>> of(String id) {
        return RegistryKey.ofRegistry(ModIdentifier.of(id));
    }
}
