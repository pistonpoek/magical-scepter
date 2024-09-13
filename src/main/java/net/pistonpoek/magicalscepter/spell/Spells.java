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
import net.pistonpoek.magicalscepter.spell.effect.ApplyMobEffectSpellEffect;
import net.pistonpoek.magicalscepter.spell.effect.PlaySoundSpellEffect;
import net.pistonpoek.magicalscepter.spell.effect.ShootProjectileSpellEffect;
import net.pistonpoek.magicalscepter.spell.effect.SpellEffect;
import net.pistonpoek.magicalscepter.spell.projectile.SpellProjectile;

import java.util.Arrays;

public class Spells {
    public static final RegistryKey<Spell> MAGICAL_KEY = of("magical");
    public static final RegistryKey<Spell> BLAZE_KEY = of("blaze");
    public static final RegistryKey<Spell> BREEZE_KEY = of("breeze");
    public static final RegistryKey<Spell> DRAGON_KEY = of("dragon");
    public static final RegistryKey<Spell> EVOKER_KEY = of("evoker");
    public static final RegistryKey<Spell> GHAST_KEY = of("ghast");
    public static final RegistryKey<Spell> GUARDIAN_KEY = of("guardian");
    public static final RegistryKey<Spell> SHULKER_KEY = of("shulker");
    public static final RegistryKey<Spell> WARDEN_KEY = of("warden");
    public static final RegistryKey<Spell> WITHER_KEY = of("wither");

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

    public static final Spell BREEZE_WIND_CHARGE = create(
            new PlaySoundSpellEffect(
                    RegistryEntry.of(SoundEvents.ENTITY_BREEZE_SHOOT),
                    ConstantFloatProvider.create(1.0F),
                    UniformFloatProvider.create(0.8F, 1.2F)),
            new ShootProjectileSpellEffect(
                    RegistryEntryList.of(SpellProjectile.Type.WIND_CHARGE.getEntry()),
                    1.5F)); // Effect (Fire resistance)); // SoundEvents.ENTITY_BREEZE_JUMP

    public static final Spell DRAGON = create(
            new PlaySoundSpellEffect(
                    RegistryEntry.of(SoundEvents.ENTITY_ENDER_DRAGON_SHOOT),
                    ConstantFloatProvider.create(1.0F),
                    UniformFloatProvider.create(0.8F, 1.2F)),
            new ShootProjectileSpellEffect(
                    RegistryEntryList.of(SpellProjectile.Type.DRAGON_FIREBALL.getEntry()),
                    1.0F)); // SoundEvents.ENTITY_ENDER_DRAGON_GROWL

    public static final Spell EVOKER = create(
            new PlaySoundSpellEffect(RegistryEntry.of(SoundEvents.ENTITY_EVOKER_CAST_SPELL), ConstantFloatProvider.create(1.0F), UniformFloatProvider.create(0.8F, 1.2F))); // SoundEvents.ENTITY_EVOKER_CAST_SPELL

    public static final Spell GHAST = create(
            new PlaySoundSpellEffect(
                    RegistryEntry.of(SoundEvents.ENTITY_GHAST_SHOOT),
                    ConstantFloatProvider.create(1.0F),
                    UniformFloatProvider.create(0.8F, 1.2F)),
            new ShootProjectileSpellEffect(
                    RegistryEntryList.of(SpellProjectile.Type.FIREBALL.getEntry()),
                    1.0F)); // Effect (Regeneration)

    public static final Spell GUARDIAN = create(
            new PlaySoundSpellEffect(RegistryEntry.of(SoundEvents.ENTITY_GUARDIAN_ATTACK), ConstantFloatProvider.create(1.0F), UniformFloatProvider.create(0.8F, 1.2F))); // Effect (Haste)

    public static final Spell SHULKER = create(
            new PlaySoundSpellEffect(
                    RegistryEntry.of(SoundEvents.ENTITY_SHULKER_SHOOT),
                    ConstantFloatProvider.create(1.0F),
                    UniformFloatProvider.create(0.8F, 1.2F)),
            new ShootProjectileSpellEffect(
                    RegistryEntryList.of(SpellProjectile.Type.SHULKER_BULLET.getEntry()),
                    1.0F)); // SoundEvents.ENTITY_SHULKER_TELEPORT

    public static final Spell WARDEN = create(
            new PlaySoundSpellEffect(
                    RegistryEntry.of(SoundEvents.ENTITY_WARDEN_SONIC_BOOM),
                    ConstantFloatProvider.create(1.0F),
                    UniformFloatProvider.create(0.8F, 1.2F)),
            new ApplyMobEffectSpellEffect(
                    RegistryEntryList.of(ModStatusEffects.STABILITY),
                    ConstantFloatProvider.create(1000.0F),
                    ConstantFloatProvider.create(400.0f),
                    ConstantFloatProvider.create(1.0F),
                    ConstantFloatProvider.create(3.0F)
            )); // Effect (Stability)

    public static final Spell WITHER = create(
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
