package net.pistonpoek.magicalscepter.registry;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.registry.*;
import net.pistonpoek.magicalscepter.scepter.Scepter;
import net.pistonpoek.magicalscepter.scepter.Scepters;
import net.pistonpoek.magicalscepter.spell.Spell;
import net.pistonpoek.magicalscepter.spell.Spells;
import net.pistonpoek.magicalscepter.spell.effect.SpellEffect;
import net.pistonpoek.magicalscepter.spell.projectile.SpellProjectile;

public class ModRegistries {
    public static final Registry<MapCodec<? extends SpellEffect>> SPELL_EFFECT_TYPE =
            FabricRegistryBuilder.createSimple(ModRegistryKeys.SPELL_EFFECT_TYPE).buildAndRegister();
    public static final Registry<SpellProjectile<? extends Entity>> SPELL_PROJECTILE_TYPE =
            FabricRegistryBuilder.createSimple(ModRegistryKeys.SPELL_PROJECTILE_TYPE).buildAndRegister();

    public static void init() {
        SpellEffect.register(SPELL_EFFECT_TYPE);
        SpellProjectile.register(SPELL_PROJECTILE_TYPE);
        Scepters.init();
        Spells.init();
        DynamicRegistries.registerSynced(ModRegistryKeys.SCEPTER, Scepter.CODEC);
        DynamicRegistries.registerSynced(ModRegistryKeys.SPELL, Spell.CODEC);
    }
}
