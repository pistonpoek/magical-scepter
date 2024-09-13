package net.pistonpoek.magicalscepter.spell;

import net.minecraft.entity.effect.StatusEffects;
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

import java.util.Arrays;

public class Spells {
    public static final RegistryKey<Spell> MAGICAL_KEY = of("magical");
    public static final RegistryKey<Spell> BLAZE_SMALL_FIREBALL_KEY = of("blaze_small_fireball");
    public static final RegistryKey<Spell> BLAZE_FIRE_RESISTANCE_KEY = of("blaze_fire_resistance");
    public static final RegistryKey<Spell> BREEZE_WIND_CHARGE_KEY = of("breeze_wind_charge");
    public static final RegistryKey<Spell> DRAGON_FIREBALL_KEY = of("dragon_fireball");
    public static final RegistryKey<Spell> EVOKER_FANG_LINE_KEY = of("evoker_fang_line");
    public static final RegistryKey<Spell> EVOKER_FANG_CIRCLE_KEY = of("evoker_fang_circle");
    public static final RegistryKey<Spell> GHAST_FIREBALL_KEY = of("ghast_fireball");
    public static final RegistryKey<Spell> GHAST_REGENERATION_KEY = of("ghast_regeneration");
    public static final RegistryKey<Spell> GUARDIAN_KEY = of("guardian");
    public static final RegistryKey<Spell> SHULKER_BULLET_KEY = of("shulker_bullet");
    public static final RegistryKey<Spell> SHULKER_TELEPORT_KEY = of("shulker_teleport");
    public static final RegistryKey<Spell> WARDEN_SONIC_BOOM_KEY = of("warden_sonic_boom");
    public static final RegistryKey<Spell> WARDEN_STABILITY_KEY = of("warden_stability");
    public static final RegistryKey<Spell> WITHER_SKULL_KEY = of("wither_skull");

    public static final RegistryKey<Spell> DEFAULT_KEY = MAGICAL_KEY;

    private static RegistryKey<Spell> of(String id) {
        return RegistryKey.of(ModRegistryKeys.SPELL, ModIdentifier.of(id));

    }

    // Second spell sound effect
    public static final Spell MAGICAL = create(
            new PlaySoundSpellEffect(
                    RegistryEntry.of(SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL),
                    ConstantFloatProvider.create(1.0F),
                    UniformFloatProvider.create(0.8F, 1.2F)),
            new ApplyMobEffectSpellEffect(
                    RegistryEntryList.of(StatusEffects.RESISTANCE),
                    ConstantFloatProvider.create(1000.0F),
                    ConstantFloatProvider.create(400.0f),
                    ConstantFloatProvider.create(1.0F),
                    ConstantFloatProvider.create(3.0F)
            )); // Effect (Resistance)

    public static final Spell BLAZE_SMALL_FIREBALL = create(
            new PlaySoundSpellEffect(
                    RegistryEntry.of(SoundEvents.ENTITY_BLAZE_SHOOT),
                    ConstantFloatProvider.create(1.0F),
                    UniformFloatProvider.create(0.8F, 1.2F)),
            new ShootProjectileSpellEffect(
                RegistryEntryList.of(SpellProjectile.Type.SMALL_FIREBALL.getEntry()),
                    1.0F)); // Effect (Fire resistance)

    public static final Spell BLAZE_FIRE_RESISTANCE = create(
            new ApplyMobEffectSpellEffect(
                    RegistryEntryList.of(StatusEffects.FIRE_RESISTANCE),
                    ConstantFloatProvider.create(1000.0F),
                    ConstantFloatProvider.create(400.0f),
                    ConstantFloatProvider.create(1.0F),
                    ConstantFloatProvider.create(3.0F)
            )); // Effect (Resistance)

    public static final Spell BREEZE_WIND_CHARGE = create(
            new PlaySoundSpellEffect(
                    RegistryEntry.of(SoundEvents.ENTITY_BREEZE_SHOOT),
                    ConstantFloatProvider.create(1.0F),
                    UniformFloatProvider.create(0.8F, 1.2F)),
            new ShootProjectileSpellEffect(
                    RegistryEntryList.of(SpellProjectile.Type.WIND_CHARGE.getEntry()),
                    1.5F)); // Effect (Fire resistance)); // SoundEvents.ENTITY_BREEZE_JUMP

