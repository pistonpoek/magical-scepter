package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.entity.spell.SpellGuardianBeamEntity;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import io.github.pistonpoek.magicalscepter.spell.rotation.RotationSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record SummonEntitySpellEffect(
        HolderSet<EntityType<?>> entityTypes,
        List<SpellEffect> effects,
        Optional<CompoundTag> nbt
) implements SpellEffect {
    public static final MapCodec<SummonEntitySpellEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    HolderSetCodec.create(Registries.ENTITY_TYPE,
                                    BuiltInRegistries.ENTITY_TYPE.holderByNameCodec(), false)
                            .fieldOf("entity").forGetter(SummonEntitySpellEffect::entityTypes),
                    SpellEffect.CODEC.listOf().fieldOf("effects").forGetter(SummonEntitySpellEffect::effects),
                    CompoundTag.CODEC.optionalFieldOf("nbt").forGetter(SummonEntitySpellEffect::nbt)
            ).apply(instance, SummonEntitySpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        Vec3 position = context.position();
        RandomSource random = context.getRandom();
        ServerLevel world = context.getWorld();
        LivingEntity caster = context.caster();

        BlockPos blockPos = BlockPos.containing(position);
        if (!Level.isInSpawnableBounds(blockPos)) {
            MagicalScepter.LOGGER.info("Failed to summon entity spell effect as position is not valid");
            return;
        }

        Optional<Holder<EntityType<?>>> optionalEntityType = entityTypes.getRandomElement(random);
        if (optionalEntityType.isEmpty()) {
            MagicalScepter.LOGGER.info("Failed to summon entity spell effect as entity type is missing");
            return;
        }

        Registry<EntityType<?>> entityTypeRegistry = world.registryAccess().lookupOrThrow(Registries.ENTITY_TYPE);
        EntityType<?> entityType = optionalEntityType.get().value();
        if (!entityType.canSummon()) {
            MagicalScepter.LOGGER.info("Failed to summon entity spell effect as entity type is not summonable");
            return;
        }

        Identifier entityTypeIdentifier = entityTypeRegistry.getKey(entityType);
        if (entityTypeIdentifier == null) {
            MagicalScepter.LOGGER.info("Failed to summon entity spell effect as entity type id is invalid");
            return;
        }

        CompoundTag nbtCompound = nbt.orElse(new CompoundTag());

        nbtCompound.putString("id", entityTypeIdentifier.toString());
        if (entityType == EntityType.SHULKER_BULLET) {
            initializeShulkerBulletNbtCompound(nbtCompound, context);
        }

        Entity entity = EntityType.loadEntityRecursive(nbtCompound, world, EntitySpawnReason.MOB_SUMMONED, summonedEntity -> {
            summonedEntity.snapTo(position.x, position.y, position.z, context.yaw(), context.pitch());
            return summonedEntity;
        });

        if (entity instanceof Mob mobEntity) {
            mobEntity.finalizeSpawn(world, world.getCurrentDifficultyAt(entity.blockPosition()), EntitySpawnReason.MOB_SUMMONED, null);
        }

        if (!world.tryAddFreshEntityWithPassengers(entity)) {
            MagicalScepter.LOGGER.info("Failed to summon entity spell effect in the world due to conflicting UUIDs");
            return;
        }

        if (entity != null) {
            if (entity instanceof LightningBolt lightningEntity && caster instanceof ServerPlayer serverPlayerEntity) {
                lightningEntity.setCause(serverPlayerEntity);
            }

            if (entity instanceof Projectile projectileEntity) {
                projectileEntity.setOwner(caster);
            }
            if (entity instanceof AreaEffectCloud areaEffectCloudEntity) {
                areaEffectCloudEntity.setOwner(caster);
            }
            if (entity instanceof SpellGuardianBeamEntity spellGuardianBeamEntity) {
                spellGuardianBeamEntity.setOwner(caster);
                spellGuardianBeamEntity.setTarget(context.target());
            }
            if (entity instanceof EvokerFangs evokerFangsEntity) {
                evokerFangsEntity.setOwner(caster);
            }
            if (entity instanceof Vex vexEntity && caster instanceof Mob mobCaster) {
                vexEntity.setOwner(mobCaster);
            }

            SpellContext summonedContext = new SpellContext(context, entity);
            summonedContext.apply(effects);
        }
    }

    @Override
    public MapCodec<SummonEntitySpellEffect> getCodec() {
        return MAP_CODEC;
    }

    private static void initializeShulkerBulletNbtCompound(CompoundTag nbtCompound, SpellContext context) {
        Entity target = context.target();
        LivingEntity caster = context.caster();

        if (target != null) {
            nbtCompound.store("Target", UUIDUtil.CODEC, target.getUUID());
        }
        Direction direction = RotationSource.getDirection(context);
        nbtCompound.storeNullable("Dir", Direction.LEGACY_ID_CODEC, direction);
        nbtCompound.putInt("Steps", 10 + caster.getRandom().nextInt(5) * 10);
        double xOffset = direction.getStepX();
        double yOffset = direction.getStepY();
        double zOffset = direction.getStepZ();
        double distance = Math.sqrt(xOffset * xOffset + yOffset * yOffset + zOffset * zOffset);
        nbtCompound.putDouble("TXD", xOffset / distance * 0.15);
        nbtCompound.putDouble("TYD", yOffset / distance * 0.15);
        nbtCompound.putDouble("TZD", zOffset / distance * 0.15);
    }

    public static Builder builder(Holder.Reference<EntityType<?>> entityType) {
        return new Builder(entityType);
    }

    public static class Builder {
        private final List<Holder.Reference<EntityType<?>>> entityTypes = new ArrayList<>();
        private final List<SpellEffect> effects = new ArrayList<>();
        private CompoundTag nbt = null;

        public Builder(Holder.Reference<EntityType<?>> entityType) {
            entityTypes.add(entityType);
        }

        public Builder addEntityType(Holder.Reference<EntityType<?>> entityType) {
            entityTypes.add(entityType);
            return this;
        }

        public Builder addEffect(SpellEffect effect) {
            effects.add(effect);
            return this;
        }

        public Builder nbt(CompoundTag nbt) {
            this.nbt = nbt;
            return this;
        }

        public SummonEntitySpellEffect build() {
            return new SummonEntitySpellEffect(HolderSet.direct(entityTypes), effects, Optional.ofNullable(nbt));
        }

    }
}
