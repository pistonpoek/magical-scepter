package io.github.pistonpoek.magicalscepter.spell;

import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import io.github.pistonpoek.magicalscepter.entity.effect.ModStatusEffects;
import io.github.pistonpoek.magicalscepter.entity.spell.SpellGuardianBeamEntity;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.spell.cast.SpellCast;
import io.github.pistonpoek.magicalscepter.spell.cast.transformer.*;
import io.github.pistonpoek.magicalscepter.spell.effect.*;
import io.github.pistonpoek.magicalscepter.spell.position.*;
import io.github.pistonpoek.magicalscepter.spell.rotation.*;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityFlagsPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.PowerParticleOption;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Spells {
    public static final List<ResourceKey<Spell>> KEYS = new ArrayList<>();
    public static final ResourceKey<Spell> MAGICAL_ATTACK_KEY = of("magical_attack");
    public static final ResourceKey<Spell> MAGICAL_RESISTANCE_KEY = of("magical_resistance");
    public static final ResourceKey<Spell> BLAZE_FIRE_CHARGES = of("blaze_fire_charges");
    public static final ResourceKey<Spell> BLAZE_FIRE_RESISTANCE_KEY = of("blaze_fire_resistance");
    public static final ResourceKey<Spell> BREEZE_WIND_CHARGE_KEY = of("breeze_wind_charge");
    public static final ResourceKey<Spell> BREEZE_JUMP_KEY = of("breeze_jump");
    public static final ResourceKey<Spell> DRAGON_FIREBALL_KEY = of("dragon_fireball");
    public static final ResourceKey<Spell> DRAGON_GROWL_KEY = of("dragon_growl");
    public static final ResourceKey<Spell> EVOKER_FANG_LINE_KEY = of("evoker_fang_line");
    public static final ResourceKey<Spell> EVOKER_FANG_CIRCLE_KEY = of("evoker_fang_circle");
    public static final ResourceKey<Spell> GHAST_FIREBALL_KEY = of("ghast_fireball");
    public static final ResourceKey<Spell> GHAST_REGENERATION_KEY = of("ghast_regeneration");
    public static final ResourceKey<Spell> GUARDIAN_BEAM_KEY = of("guardian_beam");
    public static final ResourceKey<Spell> GUARDIAN_HASTE_KEY = of("guardian_haste");
    public static final ResourceKey<Spell> SHULKER_BULLET_KEY = of("shulker_bullet");
    public static final ResourceKey<Spell> SHULKER_TELEPORT_KEY = of("shulker_teleport");
    public static final ResourceKey<Spell> WARDEN_SONIC_BOOM_KEY = of("warden_sonic_boom");
    public static final ResourceKey<Spell> WARDEN_STABILITY_KEY = of("warden_stability");
    public static final ResourceKey<Spell> WITHER_SKULL_KEY = of("wither_skull");
    public static final ResourceKey<Spell> WITHER_REPULSION_KEY = of("wither_repulsion");

    private static ResourceKey<Spell> of(String identifier) {
        return ResourceKey.create(ModRegistryKeys.SPELL, ModIdentifier.of(identifier));
    }

    public static void bootstrap(BootstrapContext<Spell> registry) {
        HolderGetter<DamageType> damageTypeLookup = registry.lookup(Registries.DAMAGE_TYPE);
        HolderGetter<EntityType<?>> entityTypeLookup = registry.lookup(Registries.ENTITY_TYPE);
        Function<EntityType<?>, Holder.Reference<EntityType<?>>> entityTypeReferenceFunction = entityType ->
                entityTypeLookup.getOrThrow(ResourceKey.create(Registries.ENTITY_TYPE, EntityType.getKey(entityType)));

        final PositionSource PROJECTILE_BASE =
                RelativePositionSource.builder(0, 0, 0.8).position(
                        EntityPositionSource.builder(
                                EntityPositionSource.Anchor.EYES
                        ).build()
                ).build();

        final double MAGICAL_ATTACK_RANGE = 8.0;
        final double DRAGON_GROWL_RANGE = 8.0;
        final double GUARDIAN_BEAM_RANGE = SpellGuardianBeamEntity.MAX_DISTANCE;
        final double SHULKER_BULLET_RANGE = 24.0;
        final double SONIC_BOOM_RANGE = 20.0;

        register(registry, MAGICAL_ATTACK_KEY, Spell.builder(30,
                        Component.translatable(getTranslationKey(MAGICAL_ATTACK_KEY)))
                .addCast(SpellCast.builder()
                        .addTransformer(new AnchorCastTransformer())
                        .addTransformer(
                                DelayCastTransformer.builder((int) (MAGICAL_ATTACK_RANGE * 0.5)).build()
                        )
                        .addTransformer(
                                RayCastTransformer.builder(
                                        RayCastTransformer.Target.ENTITY, MAGICAL_ATTACK_RANGE
                                ).build()
                        )
                        .addEffect(
                                new DamageSpellEffect(
                                        ConstantFloat.of(5.0F),
                                        damageTypeLookup.getOrThrow(DamageTypes.INDIRECT_MAGIC)
                                )
                        )
                )
                .addCast(SpellCast.builder()
                        .addTransformer(MoveCastTransformer.builder(PROJECTILE_BASE).build())
                        .addTransformer(
                                LineCastTransformer.builder((int) (MAGICAL_ATTACK_RANGE * 2),
                                        RelativePositionSource.builder(new Vec3(0, 0, MAGICAL_ATTACK_RANGE)).build()
                                ).stepDelay(0.5F).build()
                        )
                        .addEffect(
                                SpawnParticleSpellEffect.builder(ParticleTypes.WITCH)
                                        .speed(ConstantFloat.of(1.0F))
                                        .build()
                        )
                )
        );
        register(registry, MAGICAL_RESISTANCE_KEY, Spell.builder(30,
                        MobEffects.RESISTANCE.value().getDisplayName())
                .addCast(SpellCast.builder()
                        .addEffect(new ApplyMobEffectSpellEffect(
                                HolderSet.direct(MobEffects.RESISTANCE),
                                UniformFloat.of(10.0F, 12.5F),
                                ConstantFloat.of(1.0F)
                        ))
                )
        );

        register(registry, BLAZE_FIRE_CHARGES, Spell.builder(40,
                        Component.translatable(getTranslationKey(BLAZE_FIRE_CHARGES)))
                .addCast(SpellCast.builder()
                        .addTransformer(RepeatCastTransformer.builder(3).stepDelay(6.0F).build())
                        .addEffect(new PlaySoundSpellEffect(
                                Holder.direct(SoundEvents.BLAZE_SHOOT),
                                ConstantFloat.of(1.0F),
                                UniformFloat.of(0.8F, 1.2F))
                        )
                )
                .addCast(SpellCast.builder()
                        .addTransformer(MoveCastTransformer.builder(PROJECTILE_BASE).build())
                        .addTransformer(RepeatCastTransformer.builder(3).stepDelay(6.0F).build())
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
                                        entityTypeReferenceFunction.apply(ModEntityType.SPELL_FIRE_CHARGE)
                                ).addEffect(
                                        new MoveSpellEffect(ConstantFloat.of(1.0F), false)
                                ).build()
                        )
                )
        );
        register(registry, BLAZE_FIRE_RESISTANCE_KEY, Spell.builder(40,
                        MobEffects.FIRE_RESISTANCE.value().getDisplayName())
                .addCast(SpellCast.builder()
                        .addEffect(new ApplyMobEffectSpellEffect(
                                HolderSet.direct(MobEffects.FIRE_RESISTANCE),
                                UniformFloat.of(10.0F, 12.5F),
                                ConstantFloat.of(0.0F)
                        ))
                )
        );

        register(registry, BREEZE_WIND_CHARGE_KEY, Spell.builder(24,
                        Component.translatable(getTranslationKey(BREEZE_WIND_CHARGE_KEY)))
                .addCast(SpellCast.builder()
                        .addEffect(new PlaySoundSpellEffect(
                                Holder.direct(SoundEvents.BREEZE_SHOOT),
                                ConstantFloat.of(1.0F),
                                UniformFloat.of(0.8F, 1.2F)))
                )
                .addCast(SpellCast.builder()
                        .addTransformer(MoveCastTransformer.builder(PROJECTILE_BASE).build())
                        .addEffect(
                                SummonEntitySpellEffect.builder(
                                        entityTypeReferenceFunction.apply(EntityType.WIND_CHARGE)
                                ).addEffect(
                                        new MoveSpellEffect(ConstantFloat.of(1.5F), false)
                                ).build()
                        )
                )
        );
        register(registry, BREEZE_JUMP_KEY, Spell.builder(24,
                        Component.translatable(getTranslationKey(BREEZE_JUMP_KEY)))
                .addCast(SpellCast.builder()
                        .addTransformer(new FilterCastTransformer(
                                ContextAwarePredicate.create(
                                        LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS,
                                                EntityPredicate.Builder.entity()
                                                        .flags(EntityFlagsPredicate.Builder.flags()
                                                                .setOnGround(true))
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
                                                        .xPosition(RelativePositionSource.builder(new Vec3(0, 0, 1)).build())
                                                        .zPosition(RelativePositionSource.builder(new Vec3(0, 0, 1)).build()).build())
                                ).build()
                        )
                        .addEffect(new PlaySoundSpellEffect(
                                Holder.direct(SoundEvents.BREEZE_JUMP),
                                ConstantFloat.of(1.0F),
                                UniformFloat.of(0.8F, 1.2F)))
                        .addEffect(new MoveSpellEffect(
                                ConstantFloat.of(2.0F),
                                false
                        ))
                )
                .addCast(SpellCast.builder()
                        .addTransformer(new FilterCastTransformer(
                                ContextAwarePredicate.create(
                                        LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS,
                                                EntityPredicate.Builder.entity()
                                                        .flags(EntityFlagsPredicate.Builder.flags()
                                                                .setOnGround(false))
                                        ).build()
                                )
                        ))
                        .addEffect(new PlaySoundSpellEffect(
                                Holder.direct(SoundEvents.BREEZE_LAND),
                                ConstantFloat.of(1.0F),
                                UniformFloat.of(0.8F, 1.2F)
                        ))
                        .addEffect(new ApplyMobEffectSpellEffect(
                                HolderSet.direct(MobEffects.SLOW_FALLING),
                                UniformFloat.of(0.8F, 1.2F),
                                ConstantFloat.of(0.0F)
                        ))
                )
        );

        register(registry, DRAGON_FIREBALL_KEY, Spell.builder(80,
                        Component.translatable(getTranslationKey(DRAGON_FIREBALL_KEY)))
                .addCast(SpellCast.builder()
                        .addEffect(new PlaySoundSpellEffect(
                                Holder.direct(SoundEvents.ENDER_DRAGON_SHOOT),
                                ConstantFloat.of(1.0F),
                                UniformFloat.of(0.8F, 1.2F)))
                )
                .addCast(SpellCast.builder()
                        .addTransformer(MoveCastTransformer.builder(PROJECTILE_BASE).build())
                        .addEffect(
                                SummonEntitySpellEffect.builder(
                                        entityTypeReferenceFunction.apply(ModEntityType.SPELL_DRAGON_FIREBALL)
                                ).addEffect(
                                        new MoveSpellEffect(ConstantFloat.of(1.0F), false)
                                ).build()
                        )
                )
        );
        CompoundTag areaEffectCloudNbtCompound = new CompoundTag();
        areaEffectCloudNbtCompound.putInt("Duration", 60);
        areaEffectCloudNbtCompound.putFloat("Radius", 1.2F);
        areaEffectCloudNbtCompound.putFloat("RadiusPerTick", -0.004F);
        Tag nbtElement = PotionContents.CODEC.encodeStart(NbtOps.INSTANCE,
                new PotionContents(Potions.STRONG_HARMING)).getOrThrow();
        areaEffectCloudNbtCompound.put("potion_contents", nbtElement);
        areaEffectCloudNbtCompound.put("custom_particle", ParticleTypes.CODEC.encodeStart(NbtOps.INSTANCE,
                PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1)).getOrThrow());
        register(registry, DRAGON_GROWL_KEY, Spell.builder(80,
                        Component.translatable(getTranslationKey(DRAGON_GROWL_KEY)))
                .addCast(SpellCast.builder()
                        .addEffect(new PlaySoundSpellEffect(
                                Holder.direct(SoundEvents.ENDER_DRAGON_GROWL),
                                ConstantFloat.of(1.0F),
                                UniformFloat.of(0.8F, 1.2F))
                        )
                )
                .addCast(SpellCast.builder()
                        .addEffect(
                                SummonEntitySpellEffect.builder(
                                        entityTypeReferenceFunction.apply(EntityType.AREA_EFFECT_CLOUD)
                                ).nbt(areaEffectCloudNbtCompound).build()
                        )
                        .addTransformer(
                                RepeatCastTransformer.builder(4).stepDelay(4.0F).build()
                        )
                        .addTransformer(
                                RayCastTransformer.builder(RayCastTransformer.Target.BLOCK, DRAGON_GROWL_RANGE)
                                        .require(true)
                                        .build()
                        )
                )
                .addCast(SpellCast.builder()
                        .addEffect(
                                SpawnParticleSpellEffect.builder(PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1))
                                        .speed(ConstantFloat.ZERO)
                                        .build()
                        )
                        .addTransformer(
                                RepeatCastTransformer.builder(4).stepDelay(4.0F).build()
                        )
                        .addTransformer(
                                RayCastTransformer.builder(RayCastTransformer.Target.BLOCK, DRAGON_GROWL_RANGE)
                                        .require(true)
                                        .build()
                        )
                        .addTransformer(
                                LineCastTransformer.builder(12, PROJECTILE_BASE).build()
                        )
                )
        );

        register(registry, EVOKER_FANG_LINE_KEY, Spell.builder(40,
                        Component.translatable(getTranslationKey(EVOKER_FANG_LINE_KEY)))
                .addCast(SpellCast.builder()
                        .addEffect(new PlaySoundSpellEffect(
                                Holder.direct(SoundEvents.EVOKER_CAST_SPELL),
                                ConstantFloat.of(1.0F),
                                UniformFloat.of(0.8F, 1.2F)))
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
                                        ).stepDelay(1.0F)
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
                        Component.translatable(getTranslationKey(EVOKER_FANG_CIRCLE_KEY)))
                .addCast(SpellCast.builder()
                        .addEffect(new PlaySoundSpellEffect(
                                Holder.direct(SoundEvents.EVOKER_CAST_SPELL),
                                ConstantFloat.of(1.0F),
                                UniformFloat.of(0.8F, 1.2F)))
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
                                        RelativePositionSource.builder(new Vec3(0, 0, 1.5F)).build(), 5
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
                                        RelativePositionSource.builder(new Vec3(0, 0, 2.5f)).build(), 8
                                ).build()
                        )
                        .addTransformer(
                                SurfaceCastTransformer.builder(8).build()
                        )
                )
        );

        register(registry, GHAST_FIREBALL_KEY, Spell.builder(40,
                        Component.translatable(getTranslationKey(GHAST_FIREBALL_KEY)))
                .addCast(SpellCast.builder()
                        .addEffect(new PlaySoundSpellEffect(
                                Holder.direct(SoundEvents.GHAST_SHOOT),
                                ConstantFloat.of(1.0F),
                                UniformFloat.of(0.8F, 1.2F)))
                )
                .addCast(SpellCast.builder()
                        .addTransformer(MoveCastTransformer.builder(PROJECTILE_BASE).build())
                        .addEffect(
                                SummonEntitySpellEffect.builder(
                                        entityTypeReferenceFunction.apply(ModEntityType.SPELL_FIREBALL)
                                ).addEffect(
                                        new MoveSpellEffect(ConstantFloat.of(1.0F), false)
                                ).build()
                        )
                )
        );
        register(registry, GHAST_REGENERATION_KEY, Spell.builder(40,
                        MobEffects.REGENERATION.value().getDisplayName())
                .addCast(SpellCast.builder()
                        .addEffect(new ApplyMobEffectSpellEffect(
                                HolderSet.direct(MobEffects.REGENERATION),
                                UniformFloat.of(10.0F, 12.5F),
                                ConstantFloat.of(1.0F)
                        ))
                )
        );

        register(registry, GUARDIAN_BEAM_KEY, Spell.builder(30,
                        Component.translatable(getTranslationKey(GUARDIAN_BEAM_KEY)))
                .addCast(SpellCast.builder()
                        .addTransformer(
                                RayCastTransformer.builder(
                                        RayCastTransformer.Target.ENTITY, GUARDIAN_BEAM_RANGE
                                ).build()
                        )
                        .addEffect(
                                SummonEntitySpellEffect.builder(
                                                entityTypeReferenceFunction.apply(ModEntityType.SPELL_GUARDIAN_BEAM))
                                        .build()
                        )
                )
        );
        register(registry, GUARDIAN_HASTE_KEY, Spell.builder(30,
                        MobEffects.HASTE.value().getDisplayName())
                .addCast(SpellCast.builder()
                        .addEffect(new ApplyMobEffectSpellEffect(
                                HolderSet.direct(MobEffects.HASTE),
                                UniformFloat.of(20.0F, 25.0F),
                                ConstantFloat.of(1.0F)
                        ))
                        .addEffect(new RemoveMobEffectSpellEffect(
                                HolderSet.direct(MobEffects.MINING_FATIGUE)
                        ))
                )
        );

        register(registry, SHULKER_BULLET_KEY, Spell.builder(30,
                        Component.translatable(getTranslationKey(SHULKER_BULLET_KEY)))
                .addCast(SpellCast.builder()
                        .addTransformer(
                                RayCastTransformer.builder(
                                                RayCastTransformer.Target.ENTITY, SHULKER_BULLET_RANGE
                                        ).require(true)
                                        .build()
                        )
                        .addEffect(new PlaySoundSpellEffect(
                                Holder.direct(SoundEvents.SHULKER_SHOOT),
                                ConstantFloat.of(1.0F),
                                UniformFloat.of(0.8F, 1.2F)))
                )
                .addCast(SpellCast.builder()
                        .addTransformer(
                                RayCastTransformer.builder(
                                                RayCastTransformer.Target.ENTITY, SHULKER_BULLET_RANGE
                                        ).require(true)
                                        .build()
                        )
                        .addTransformer(MoveCastTransformer.builder(PROJECTILE_BASE).build())
                        .addEffect(
                                SummonEntitySpellEffect.builder(
                                        entityTypeReferenceFunction.apply(EntityType.SHULKER_BULLET)
                                ).build()
                        )
                )
        );
        register(registry, SHULKER_TELEPORT_KEY, Spell.builder(30,
                        Component.translatable(getTranslationKey(SHULKER_TELEPORT_KEY)))
                .addCast(SpellCast.builder()
                        .addEffect(new PlaySoundSpellEffect(
                                Holder.direct(SoundEvents.SHULKER_TELEPORT),
                                ConstantFloat.of(1.0F),
                                UniformFloat.of(0.8F, 1.2F))
                        )
                )
                .addCast(SpellCast.builder()
                        .addEffect(new TeleportSpellEffect())
                        .addTransformer(new AnchorCastTransformer())
                        .addTransformer(RepeatCastTransformer.builder(64).build())
                        .addTransformer(MoveCastTransformer.builder(
                                        RandomPositionSource.builder(8, 4, 8).build()
                                ).build()
                        )
                        .addTransformer(SurfaceCastTransformer.builder(4).require(true).build())
                )
        );

        register(registry, WARDEN_SONIC_BOOM_KEY, Spell.builder(60,
                        Component.translatable(getTranslationKey(WARDEN_SONIC_BOOM_KEY)))
                .addCast(SpellCast.builder()
                        .addEffect(new PlaySoundSpellEffect(
                                Holder.direct(SoundEvents.WARDEN_SONIC_BOOM),
                                ConstantFloat.of(1.0F),
                                UniformFloat.of(0.8F, 1.2F))
                        )
                )
                .addCast(SpellCast.builder()
                        .addTransformer(MoveCastTransformer.builder(PROJECTILE_BASE).build())
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
                                ConstantFloat.of(10.0F),
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
                                ConstantFloat.of(0.5F),
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
                                ConstantFloat.of(2.5F),
                                true
                        ))
                )
        );
        register(registry, WARDEN_STABILITY_KEY, Spell.builder(40,
                        ModStatusEffects.STABILITY.value().getDisplayName())
                .addCast(SpellCast.builder()
                        .addEffect(new ApplyMobEffectSpellEffect(
                                HolderSet.direct(ModStatusEffects.STABILITY),
                                UniformFloat.of(20.0F, 25.0F),
                                ConstantFloat.of(1.0F)
                        ))
                )
        );

        register(registry, WITHER_SKULL_KEY, Spell.builder(40,
                        Component.translatable(getTranslationKey(WITHER_SKULL_KEY)))
                .addCast(SpellCast.builder()
                        .addEffect(new PlaySoundSpellEffect(
                                Holder.direct(SoundEvents.WITHER_SHOOT),
                                ConstantFloat.of(1.0F),
                                UniformFloat.of(0.8F, 1.2F)))
                )
                .addCast(SpellCast.builder()
                        .addTransformer(MoveCastTransformer.builder(PROJECTILE_BASE).build())
                        .addEffect(
                                SummonEntitySpellEffect.builder(
                                        entityTypeReferenceFunction.apply(ModEntityType.SPELL_WITHER_SKULL)
                                ).addEffect(
                                        new MoveSpellEffect(ConstantFloat.of(1.0F), false)
                                ).build()
                        )
                )
        );
        register(registry, WITHER_REPULSION_KEY, Spell.builder(40,
                        ModStatusEffects.REPULSION.value().getDisplayName())
                .addCast(SpellCast.builder()
                        .addEffect(new ApplyMobEffectSpellEffect(
                                HolderSet.direct(ModStatusEffects.REPULSION),
                                UniformFloat.of(10.0F, 12.5F),
                                ConstantFloat.of(0.0F)
                        ))
                )
        );
    }

    private static void register(BootstrapContext<Spell> registry, ResourceKey<Spell> key, Spell.Builder builder) {
        KEYS.add(key);
        registry.register(key, builder.build());
    }

    public static String getTranslationKey(@NotNull ResourceKey<Spell> spell) {
        return spell.identifier().toLanguageKey(ModIdentifier.of("spell").toLanguageKey());
    }
}
