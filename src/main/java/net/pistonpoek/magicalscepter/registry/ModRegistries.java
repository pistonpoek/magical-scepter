package net.pistonpoek.magicalscepter.registry;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.*;
import net.pistonpoek.magicalscepter.scepter.Scepter;
import net.pistonpoek.magicalscepter.spell.Spell;
import net.pistonpoek.magicalscepter.spell.cast.transformer.CastTransformer;
import net.pistonpoek.magicalscepter.spell.effect.projectile.ShootProjectileSpellEffect;
import net.pistonpoek.magicalscepter.spell.effect.SpellEffect;
import net.pistonpoek.magicalscepter.spell.position.PositionSource;
import net.pistonpoek.magicalscepter.spell.rotation.RotationSource;

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

    public static void init() {
        SpellEffect.register(SPELL_EFFECT_TYPE);
        ShootProjectileSpellEffect.register(SPELL_EFFECT_PROJECTILE);
        CastTransformer.register(CAST_TRANSFORMER_TYPE);
        PositionSource.register(CAST_POSITION_SOURCE_TYPE);
        RotationSource.register(CAST_ROTATION_SOURCE_TYPE);
        DynamicRegistries.registerSynced(ModRegistryKeys.SCEPTER, Scepter.CODEC, Scepter.NETWORK_CODEC);
        DynamicRegistries.registerSynced(ModRegistryKeys.SPELL, Spell.CODEC, Spell.NETWORK_CODEC);
    }
}
