package net.pistonpoek.magicalscepter.spell;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.pistonpoek.magicalscepter.entity.effect.ModStatusEffects;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.spell.effect.*;
import net.pistonpoek.magicalscepter.spell.projectile.SpellProjectile;

import java.util.ArrayList;
import java.util.List;

public class Spells {
    public static final List<RegistryKey<Spell>> SPELL_KEYS = new ArrayList<>();
    public static final RegistryKey<Spell> MAGICAL_ATTACK_KEY = of("magical_attack");
    public static final RegistryKey<Spell> MAGICAL_RESISTANCE_KEY = of("magical_resistance");
    public static final RegistryKey<Spell> BLAZE_SMALL_FIREBALL_KEY = of("blaze_small_fireball");
    public static final RegistryKey<Spell> BLAZE_FIRE_RESISTANCE_KEY = of("blaze_fire_resistance");
    public static final RegistryKey<Spell> BREEZE_WIND_CHARGE_KEY = of("breeze_wind_charge");
    public static final RegistryKey<Spell> BREEZE_JUMP_KEY = of("breeze_jump");
    public static final RegistryKey<Spell> DRAGON_FIREBALL_KEY = of("dragon_fireball");
    public static final RegistryKey<Spell> DRAGON_PROTECT_KEY = of("dragon_protect");
    public static final RegistryKey<Spell> EVOKER_FANG_LINE_KEY = of("evoker_fang_line");
    public static final RegistryKey<Spell> EVOKER_FANG_CIRCLE_KEY = of("evoker_fang_circle");
    public static final RegistryKey<Spell> GHAST_FIREBALL_KEY = of("ghast_fireball");
    public static final RegistryKey<Spell> GHAST_REGENERATION_KEY = of("ghast_regeneration");
    public static final RegistryKey<Spell> GUARDIAN_BEAM_KEY = of("guardian_beam");
    public static final RegistryKey<Spell> GUARDIAN_HASTE_KEY = of("guardian_haste");
    public static final RegistryKey<Spell> SHULKER_BULLET_KEY = of("shulker_bullet");
    public static final RegistryKey<Spell> SHULKER_TELEPORT_KEY = of("shulker_teleport");
    public static final RegistryKey<Spell> WARDEN_SONIC_BOOM_KEY = of("warden_sonic_boom");
    public static final RegistryKey<Spell> WARDEN_STABILITY_KEY = of("warden_stability");
    public static final RegistryKey<Spell> WITHER_SKULL_KEY = of("wither_skull");
    public static final RegistryKey<Spell> WITHER_SHIELD_KEY = of("wither_shield");

    private static RegistryKey<Spell> of(String id) {
        return RegistryKey.of(ModRegistryKeys.SPELL, ModIdentifier.of(id));

    }

