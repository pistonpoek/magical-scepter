package net.pistonpoek.magicalscepter.registry;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.*;
import net.pistonpoek.magicalscepter.scepter.Scepter;
import net.pistonpoek.magicalscepter.spell.Spell;
import net.pistonpoek.magicalscepter.spell.cast.SpellCast;
import net.pistonpoek.magicalscepter.spell.effect.projectile.ShootProjectileSpellEffect;
import net.pistonpoek.magicalscepter.spell.effect.SpellEffect;

public class ModRegistries {
    public static final Registry<MapCodec<? extends SpellEffect>> SPELL_EFFECT_TYPE =
            FabricRegistryBuilder.createSimple(ModRegistryKeys.SPELL_EFFECT_TYPE).buildAndRegister();
    public static final Registry<MapCodec<? extends ShootProjectileSpellEffect>> SPELL_EFFECT_PROJECTILE =
            FabricRegistryBuilder.createSimple(ModRegistryKeys.SPELL_EFFECT_PROJECTILE).buildAndRegister();
    public static final Registry<MapCodec<? extends SpellCast>> SPELL_CAST_TYPE =
            FabricRegistryBuilder.createSimple(ModRegistryKeys.SPELL_CAST_TYPE).buildAndRegister();

    public static void init() {
        SpellEffect.register(SPELL_EFFECT_TYPE);
        ShootProjectileSpellEffect.register(SPELL_EFFECT_PROJECTILE);
        SpellCast.register(SPELL_CAST_TYPE);
        DynamicRegistries.registerSynced(ModRegistryKeys.SCEPTER, Scepter.CODEC, Scepter.NETWORK_CODEC);
        DynamicRegistries.registerSynced(ModRegistryKeys.SPELL, Spell.CODEC, Spell.NETWORK_CODEC);
    }
}
