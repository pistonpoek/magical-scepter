package io.github.pistonpoek.magicalscepter.registry;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.*;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContextSource;
import io.github.pistonpoek.magicalscepter.spell.cast.transformer.CastTransformer;
import io.github.pistonpoek.magicalscepter.spell.effect.projectile.ShootProjectileSpellEffect;
import io.github.pistonpoek.magicalscepter.spell.effect.SpellEffect;
import io.github.pistonpoek.magicalscepter.spell.position.PositionSource;
import io.github.pistonpoek.magicalscepter.spell.rotation.RotationSource;
import io.github.pistonpoek.magicalscepter.spell.target.TargetSource;

public class ModRegistries {
    public static final Registry<MapCodec<? extends SpellEffect>> SPELL_EFFECT_TYPE =
            FabricRegistryBuilder.createSimple(ModRegistryKeys.SPELL_EFFECT_TYPE).buildAndRegister();
    public static final Registry<MapCodec<? extends ShootProjectileSpellEffect>> SPELL_EFFECT_PROJECTILE =
            FabricRegistryBuilder.createSimple(ModRegistryKeys.SPELL_EFFECT_PROJECTILE).buildAndRegister();
    public static final Registry<MapCodec<? extends CastTransformer>> CAST_TRANSFORMER_TYPE =
            FabricRegistryBuilder.createSimple(ModRegistryKeys.CAST_TRANSFORMER_TYPE).buildAndRegister();
    public static final Registry<MapCodec<? extends PositionSource>> CAST_POSITION_SOURCE_TYPE =
            FabricRegistryBuilder.createSimple(ModRegistryKeys.CAST_POSITION_SOURCE_TYPE).buildAndRegister();
    public static final Registry<MapCodec<? extends RotationSource>> CAST_ROTATION_SOURCE_TYPE =
            FabricRegistryBuilder.createSimple(ModRegistryKeys.CAST_ROTATION_SOURCE_TYPE).buildAndRegister();
    public static final Registry<MapCodec<? extends TargetSource>> CAST_TARGET_SOURCE_TYPE =
            FabricRegistryBuilder.createSimple(ModRegistryKeys.CAST_TARGET_SOURCE_TYPE).buildAndRegister();
    public static final Registry<MapCodec<? extends SpellContextSource>> CAST_CONTEXT_SOURCE_TYPE =
            FabricRegistryBuilder.createSimple(ModRegistryKeys.CAST_CONTEXT_SOURCE_TYPE).buildAndRegister();

    public static void init() {
        SpellEffect.register(SPELL_EFFECT_TYPE);
        ShootProjectileSpellEffect.register(SPELL_EFFECT_PROJECTILE);
        CastTransformer.register(CAST_TRANSFORMER_TYPE);
        PositionSource.register(CAST_POSITION_SOURCE_TYPE);
        RotationSource.register(CAST_ROTATION_SOURCE_TYPE);
        TargetSource.register(CAST_TARGET_SOURCE_TYPE);
        SpellContextSource.register(CAST_CONTEXT_SOURCE_TYPE);
        DynamicRegistries.registerSynced(ModRegistryKeys.SCEPTER, Scepter.CODEC, Scepter.NETWORK_CODEC);
        DynamicRegistries.registerSynced(ModRegistryKeys.SPELL, Spell.CODEC, Spell.NETWORK_CODEC);
    }
}