    public static void bootstrap(Registerable<Spell> registry) {
        register(registry, MAGICAL_ATTACK_KEY, Spell.builder(10, 30)
                .addEffect(new PlaySoundSpellEffect(
                        RegistryEntry.of(SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL),
                        ConstantFloatProvider.create(1.0F),
                        UniformFloatProvider.create(0.8F, 1.2F)))
                .addEffect(new ShootProjectileSpellEffect(
                        RegistryEntryList.of(SpellProjectile.Type.DRAGON_FIREBALL.getEntry()),
                        1.0F))
        );
        register(registry, MAGICAL_RESISTANCE_KEY, Spell.builder(10, 30)
                .addEffect(new PlaySoundSpellEffect(
                        RegistryEntry.of(SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL),
                        ConstantFloatProvider.create(1.0F),
                        UniformFloatProvider.create(0.8F, 1.2F)))
                .addEffect(new ApplyMobEffectSpellEffect(
                        RegistryEntryList.of(StatusEffects.RESISTANCE),
                        ConstantFloatProvider.create(1000.0F),
                        ConstantFloatProvider.create(400.0f),
                        ConstantFloatProvider.create(1.0F),
                        ConstantFloatProvider.create(3.0F)))
        );

        register(registry, BLAZE_SMALL_FIREBALL_KEY, Spell.builder(10, 30)
                .addEffect(new PlaySoundSpellEffect(
                        RegistryEntry.of(SoundEvents.ENTITY_BLAZE_SHOOT),
                        ConstantFloatProvider.create(1.0F),
                        UniformFloatProvider.create(0.8F, 1.2F)))
                .addEffect(new ShootProjectileSpellEffect(
                        RegistryEntryList.of(SpellProjectile.Type.SMALL_FIREBALL.getEntry()),
                        1.0F))
        );
        register(registry, BLAZE_FIRE_RESISTANCE_KEY, Spell.builder(10, 30)
                .addEffect(new ApplyMobEffectSpellEffect(
                        RegistryEntryList.of(StatusEffects.FIRE_RESISTANCE),
                        ConstantFloatProvider.create(1000.0F),
                        ConstantFloatProvider.create(400.0f),
                        ConstantFloatProvider.create(1.0F),
                        ConstantFloatProvider.create(3.0F)))
        );

        register(registry, BREEZE_WIND_CHARGE_KEY, Spell.builder(10, 30)
                .addEffect(new PlaySoundSpellEffect(
                        RegistryEntry.of(SoundEvents.ENTITY_BREEZE_SHOOT),
                        ConstantFloatProvider.create(1.0F),
                        UniformFloatProvider.create(0.8F, 1.2F)))
                .addEffect(new ShootProjectileSpellEffect(
                        RegistryEntryList.of(SpellProjectile.Type.WIND_CHARGE.getEntry()),
                        1.5F))
        );
        register(registry, BREEZE_JUMP_KEY, Spell.builder(10, 30)
                .addEffect(new ApplyMobEffectSpellEffect(
                        RegistryEntryList.of(StatusEffects.JUMP_BOOST),
                        ConstantFloatProvider.create(1000.0F),
                        ConstantFloatProvider.create(400.0f),
                        ConstantFloatProvider.create(1.0F),
                        ConstantFloatProvider.create(3.0F)))
        );

        register(registry, DRAGON_FIREBALL_KEY, Spell.builder(10, 30)
                .addEffect(new PlaySoundSpellEffect(
                        RegistryEntry.of(SoundEvents.ENTITY_ENDER_DRAGON_SHOOT),
                        ConstantFloatProvider.create(1.0F),
                        UniformFloatProvider.create(0.8F, 1.2F)))
                .addEffect(new ShootProjectileSpellEffect(
                        RegistryEntryList.of(SpellProjectile.Type.DRAGON_FIREBALL.getEntry()),
                        1.0F))
        );
        register(registry, DRAGON_PROTECT_KEY, Spell.builder(10, 30)
                .addEffect(new PlaySoundSpellEffect(
                        RegistryEntry.of(SoundEvents.ENTITY_ENDER_DRAGON_GROWL),
                        ConstantFloatProvider.create(1.0F),
                        UniformFloatProvider.create(0.8F, 1.2F)))
        );

        register(registry, EVOKER_FANG_LINE_KEY, Spell.builder(10, 30)
                .addEffect(new PlaySoundSpellEffect(
                        RegistryEntry.of(SoundEvents.ENTITY_EVOKER_CAST_SPELL),
                        ConstantFloatProvider.create(1.0F),
                        UniformFloatProvider.create(0.8F, 1.2F)))
        );
        register(registry, EVOKER_FANG_CIRCLE_KEY, Spell.builder(10, 30)
                .addEffect(new PlaySoundSpellEffect(
                        RegistryEntry.of(SoundEvents.ENTITY_EVOKER_CAST_SPELL),
                        ConstantFloatProvider.create(1.0F),
                        UniformFloatProvider.create(0.8F, 1.2F)))
        );

        register(registry, GHAST_FIREBALL_KEY, Spell.builder(10, 30)
                .addEffect(new PlaySoundSpellEffect(
                        RegistryEntry.of(SoundEvents.ENTITY_GHAST_SHOOT),
                        ConstantFloatProvider.create(1.0F),
                        UniformFloatProvider.create(0.8F, 1.2F)))
                .addEffect(new ShootProjectileSpellEffect(
                        RegistryEntryList.of(SpellProjectile.Type.FIREBALL.getEntry()),
                        1.0F))
        );
        register(registry, GHAST_REGENERATION_KEY, Spell.builder(10, 30)
                .addEffect(new ApplyMobEffectSpellEffect(
                        RegistryEntryList.of(StatusEffects.REGENERATION),
                        ConstantFloatProvider.create(1000.0F),
                        ConstantFloatProvider.create(400.0f),
                        ConstantFloatProvider.create(1.0F),
                        ConstantFloatProvider.create(3.0F)))
        );

        register(registry, GUARDIAN_BEAM_KEY, Spell.builder(10, 30)

        );
        register(registry, GUARDIAN_HASTE_KEY, Spell.builder(10, 30)
                .addEffect(new ApplyMobEffectSpellEffect(
                        RegistryEntryList.of(StatusEffects.HASTE),
                        ConstantFloatProvider.create(1000.0F),
                        ConstantFloatProvider.create(400.0f),
                        ConstantFloatProvider.create(1.0F),
                        ConstantFloatProvider.create(3.0F)))
        );

        register(registry, SHULKER_BULLET_KEY, Spell.builder(10, 30)
                .addEffect(new PlaySoundSpellEffect(
                        RegistryEntry.of(SoundEvents.ENTITY_SHULKER_SHOOT),
                        ConstantFloatProvider.create(1.0F),
                        UniformFloatProvider.create(0.8F, 1.2F)))
                .addEffect(new ShootProjectileSpellEffect(
                        RegistryEntryList.of(SpellProjectile.Type.SHULKER_BULLET.getEntry()),
                        1.0F))
        );
        register(registry, SHULKER_TELEPORT_KEY, Spell.builder(10, 30)
                .addEffect(new PlaySoundSpellEffect(
                        RegistryEntry.of(SoundEvents.ENTITY_SHULKER_TELEPORT),
                        ConstantFloatProvider.create(1.0F),
                        UniformFloatProvider.create(0.8F, 1.2F)))
                .addEffect(new RandomTeleportSpellEffect(
                        true, 16.0))
        );

        register(registry, WARDEN_SONIC_BOOM_KEY, Spell.builder(10, 30)
                .addEffect(new PlaySoundSpellEffect(
                        RegistryEntry.of(SoundEvents.ENTITY_WARDEN_SONIC_BOOM),
                        ConstantFloatProvider.create(1.0F),
                        UniformFloatProvider.create(0.8F, 1.2F)))
        );
        register(registry, WARDEN_STABILITY_KEY, Spell.builder(10, 30)
                .addEffect(new ApplyMobEffectSpellEffect(
                        RegistryEntryList.of(ModStatusEffects.STABILITY),
                        ConstantFloatProvider.create(1000.0F),
                        ConstantFloatProvider.create(400.0f),
                        ConstantFloatProvider.create(1.0F),
                        ConstantFloatProvider.create(3.0F)))
        );

        register(registry, WITHER_SKULL_KEY, Spell.builder(10, 30)
                .addEffect(new PlaySoundSpellEffect(
                        RegistryEntry.of(SoundEvents.ENTITY_WITHER_SHOOT),
                        ConstantFloatProvider.create(1.0F),
                        UniformFloatProvider.create(0.8F, 1.2F)))
                .addEffect(new ShootProjectileSpellEffect(
                        RegistryEntryList.of(SpellProjectile.Type.WITHER_SKULL.getEntry()),
                        1.0F))
        );
        register(registry, WITHER_SHIELD_KEY, Spell.builder(10, 30)
        );
    }

    private static void register(Registerable<Spell> registry, RegistryKey<Spell> key, Spell.Builder builder) {
        SPELL_KEYS.add(key);
        registry.register(key, builder.build());
    }
}
