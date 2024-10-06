package net.pistonpoek.magicalscepter.spell;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.pistonpoek.magicalscepter.entity.effect.ModStatusEffects;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.spell.cast.*;
import net.pistonpoek.magicalscepter.spell.cast.transformer.DelayCastTransformer;
import net.pistonpoek.magicalscepter.spell.cast.transformer.LineCastTransformer;
import net.pistonpoek.magicalscepter.spell.cast.transformer.RotateCastTransformer;
import net.pistonpoek.magicalscepter.spell.effect.*;
import net.pistonpoek.magicalscepter.spell.effect.projectile.*;

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
        register(registry, MAGICAL_ATTACK_KEY, Spell.builder(100, 32,
                        textOf("magical_attack"))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F)))
                    .addEffect(new DragonFireballSpellProjectile()).build())
        );
        register(registry, MAGICAL_RESISTANCE_KEY, Spell.builder(100, 32,
                        Text.translatable(Util.createTranslationKey("effect", Identifier.ofVanilla("resistance"))))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F)))
                    .addEffect(new ApplyMobEffectSpellEffect(
                            RegistryEntryList.of(StatusEffects.RESISTANCE),
                            ConstantFloatProvider.create(1000.0F),
                            ConstantFloatProvider.create(400.0f),
                            ConstantFloatProvider.create(1.0F),
                            ConstantFloatProvider.create(3.0F))).build())
        );

        register(registry, BLAZE_SMALL_FIREBALL_KEY, Spell.builder(100, 32,
                        textOf("small_fireballs"))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_BLAZE_SHOOT),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F)))
                    .addEffect(new SmallFireballSpellProjectile()).build())
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_BLAZE_SHOOT),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F)))
                    .addEffect(new SmallFireballSpellProjectile())
                    .addTransformer(new DelayCastTransformer(6)).build())
            .addCast(SpellCast.builder()
                        .addEffect(new PlaySoundSpellEffect(
                                RegistryEntry.of(SoundEvents.ENTITY_BLAZE_SHOOT),
                                ConstantFloatProvider.create(1.0F),
                                UniformFloatProvider.create(0.8F, 1.2F)))
                        .addEffect(new SmallFireballSpellProjectile())
                    .addTransformer(new DelayCastTransformer(12)).build())
        );
        register(registry, BLAZE_FIRE_RESISTANCE_KEY, Spell.builder(100, 32,
                        Text.translatable(Util.createTranslationKey("effect", Identifier.ofVanilla("fire_resistance"))))
            .addCast(SpellCast.builder()
                    .addEffect(new ApplyMobEffectSpellEffect(
                            RegistryEntryList.of(StatusEffects.FIRE_RESISTANCE),
                            ConstantFloatProvider.create(1000.0F),
                            ConstantFloatProvider.create(400.0f),
                            ConstantFloatProvider.create(1.0F),
                            ConstantFloatProvider.create(3.0F))).build())
        );

        register(registry, BREEZE_WIND_CHARGE_KEY, Spell.builder(100, 32,
                        textOf("wind_charge"))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_BREEZE_SHOOT),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F)))
                    .addEffect(new WindChargeSpellProjectile()).build())
        );
        register(registry, BREEZE_JUMP_KEY, Spell.builder(100, 32,
                        textOf("breeze_jump"))
            .addCast(SpellCast.builder()
                    .addEffect(new ApplyMobEffectSpellEffect(
                            RegistryEntryList.of(StatusEffects.JUMP_BOOST),
                            ConstantFloatProvider.create(1000.0F),
                            ConstantFloatProvider.create(400.0f),
                            ConstantFloatProvider.create(1.0F),
                            ConstantFloatProvider.create(3.0F))).build())
        );

        register(registry, DRAGON_FIREBALL_KEY, Spell.builder(100, 32,
                        textOf("dragon_fireball"))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_ENDER_DRAGON_SHOOT),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F)))
                    .addEffect(new DragonFireballSpellProjectile()).build())
        );
        register(registry, DRAGON_PROTECT_KEY, Spell.builder(100, 32,
                        textOf("dragon_growl"))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_ENDER_DRAGON_GROWL),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F))).build())
        );

        register(registry, EVOKER_FANG_LINE_KEY, Spell.builder(100, 32,
                        textOf("fang_line"))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_EVOKER_CAST_SPELL),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F))).build())
            .addCast(SpellCast.builder()
                    .addEffect(new SummonEntitySpellEffect(
                            RegistryEntryList.of(Registries.ENTITY_TYPE.getEntry(EntityType.EVOKER_FANGS)),
                            false, 4.0))
                    .addTransformer(LineCastTransformer.builder(16,
                            new RelativePositionSource(new Vec3d(0, 0, 1.25),
                                    new EntityPositionSource(EntityPositionSource.Anchor.EYES),
                                    new RelativeRotationSource(0, 0)),
                            new RelativePositionSource(new Vec3d(0, 0, 20),
                                    new EntityPositionSource(EntityPositionSource.Anchor.EYES),
                                    new RelativeRotationSource(0, 0))).stepDelay(1).build())
                    .addTransformer(new RotateCastTransformer(new RelativeRotationSource(0, 90))).build())
        );
        register(registry, EVOKER_FANG_CIRCLE_KEY, Spell.builder(100, 32,
                        textOf("fang_circle"))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_EVOKER_CAST_SPELL),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F))).build())
        );

        register(registry, GHAST_FIREBALL_KEY, Spell.builder(100, 32,
                        textOf("fireball"))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_GHAST_SHOOT),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F)))
                    .addEffect(new FireballSpellProjectile()).build())
        );
        register(registry, GHAST_REGENERATION_KEY, Spell.builder(100, 32,
                        Text.translatable(Util.createTranslationKey("effect", Identifier.ofVanilla("regeneration"))))
            .addCast(SpellCast.builder()
                    .addEffect(new ApplyMobEffectSpellEffect(
                            RegistryEntryList.of(StatusEffects.REGENERATION),
                            ConstantFloatProvider.create(1000.0F),
                            ConstantFloatProvider.create(400.0f),
                            ConstantFloatProvider.create(1.0F),
                            ConstantFloatProvider.create(3.0F))).build())
        );

        register(registry, GUARDIAN_BEAM_KEY, Spell.builder(100, 32,
                        textOf("guardian_beam"))
            .addCast(SpellCast.builder().build())
        );
        register(registry, GUARDIAN_HASTE_KEY, Spell.builder(100, 32,
                        Text.translatable(Util.createTranslationKey("effect", Identifier.ofVanilla("haste"))))
            .addCast(SpellCast.builder()
                    .addEffect(new ApplyMobEffectSpellEffect(
                            RegistryEntryList.of(StatusEffects.HASTE),
                            ConstantFloatProvider.create(1000.0F),
                            ConstantFloatProvider.create(400.0f),
                            ConstantFloatProvider.create(1.0F),
                            ConstantFloatProvider.create(3.0F))).build())
        );

        register(registry, SHULKER_BULLET_KEY, Spell.builder(100, 32,
                        textOf("shulker_bullet"))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_SHULKER_SHOOT),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F)))
                    .addEffect(new ShulkerBulletSpellProjectile()).build())
        );
        register(registry, SHULKER_TELEPORT_KEY, Spell.builder(100, 32,
                        textOf("teleport"))
            .addCast(SpellCast.builder()
                    .addEffect(new RandomTeleportSpellEffect(
                            true, 16.0))
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_SHULKER_TELEPORT),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F))).build())
        );

        register(registry, WARDEN_SONIC_BOOM_KEY, Spell.builder(100, 32,
                        textOf("sonic_boom"))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_WARDEN_SONIC_BOOM),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F))).build())
            .addCast(SpellCast.builder()
                    .addTransformer(LineCastTransformer.builder(15,
                                new RelativePositionSource(new Vec3d(0, 0, 1.25),
                                        new EntityPositionSource(EntityPositionSource.Anchor.EYES),
                                        new RelativeRotationSource(0, 0)),
                                new RelativePositionSource(new Vec3d(0, 0, 20),
                                        new EntityPositionSource(EntityPositionSource.Anchor.EYES),
                                        new RelativeRotationSource(0, 0))).build()).build())