    public static final Spell DRAGON_FIREBALL = create(
            new PlaySoundSpellEffect(
                    RegistryEntry.of(SoundEvents.ENTITY_ENDER_DRAGON_SHOOT),
                    ConstantFloatProvider.create(1.0F),
                    UniformFloatProvider.create(0.8F, 1.2F)),
            new ShootProjectileSpellEffect(
                    RegistryEntryList.of(SpellProjectile.Type.DRAGON_FIREBALL.getEntry()),
                    1.0F)); // SoundEvents.ENTITY_ENDER_DRAGON_GROWL

    public static final Spell EVOKER = create(
            new PlaySoundSpellEffect(RegistryEntry.of(SoundEvents.ENTITY_EVOKER_CAST_SPELL), ConstantFloatProvider.create(1.0F), UniformFloatProvider.create(0.8F, 1.2F))); // SoundEvents.ENTITY_EVOKER_CAST_SPELL

    public static final Spell GHAST_FIREBALL = create(
            new PlaySoundSpellEffect(
                    RegistryEntry.of(SoundEvents.ENTITY_GHAST_SHOOT),
                    ConstantFloatProvider.create(1.0F),
                    UniformFloatProvider.create(0.8F, 1.2F)),
            new ShootProjectileSpellEffect(
                    RegistryEntryList.of(SpellProjectile.Type.FIREBALL.getEntry()),
                    1.0F)); // Effect (Regeneration)

    public static final Spell GHAST_REGENERATION = create(
            new ApplyMobEffectSpellEffect(
                    RegistryEntryList.of(StatusEffects.REGENERATION),
                    ConstantFloatProvider.create(1000.0F),
                    ConstantFloatProvider.create(400.0f),
                    ConstantFloatProvider.create(1.0F),
                    ConstantFloatProvider.create(3.0F)
            )); // Effect (Regeneration)

    public static final Spell GUARDIAN = create(
            new PlaySoundSpellEffect(RegistryEntry.of(SoundEvents.ENTITY_GUARDIAN_ATTACK), ConstantFloatProvider.create(1.0F), UniformFloatProvider.create(0.8F, 1.2F))); // Effect (Haste)

    public static final Spell SHULKER_BULLET = create(
            new PlaySoundSpellEffect(
                    RegistryEntry.of(SoundEvents.ENTITY_SHULKER_SHOOT),
                    ConstantFloatProvider.create(1.0F),
                    UniformFloatProvider.create(0.8F, 1.2F)),
            new ShootProjectileSpellEffect(
                    RegistryEntryList.of(SpellProjectile.Type.SHULKER_BULLET.getEntry()),
                    1.0F));

    public static final Spell SHULKER_TELEPORT = create(
            new PlaySoundSpellEffect(
                    RegistryEntry.of(SoundEvents.ENTITY_SHULKER_TELEPORT),
                    ConstantFloatProvider.create(1.0F),
                    UniformFloatProvider.create(0.8F, 1.2F)),
            new RandomTeleportSpellEffect(
                   true));

    public static final Spell WARDEN_SONIC_BOOM = create(
            new PlaySoundSpellEffect(
                    RegistryEntry.of(SoundEvents.ENTITY_WARDEN_SONIC_BOOM),
                    ConstantFloatProvider.create(1.0F),
                    UniformFloatProvider.create(0.8F, 1.2F))); // Effect (Stability)

    public static final Spell WARDEN_STABILITY = create(
            new ApplyMobEffectSpellEffect(
                    RegistryEntryList.of(ModStatusEffects.STABILITY),
                    ConstantFloatProvider.create(1000.0F),
                    ConstantFloatProvider.create(400.0f),
                    ConstantFloatProvider.create(1.0F),
                    ConstantFloatProvider.create(3.0F)
            )); // Effect (Stability)

    public static final Spell WITHER_SKULL = create(
            new PlaySoundSpellEffect(
                    RegistryEntry.of(SoundEvents.ENTITY_WITHER_SHOOT),
                    ConstantFloatProvider.create(1.0F),
                    UniformFloatProvider.create(0.8F, 1.2F)),
            new ShootProjectileSpellEffect(
                    RegistryEntryList.of(SpellProjectile.Type.WITHER_SKULL.getEntry()),
                    1.0F)); // Effect (Anti wither)


    private static Spell create(SpellEffect ... effect) {
        return create(30, 100, effect);
    }

    private static Spell create(int experienceCost, int cooldown, SpellEffect ... effects) {
        return new Spell(experienceCost, cooldown, Arrays.stream(effects).toList());
    }

    public static final Spell DEFAULT = MAGICAL;

    public static void init() {
    }

}
