package net.pistonpoek.magicalscepter.spell;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.pistonpoek.magicalscepter.entity.effect.ModStatusEffects;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.spell.cast.*;
import net.pistonpoek.magicalscepter.spell.cast.transformer.*;
import net.pistonpoek.magicalscepter.spell.effect.*;
import net.pistonpoek.magicalscepter.spell.effect.projectile.*;
import net.pistonpoek.magicalscepter.spell.position.EntityPositionSource;
import net.pistonpoek.magicalscepter.spell.position.RandomPositionSource;
import net.pistonpoek.magicalscepter.spell.position.RelativePositionSource;
import net.pistonpoek.magicalscepter.spell.rotation.AbsoluteRotationSource;
import net.pistonpoek.magicalscepter.spell.rotation.MixedRotationSource;
import net.pistonpoek.magicalscepter.spell.rotation.RelativeRotationSource;

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
        RegistryEntryLookup<DamageType> damageTypeLookup = registry.getRegistryLookup(RegistryKeys.DAMAGE_TYPE);

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
                    .addEffect(new SmallFireballSpellProjectile())
                    .addTransformer(
                            MoveCastTransformer.builder(
                                    RelativePositionSource.builder(0, 0, 0.8).build()
                            ).build()
                    ).build()
            )
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_BLAZE_SHOOT),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F)))
                    .addEffect(new SmallFireballSpellProjectile())
                    .addTransformer(DelayCastTransformer.builder(6).build())
                    .addTransformer(
                            MoveCastTransformer.builder(
                                    RelativePositionSource.builder(0, 0, 0.8).build()
                            ).build()
                    ).build()
            )
            .addCast(SpellCast.builder()
                        .addEffect(new PlaySoundSpellEffect(
                                RegistryEntry.of(SoundEvents.ENTITY_BLAZE_SHOOT),
                                ConstantFloatProvider.create(1.0F),
                                UniformFloatProvider.create(0.8F, 1.2F)))
                        .addEffect(new SmallFireballSpellProjectile())
                    .addTransformer(DelayCastTransformer.builder(12).build())
                    .addTransformer(
                            MoveCastTransformer.builder(
                                    RelativePositionSource.builder(0, 0, 0.8).build()
                            ).build()
                    ).build()
            )
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
                            UniformFloatProvider.create(0.8F, 1.2F))
                    ).build()
            )
            .addCast(SpellCast.builder()
                    .addEffect(new SummonEntitySpellEffect(
                            RegistryEntryList.of(Registries.ENTITY_TYPE.getEntry(EntityType.GHAST)), false
                    ))
                    .addTransformer(
                            TargetCastTransformer.builder(TargetCastTransformer.Target.BLOCK, 48.0)
                                    .require(false)
                                    .build()
                    ).build()
            )
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
                            RegistryEntryList.of(Registries.ENTITY_TYPE.getEntry(EntityType.EVOKER_FANGS)), false))
                    .addTransformer(
                            MoveCastTransformer.builder(
                                    RelativePositionSource.builder(0, 0, 1.25)
                                    .position(new EntityPositionSource(EntityPositionSource.Anchor.FEET))
                                    .rotation(MixedRotationSource.builder()
                                            .pitchRotation(new AbsoluteRotationSource(0, 0))
                                            .build()
                                    ).build()
                            ).build()
                    )
                    .addTransformer(
                            LineCastTransformer.builder(16,
                                    RelativePositionSource.builder(0, 0, 20)
                                    .position(new EntityPositionSource(EntityPositionSource.Anchor.FEET))
                                    .rotation(MixedRotationSource.builder()
                                            .pitchRotation(new AbsoluteRotationSource(0, 0)
                                            ).build()
                                    ).build()
                            ).stepDelay(1)
                            .build()
                    )
                    .addTransformer(
                            SurfaceCastTransformer.builder(1).position(
                                    RelativePositionSource.builder(0, 0, 20)
                                    .position(new EntityPositionSource(EntityPositionSource.Anchor.FEET))
                                    .rotation(MixedRotationSource.builder()
                                            .pitchRotation(new AbsoluteRotationSource(0, 0)
                                            ).build()
                                    ).build()
                            ).build()
                    )
                    .addTransformer(
                            RotateCastTransformer.builder(
                                    new RelativeRotationSource(0, 90)
                            ).build()
                    ).build()
            )
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
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_SHULKER_TELEPORT),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F)))
                    .build()
            )
            .addCast(SpellCast.builder()
                    .addEffect(new TeleportSpellEffect())
                    .addTransformer(MoveCastTransformer.builder(
                            RandomPositionSource.builder(16, 16, 16).build()
                        ).build()
                    ).build()
            )
        );

        register(registry, WARDEN_SONIC_BOOM_KEY, Spell.builder(100, 32,
                        textOf("sonic_boom"))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_WARDEN_SONIC_BOOM),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F))
                    ).build()
            )
            .addCast(SpellCast.builder()
                    .addEffect(new SpawnParticleSpellEffect(ParticleTypes.SONIC_BOOM, ConstantFloatProvider.ZERO))
                    .addTransformer(
                            LineCastTransformer.builder(15,
                                    RelativePositionSource.builder(0, 0, 20).build()
                            ).build()
                    ).build()
            )
            .addCast(SpellCast.builder()
                    .addEffect(new MoveSpellEffect(
                            ConstantFloatProvider.create(2.0f), // TODO add vertical and horizontal knockback component like sonicboom task applies.
                            true
                    ))
                    .addEffect(new DamageSpellEffect(
                            ConstantFloatProvider.create(10.0f),
                            damageTypeLookup.getOrThrow(DamageTypes.SONIC_BOOM)
                    ))
                    .addTransformer(
                        TargetCastTransformer.builder(
                            TargetCastTransformer.Target.ENTITY,
                                20.0
                        ).build()
                    ).build()
            )
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
