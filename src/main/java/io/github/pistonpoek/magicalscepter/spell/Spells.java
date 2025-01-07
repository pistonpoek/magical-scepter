package io.github.pistonpoek.magicalscepter.spell;

import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import io.github.pistonpoek.magicalscepter.spell.cast.SpellCast;
import io.github.pistonpoek.magicalscepter.spell.cast.transformer.*;
import io.github.pistonpoek.magicalscepter.spell.effect.*;
import io.github.pistonpoek.magicalscepter.spell.rotation.*;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potions;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import io.github.pistonpoek.magicalscepter.entity.effect.ModStatusEffects;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.spell.position.EntityPositionSource;
import io.github.pistonpoek.magicalscepter.spell.position.MixedPositionSource;
import io.github.pistonpoek.magicalscepter.spell.position.RandomPositionSource;
import io.github.pistonpoek.magicalscepter.spell.position.RelativePositionSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Spells {
    public static final List<RegistryKey<Spell>> SPELL_KEYS = new ArrayList<>();
    public static final RegistryKey<Spell> MAGICAL_ATTACK_KEY = of("magical_attack");
    public static final RegistryKey<Spell> MAGICAL_RESISTANCE_KEY = of("magical_resistance");
    public static final RegistryKey<Spell> BLAZE_SMALL_FIREBALL_KEY = of("blaze_small_fireball");
    public static final RegistryKey<Spell> BLAZE_FIRE_RESISTANCE_KEY = of("blaze_fire_resistance");
    public static final RegistryKey<Spell> BREEZE_WIND_CHARGE_KEY = of("breeze_wind_charge");
    public static final RegistryKey<Spell> BREEZE_JUMP_KEY = of("breeze_jump");
    public static final RegistryKey<Spell> DRAGON_FIREBALL_KEY = of("dragon_fireball");
    public static final RegistryKey<Spell> DRAGON_GROWL_KEY = of("dragon_growl");
    public static final RegistryKey<Spell> EVOKER_FANG_LINE_KEY = of("evoker_fang_line");
    public static final RegistryKey<Spell> EVOKER_FANG_CIRCLE_KEY = of("evoker_fang_circle");
    public static final RegistryKey<Spell> GHAST_FIREBALL_KEY = of("ghast_fireball");
    public static final RegistryKey<Spell> GHAST_REGENERATION_KEY = of("ghast_regeneration");
    public static final RegistryKey<Spell> GUARDIAN_BOLT_KEY = of("guardian_bolt");
    public static final RegistryKey<Spell> GUARDIAN_HASTE_KEY = of("guardian_haste");
    public static final RegistryKey<Spell> SHULKER_BULLET_KEY = of("shulker_bullet");
    public static final RegistryKey<Spell> SHULKER_TELEPORT_KEY = of("shulker_teleport");
    public static final RegistryKey<Spell> WARDEN_SONIC_BOOM_KEY = of("warden_sonic_boom");
    public static final RegistryKey<Spell> WARDEN_STABILITY_KEY = of("warden_stability");
    public static final RegistryKey<Spell> WITHER_SKULL_KEY = of("wither_skull");
    public static final RegistryKey<Spell> WITHER_REPULSION_KEY = of("wither_repulsion");

    private static RegistryKey<Spell> of(String id) {
        return RegistryKey.of(ModRegistryKeys.SPELL, ModIdentifier.of(id));
    }

    public static void bootstrap(Registerable<Spell> registry) {
        RegistryEntryLookup<DamageType> damageTypeLookup = registry.getRegistryLookup(RegistryKeys.DAMAGE_TYPE);
        RegistryEntryLookup<EntityType<?>> entityTypeLookup = registry.getRegistryLookup(RegistryKeys.ENTITY_TYPE);
        Function<EntityType<?>, RegistryEntry.Reference<EntityType<?>>> entityTypeReferenceFunction = entityType ->
                entityTypeLookup.getOrThrow(RegistryKey.of(RegistryKeys.ENTITY_TYPE, EntityType.getId(entityType)));

        register(registry, MAGICAL_ATTACK_KEY, Spell.builder(30,
                        textOf("magical_attack"))
            .addCast(SpellCast.builder()
                    .addTransformer(
                            RayCastTransformer.builder(
                                    RayCastTransformer.Target.ENTITY, 16.0
                            ).build()
                    )
                    .addEffect(
                            new DamageSpellEffect(
                                    UniformFloatProvider.create(4.0F, 6.0F),
                                    damageTypeLookup.getOrThrow(DamageTypes.INDIRECT_MAGIC)
                            )
                    )
            )
            .addCast(SpellCast.builder()
                    .addTransformer(
                            MoveCastTransformer.builder(
                                    RelativePositionSource.builder(new Vec3d(0, 0, 16.0F)).build()
                            ).build()
                    )
                    .addTransformer(
                            LineCastTransformer.builder(24,
                                    RelativePositionSource.builder(0, 0, 0.8).position(
                                            EntityPositionSource.builder(
                                                    EntityPositionSource.Anchor.EYES
                                            ).build()
                                    ).build()
                            ).build()
                    )
                    .addEffect(
                            SpawnParticleSpellEffect.builder(ParticleTypes.WITCH)
                                    .speed(ConstantFloatProvider.create(1.0F))
                                    .build()
                    )
            )
        );
        register(registry, MAGICAL_RESISTANCE_KEY, Spell.builder(30,
                        Text.translatable(Util.createTranslationKey("effect", Identifier.ofVanilla("resistance"))))
            .addCast(SpellCast.builder()
                    .addEffect(new ApplyMobEffectSpellEffect(
                            RegistryEntryList.of(StatusEffects.RESISTANCE),
                            ConstantFloatProvider.create(10.0F),
                            ConstantFloatProvider.create(12.5F),
                            ConstantFloatProvider.create(1.0F),
                            ConstantFloatProvider.create(1.0F)))
            )
        );

        register(registry, BLAZE_SMALL_FIREBALL_KEY, Spell.builder(40,
                        textOf("small_fireballs"))
            .addCast(SpellCast.builder()
                    .addTransformer(RepeatCastTransformer.builder(3).stepDelay(6).build())
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_BLAZE_SHOOT),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F))
                    )
            )
            .addCast(SpellCast.builder()
                    .addTransformer(RepeatCastTransformer.builder(3).stepDelay(6).build())
                    .addTransformer(
                            RotateCastTransformer.builder(
                                    new EntityRotationSource(0, 0)
                            ).build()
                    )
                    .addTransformer(
                            MoveCastTransformer.builder(
                                    EntityPositionSource.builder(
                                            EntityPositionSource.Anchor.EYES
                                    ).build()
                            ).build()
                    )
                    .addTransformer(
                            MoveCastTransformer.builder(
                                    RelativePositionSource.builder(0, 0, 0.8).build()
                            ).build()
                    )
                    .addTransformer(
                            RotateCastTransformer.builder(
                                    new RandomRotationSource(0.0F, 12.0F)
                            ).build()
                    )
                    .addTransformer(
                            RotateCastTransformer.builder(
                                    new RandomRotationSource(0.0F, -12.0F)
                            ).build()
                    )
                    .addEffect(
                            SummonEntitySpellEffect.builder(
                                    entityTypeReferenceFunction.apply(EntityType.SMALL_FIREBALL)
                            ).addEffect(
                                    new MoveSpellEffect(ConstantFloatProvider.create(1.0F), false)
                            ).build()
                    )
            )
        );
        register(registry, BLAZE_FIRE_RESISTANCE_KEY, Spell.builder(40,
                        Text.translatable(Util.createTranslationKey("effect", Identifier.ofVanilla("fire_resistance"))))
            .addCast(SpellCast.builder()
                    .addEffect(new ApplyMobEffectSpellEffect(
                            RegistryEntryList.of(StatusEffects.FIRE_RESISTANCE),
                            ConstantFloatProvider.create(10.0F),
                            ConstantFloatProvider.create(12.5F),
                            ConstantFloatProvider.create(0.0F),
                            ConstantFloatProvider.create(0.0F)))
            )
        );

        register(registry, BREEZE_WIND_CHARGE_KEY, Spell.builder(24,
                        textOf("wind_charge"))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_BREEZE_SHOOT),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F)))
            )
            .addCast(SpellCast.builder()
                    .addTransformer(
                            MoveCastTransformer.builder(
                                    RelativePositionSource.builder(0, 0, 0.8).build()
                            ).build()
                    )
                    .addEffect(
                            SummonEntitySpellEffect.builder(
                                    entityTypeReferenceFunction.apply(EntityType.WIND_CHARGE)
                            ).addEffect(
                                    new MoveSpellEffect(ConstantFloatProvider.create(1.5F), false)
                            ).build()
                    )
            )
        );
        register(registry, BREEZE_JUMP_KEY, Spell.builder(24,
                        textOf("breeze_jump"))
            .addCast(SpellCast.builder()
                    .addTransformer(new FilterCastTransformer(
                            LootContextPredicate.create(
                                    EntityPropertiesLootCondition.builder(
                                            LootContext.EntityTarget.THIS,
                                            EntityPredicate.Builder.create()
                                                    .flags(EntityFlagsPredicate.Builder.create()
                                                            .onGround(true))
                                    ).build()
                            )
                    ))
                    .addTransformer(RotateCastTransformer.builder(
                            new FacingLocationRotationSource(
                                    MixedPositionSource.builder()
                                            .yPosition(RelativePositionSource.builder(0, 0.5, 0)
                                                    .position(EntityPositionSource.builder(EntityPositionSource.Anchor.EYES).build())
                                                    .rotation(new AbsoluteRotationSource(0, 0))
                                                    .build())
                                            .xPosition(RelativePositionSource.builder(new Vec3d(0, 0, 1)).build())
                                            .zPosition(RelativePositionSource.builder(new Vec3d(0, 0, 1)).build()).build())
                            ).build()
                    )
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_BREEZE_JUMP),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F)))
                    .addEffect(new MoveSpellEffect(
                            ConstantFloatProvider.create(2.0F),
                            false
                    ))
            )
            .addCast(SpellCast.builder()
                    .addTransformer(new FilterCastTransformer(
                            LootContextPredicate.create(
                                    EntityPropertiesLootCondition.builder(
                                            LootContext.EntityTarget.THIS,
                                            EntityPredicate.Builder.create()
                                                    .flags(EntityFlagsPredicate.Builder.create()
                                                            .onGround(false))
                                    ).build()
                            )
                    ))
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_BREEZE_LAND),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F)
                    ))
                    .addEffect(new ApplyMobEffectSpellEffect(
                            RegistryEntryList.of(StatusEffects.SLOW_FALLING),
                            ConstantFloatProvider.create(0.8F),
                            ConstantFloatProvider.create(1.2F),
                            ConstantFloatProvider.create(0.0F),
                            ConstantFloatProvider.create(0.0F)
                    ))
            )
        );

        register(registry, DRAGON_FIREBALL_KEY, Spell.builder(100,
                        textOf("dragon_fireball"))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_ENDER_DRAGON_SHOOT),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F)))
            )
            .addCast(SpellCast.builder()
                    .addTransformer(
                            MoveCastTransformer.builder(
                                    RelativePositionSource.builder(0, 0, 0.8).build()
                            ).build()
                    )
                    .addEffect(
                            SummonEntitySpellEffect.builder(
                                    entityTypeReferenceFunction.apply(EntityType.DRAGON_FIREBALL)
                            ).addEffect(
                                    new MoveSpellEffect(ConstantFloatProvider.create(1.0F), false)
                            ).build()
                    )
            )
        );
        NbtCompound areaEffectCloudNbtCompound = new NbtCompound();
        areaEffectCloudNbtCompound.putInt("Duration", 600);
        areaEffectCloudNbtCompound.putInt("Radius", 3);
        areaEffectCloudNbtCompound.putDouble("RadiusPerTick", 0.00666666666);
        NbtElement nbtElement = PotionContentsComponent.CODEC.encodeStart(NbtOps.INSTANCE, new PotionContentsComponent(Potions.HARMING)).getOrThrow();
        areaEffectCloudNbtCompound.put("potion_contents", nbtElement);
        areaEffectCloudNbtCompound.put("Particle", ParticleTypes.TYPE_CODEC.encodeStart(NbtOps.INSTANCE, ParticleTypes.DRAGON_BREATH).getOrThrow());
        register(registry, DRAGON_GROWL_KEY, Spell.builder(100,
                        textOf("dragon_growl"))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_ENDER_DRAGON_GROWL),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F))
                    )
            )
            .addCast(SpellCast.builder()
                    .addEffect(
                            SummonEntitySpellEffect.builder(
                                    entityTypeReferenceFunction.apply(EntityType.AREA_EFFECT_CLOUD)
                            ).nbt(areaEffectCloudNbtCompound).build()
                    )
                    .addTransformer(
                            RayCastTransformer.builder(RayCastTransformer.Target.BLOCK, 8)
                                    .require(true)
                                    .build()
                    )
            )
            .addCast(SpellCast.builder()
                    .addEffect(
                            SpawnParticleSpellEffect.builder(ParticleTypes.DRAGON_BREATH)
                                    .speed(ConstantFloatProvider.ZERO)
                                    .build()
                    )
                    .addTransformer(
                            RepeatCastTransformer.builder(6).stepDelay(4).build()
                    )
                    .addTransformer(
                            RotateCastTransformer.builder(
                                    new RandomRotationSource(45, 0)
                            ).build()
                    )
                    .addTransformer(
                            RotateCastTransformer.builder(
                                    new RandomRotationSource(0, 90)
                            ).build()
                    )
                    .addTransformer(
                            MoveCastTransformer.builder(
                                    RelativePositionSource.builder(0, 0, 8).build()
                            ).build()
                    )
                    .addTransformer(
                            LineCastTransformer.builder(12,
                                    EntityPositionSource.builder(EntityPositionSource.Anchor.EYES).build()
                            ).stepDelay(1).build()
                    )
            )
        );

        register(registry, EVOKER_FANG_LINE_KEY, Spell.builder(40,
                        textOf("fang_line"))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_EVOKER_CAST_SPELL),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F)))
            )
            .addCast(SpellCast.builder()
                    .addEffect(SummonEntitySpellEffect.builder(entityTypeReferenceFunction.apply(EntityType.EVOKER_FANGS)).build())
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
                            SurfaceCastTransformer.builder(8).build()
                    )
                    .addTransformer(
                            RotateCastTransformer.builder(
                                    new RelativeRotationSource(0, 90)
                            ).build()
                    )
            )
        );
        register(registry, EVOKER_FANG_CIRCLE_KEY, Spell.builder(40,
                        textOf("fang_circle"))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_EVOKER_CAST_SPELL),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F)))
            )
            .addCast(SpellCast.builder()
                    .addEffect(SummonEntitySpellEffect.builder(entityTypeReferenceFunction.apply(EntityType.EVOKER_FANGS)).build())
                    .addTransformer(
                            MoveCastTransformer.builder(
                                    EntityPositionSource.builder(EntityPositionSource.Anchor.FEET).build()
                            ).build()
                    )
                    .addTransformer(
                            RotateCastTransformer.builder(
                                    MixedRotationSource.builder()
                                            .pitchRotation(new AbsoluteRotationSource(0, 0))
                                            .build()
                            ).build()
                    )
                    .addTransformer(
                            CircleCastTransformer.builder(
                                    RelativePositionSource.builder(new Vec3d(0, 0, 1.5f)).build(), 5
                            ).build()
                    )
                    .addTransformer(
                            SurfaceCastTransformer.builder(8).build()
                    )
            )
            .addCast(SpellCast.builder()
                    .addEffect(SummonEntitySpellEffect.builder(entityTypeReferenceFunction.apply(EntityType.EVOKER_FANGS)).build())
                    .addTransformer(DelayCastTransformer.builder(3).build())
                    .addTransformer(
                            MoveCastTransformer.builder(
                                    EntityPositionSource.builder(EntityPositionSource.Anchor.FEET).build()
                            ).build()
                    )
                    .addTransformer(
                            RotateCastTransformer.builder(
                                    MixedRotationSource.builder()
                                            .pitchRotation(new AbsoluteRotationSource(0, 0))
                                            .yawRotation(new RelativeRotationSource(0, 72))
                                            .build()
                            ).build()
                    )
                    .addTransformer(
                            CircleCastTransformer.builder(
                                    RelativePositionSource.builder(new Vec3d(0, 0, 2.5f)).build(), 8
                            ).build()
                    )
                    .addTransformer(
                            SurfaceCastTransformer.builder(8).build()
                    )
            )
        );

        register(registry, GHAST_FIREBALL_KEY, Spell.builder(40,
                        textOf("fireball"))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_GHAST_SHOOT),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F)))
            )
            .addCast(SpellCast.builder()
                    .addTransformer(
                            MoveCastTransformer.builder(
                                    RelativePositionSource.builder(0, 0, 0.8).build()
                            ).build()
                    )
                    .addEffect(
                            SummonEntitySpellEffect.builder(
                                    entityTypeReferenceFunction.apply(EntityType.FIREBALL)
                            ).addEffect(
                                    new MoveSpellEffect(ConstantFloatProvider.create(1.0F), false)
                            ).build()
                    )
            )
        );
        register(registry, GHAST_REGENERATION_KEY, Spell.builder(40,
                        Text.translatable(Util.createTranslationKey("effect", Identifier.ofVanilla("regeneration"))))
            .addCast(SpellCast.builder()
                    .addEffect(new ApplyMobEffectSpellEffect(
                            RegistryEntryList.of(StatusEffects.REGENERATION),
                            ConstantFloatProvider.create(10.0F),
                            ConstantFloatProvider.create(12.5F),
                            ConstantFloatProvider.create(1.0F),
                            ConstantFloatProvider.create(1.0F)))
            )
        );

        register(registry, GUARDIAN_BOLT_KEY, Spell.builder(40,
                        textOf("guardian_bolt"))
            .addCast(SpellCast.builder()
                    .addTransformer(
                            MoveCastTransformer.builder(
                                    RelativePositionSource.builder(0, 0, 0.8).build()
                            ).build()
                    )
                    .addEffect(
                            SummonEntitySpellEffect.builder(
                                    entityTypeReferenceFunction.apply(ModEntityType.GUARDIAN_BOLT))
                            .addEffect(
                                    new MoveSpellEffect(ConstantFloatProvider.create(0.2F), false)
                            )
                            .build()
                    )
            )
        );
        register(registry, GUARDIAN_HASTE_KEY, Spell.builder(40,
                        Text.translatable(Util.createTranslationKey("effect", Identifier.ofVanilla("haste"))))
            .addCast(SpellCast.builder()
                    .addEffect(new ApplyMobEffectSpellEffect(
                            RegistryEntryList.of(StatusEffects.HASTE),
                            ConstantFloatProvider.create(20.0F),
                            ConstantFloatProvider.create(25.0F),
                            ConstantFloatProvider.create(1.0F),
                            ConstantFloatProvider.create(1.0F)))
            )
        );

        register(registry, SHULKER_BULLET_KEY, Spell.builder(40,
                        textOf("shulker_bullet"))
            .addCast(SpellCast.builder()
                    .addTransformer(
                            RayCastTransformer.builder(
                                    RayCastTransformer.Target.ENTITY, 16.0
                            ).require(true)
                            .build()
                    )
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_SHULKER_SHOOT),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F)))
            )
            .addCast(SpellCast.builder()
                    .addTransformer(
                            RayCastTransformer.builder(
                                RayCastTransformer.Target.ENTITY, 16.0
                            ).require(true)
                            .build()
                    )
                    .addTransformer(
                            MoveCastTransformer.builder(
                                    RelativePositionSource.builder(0, 0, 0.8).build()
                            ).build()
                    )
                    .addEffect(
                            SummonEntitySpellEffect.builder(
                                    entityTypeReferenceFunction.apply(EntityType.SHULKER_BULLET)
                            ).build()
                    )
            )
        );
        register(registry, SHULKER_TELEPORT_KEY, Spell.builder(40,
                        textOf("teleport"))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_SHULKER_TELEPORT),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F))
                    )
            )
            .addCast(SpellCast.builder()
                    .addEffect(new TeleportSpellEffect())
                    .addTransformer(RepeatCastTransformer.builder(64).build())
                    .addTransformer(MoveCastTransformer.builder(
                            RandomPositionSource.builder(8, 4, 8).build()
                        ).build()
                    )
                    .addTransformer(SurfaceCastTransformer.builder(4).build())
            )
        );

        final double SONIC_BOOM_RANGE = 20.0;
        register(registry, WARDEN_SONIC_BOOM_KEY, Spell.builder(60,
                        textOf("sonic_boom"))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_WARDEN_SONIC_BOOM),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F))
                    )
            )
            .addCast(SpellCast.builder()
                    .addTransformer(
                            MoveCastTransformer.builder(
                                    RelativePositionSource.builder(0, 0, 0.8).build()
                            ).build()
                    )
                    .addTransformer(
                            LineCastTransformer.builder(15,
                                    RelativePositionSource.builder(0, 0, SONIC_BOOM_RANGE).build()
                            ).build()
                    )
                    .addEffect(
                            SpawnParticleSpellEffect.builder(ParticleTypes.SONIC_BOOM)
                                    .build()
                    )
            )
            .addCast(SpellCast.builder()
                    .addTransformer(
                            RayCastTransformer.builder(
                                    RayCastTransformer.Target.ENTITY,
                                    SONIC_BOOM_RANGE
                            ).build()
                    )
                    .addEffect(new DamageSpellEffect(
                            ConstantFloatProvider.create(10.0F),
                            damageTypeLookup.getOrThrow(DamageTypes.SONIC_BOOM)
                    ))
            )
            .addCast(SpellCast.builder()
                    .addTransformer(
                            RayCastTransformer.builder(
                                    RayCastTransformer.Target.ENTITY,
                                    SONIC_BOOM_RANGE
                            ).build()
                    )
                    .addTransformer(
                            RotateCastTransformer.builder(
                                    MixedRotationSource.builder()
                                            .yawRotation(new AbsoluteRotationSource(0.0F, -90.0F))
                                            .build()
                            ).build()
                    )
                    .addEffect(new MoveSpellEffect(
                            ConstantFloatProvider.create(0.5F),
                            true
                    ))
            )
            .addCast(SpellCast.builder()
                    .addTransformer(
                            RayCastTransformer.builder(
                                    RayCastTransformer.Target.ENTITY,
                                    SONIC_BOOM_RANGE
                            ).build()
                    )
                    .addTransformer(
                            RotateCastTransformer.builder(
                                    MixedRotationSource.builder()
                                            .pitchRotation(new AbsoluteRotationSource(0.0F, 0.0F))
                                    .build()
                            ).build()
                    )
                    .addEffect(new MoveSpellEffect(
                            ConstantFloatProvider.create(2.5F),
                            true
                    ))
            )
        );
        register(registry, WARDEN_STABILITY_KEY, Spell.builder(40,
                        Text.translatable(Util.createTranslationKey("effect", ModIdentifier.of("stability"))))
            .addCast(SpellCast.builder()
                    .addEffect(new ApplyMobEffectSpellEffect(
                            RegistryEntryList.of(ModStatusEffects.STABILITY),
                            ConstantFloatProvider.create(20.0F),
                            ConstantFloatProvider.create(25.0F),
                            ConstantFloatProvider.create(1.0F),
                            ConstantFloatProvider.create(1.0F)))
            )
        );

        register(registry, WITHER_SKULL_KEY, Spell.builder(40,
                        textOf("wither_skull"))
            .addCast(SpellCast.builder()
                    .addEffect(new PlaySoundSpellEffect(
                            RegistryEntry.of(SoundEvents.ENTITY_WITHER_SHOOT),
                            ConstantFloatProvider.create(1.0F),
                            UniformFloatProvider.create(0.8F, 1.2F)))
            )
            .addCast(SpellCast.builder()
                    .addTransformer(
                            MoveCastTransformer.builder(
                                    RelativePositionSource.builder(0, 0, 0.8).build()
                            ).build()
                    )
                    .addEffect(
                            SummonEntitySpellEffect.builder(
                                    entityTypeReferenceFunction.apply(EntityType.WITHER_SKULL)
                            ).addEffect(
                                    new MoveSpellEffect(ConstantFloatProvider.create(1.0F), false)
                            ).build()
                    )
            )
        );
        register(registry, WITHER_REPULSION_KEY, Spell.builder(40,
                        Text.translatable(Util.createTranslationKey("effect", ModIdentifier.of("repulsion"))))
                .addCast(SpellCast.builder()
                        .addEffect(new ApplyMobEffectSpellEffect(
                                RegistryEntryList.of(ModStatusEffects.REPULSION),
                                ConstantFloatProvider.create(10.0F),
                                ConstantFloatProvider.create(12.0F),
                                ConstantFloatProvider.create(0.0F),
                                ConstantFloatProvider.create(0.0F)))
                )
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