//                        SpellCast.builder().addEffect(
//                                new SpawnParticlesSpellEffect(
//                                        ParticleTypes.SONIC_BOOM,
//// TODO update SpawnParticleSpellEffect
//                                        ))

        );
        register(registry, WARDEN_STABILITY_KEY, Spell.builder(100, 32,
                        Text.translatable(Util.createTranslationKey("effect", ModIdentifier.of("stability"))))
            .addCast(SpellCast.builder()
                    .addEffect(new ApplyMobEffectSpellEffect(
                            RegistryEntryList.of(ModStatusEffects.STABILITY),
                            ConstantFloatProvider.create(1000.0F),
                            ConstantFloatProvider.create(400.0f),
                            ConstantFloatProvider.create(1.0F),
                            ConstantFloatProvider.create(3.0F))).build())
        );

        register(registry, WITHER_SKULL_KEY, Spell.builder(100, 32,
                        textOf("wither_skull"))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_WITHER_SHOOT),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F)))
                    .addEffect(new WitherSkullSpellProjectile()).build())
        );
        register(registry, WITHER_SHIELD_KEY, Spell.builder(100, 32,
                        textOf("wither_shield"))
            .addCast(SpellCast.builder().build())
        );
    }

    private static void register(Registerable<Spell> registry, RegistryKey<Spell> key, Spell.Builder builder) {
        SPELL_KEYS.add(key);
        registry.register(key, builder.build());
    }
    
    private static Text textOf(String nameKey) {
        return Text.translatable(Util.createTranslationKey("spell", ModIdentifier.of(nameKey)));
    }
}
