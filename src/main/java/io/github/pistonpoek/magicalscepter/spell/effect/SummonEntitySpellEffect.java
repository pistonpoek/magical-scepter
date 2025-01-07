package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.github.pistonpoek.magicalscepter.spell.rotation.RotationSource;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.EvokerFangsEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.entry.RegistryEntryListCodec;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;

public record SummonEntitySpellEffect(
        RegistryEntryList<EntityType<?>> entityTypes,
        List<SpellEffect> effects,
        Optional<NbtCompound> nbt
) implements SpellEffect {
    public static final MapCodec<SummonEntitySpellEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            RegistryEntryListCodec.create(RegistryKeys.ENTITY_TYPE,
                                            Registries.ENTITY_TYPE.getEntryCodec(), false)
                                .fieldOf("entity").forGetter(SummonEntitySpellEffect::entityTypes),
                            SpellEffect.CODEC.listOf().fieldOf("effects").forGetter(SummonEntitySpellEffect::effects),
                            NbtCompound.CODEC.optionalFieldOf("nbt").forGetter(SummonEntitySpellEffect::nbt)
                    ).apply(instance, SummonEntitySpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        Vec3d position = context.position();
        Random random = context.getRandom();
        ServerWorld world = context.getWorld();
        Entity target = context.target();
        LivingEntity caster = context.caster();

        BlockPos blockPos = BlockPos.ofFloored(position);
        if (!World.isValid(blockPos)) {
            MagicalScepter.LOGGER.info("Failed to summon entity spell effect as position is not valid");
            return;
        }

        Optional<RegistryEntry<EntityType<?>>> optionalEntityType = entityTypes.getRandom(random);
        if (optionalEntityType.isEmpty()) {
            MagicalScepter.LOGGER.info("Failed to summon entity spell effect as entity type is missing");
            return;
        }

        Registry<EntityType<?>> entityTypeRegistry = world.getRegistryManager().getOrThrow(RegistryKeys.ENTITY_TYPE);
        EntityType<?> entityType = optionalEntityType.get().value();
        if (!entityType.isSummonable()) {
            MagicalScepter.LOGGER.info("Failed to summon entity spell effect as entity type is not summonable");
            return;
        }

        Identifier entityTypeIdentifier = entityTypeRegistry.getId(entityType);
        if (entityTypeIdentifier == null) {
            MagicalScepter.LOGGER.info("Failed to summon entity spell effect as entity type id is invalid");
            return;
        }

        NbtCompound nbtCompound = nbt.orElse(new NbtCompound());

        nbtCompound.putString("id", entityTypeIdentifier.toString());
        if (entityType == EntityType.SHULKER_BULLET) {
            initializeShulkerBulletNbtCompound(nbtCompound, context);
        }

        Entity entity = EntityType.loadEntityWithPassengers(nbtCompound, world, SpawnReason.MOB_SUMMONED, summonedEntity -> {
            summonedEntity.refreshPositionAndAngles(position.x, position.y, position.z, context.yaw(), context.pitch());
            return summonedEntity;
        });

        if (entity instanceof MobEntity mobEntity) {
            mobEntity.initialize(world, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.MOB_SUMMONED, null);
        }

        if (!world.spawnNewEntityAndPassengers(entity)) {
            MagicalScepter.LOGGER.info("Failed to summon entity spell effect in the world due to conflicting UUIDs");
            return;
        }

        if (entity != null) {
            if (entity instanceof LightningEntity lightningEntity && target instanceof ServerPlayerEntity serverPlayerEntity) {
                lightningEntity.setChanneler(serverPlayerEntity);
            }

            if (entity instanceof ProjectileEntity projectileEntity) {
                projectileEntity.setOwner(caster);
            }
            if (entity instanceof AreaEffectCloudEntity areaEffectCloudEntity) {
                areaEffectCloudEntity.setOwner(caster);
            }
            if (entity instanceof EvokerFangsEntity evokerFangsEntity) {
                evokerFangsEntity.setOwner(caster);
            }
            if (entity instanceof VexEntity vexEntity && caster instanceof MobEntity mobCaster) {
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

    private static void initializeShulkerBulletNbtCompound(NbtCompound nbtCompound, SpellContext context) {
        Entity target = context.target();
        LivingEntity caster = context.caster();

        nbtCompound.putUuid("Target", target.getUuid());
        Direction direction = RotationSource.getDirection(context);
        nbtCompound.putInt("Dir", direction.getId());
        nbtCompound.putInt("Steps",  10 + caster.getRandom().nextInt(5) * 10);
        double xOffset = direction.getOffsetX();
        double yOffset = direction.getOffsetY();
        double zOffset = direction.getOffsetZ();
        double distance = Math.sqrt(xOffset * xOffset + yOffset * yOffset + zOffset * zOffset);
        nbtCompound.putDouble("TXD", xOffset / distance * 0.15);
        nbtCompound.putDouble("TYD", yOffset / distance * 0.15);
        nbtCompound.putDouble("TZD", zOffset / distance * 0.15);
    }

    public static Builder builder(RegistryEntry.Reference<EntityType<?>> entityType) {
        return new Builder(entityType);
    }

    public static class Builder {
        private final List<RegistryEntry.Reference<EntityType<?>>> entityTypes = new ArrayList<>();
        private final List<SpellEffect> effects = new ArrayList<>();
        private NbtCompound nbt = null;

        public Builder(RegistryEntry.Reference<EntityType<?>> entityType) {
            entityTypes.add(entityType);
        }

        public Builder addEntityType(RegistryEntry.Reference<EntityType<?>> entityType) {
            entityTypes.add(entityType);
            return this;
        }

        public Builder addEffect(SpellEffect effect) {
            effects.add(effect);
            return this;
        }

        public Builder nbt(NbtCompound nbt) {
            this.nbt = nbt;
            return this;
        }

        public SummonEntitySpellEffect build() {
            return new SummonEntitySpellEffect(RegistryEntryList.of(entityTypes), effects, Optional.ofNullable(nbt));
        }

    }
}
