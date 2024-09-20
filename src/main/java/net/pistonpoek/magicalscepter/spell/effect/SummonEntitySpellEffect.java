package net.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

public record SummonEntitySpellEffect(
        RegistryEntryList<EntityType<?>> entityTypes,
        boolean joinTeam,
        double requireSurfaceDistance
) implements SpellEffect {
    public static final MapCodec<SummonEntitySpellEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            RegistryCodecs.entryList(RegistryKeys.ENTITY_TYPE).fieldOf("entity").forGetter(SummonEntitySpellEffect::entityTypes),
                            Codec.BOOL.optionalFieldOf("join_team", Boolean.FALSE).forGetter(SummonEntitySpellEffect::joinTeam),
                            Codec.DOUBLE.optionalFieldOf("require_surface_distance", 0.0).forGetter(SummonEntitySpellEffect::requireSurfaceDistance)
                    )
                    .apply(instance, SummonEntitySpellEffect::new)
    );

    @Override
    public void apply(ServerWorld world, Entity entity, Vec3d position, float pitch, float yaw) {
        Optional<Vec3d> floorPosition = getSurfacePosition(position, entity.getWorld());
        if (floorPosition.isEmpty())  {
            return;
        }
        Vec3d spawnPosition = floorPosition.get();
        BlockPos blockPos = BlockPos.ofFloored(spawnPosition);
        if (World.isValid(blockPos)) {
            Optional<RegistryEntry<EntityType<?>>> optional = this.entityTypes().getRandom(world.getRandom());
            if (optional.isPresent()) {
                Entity summonedEntity = ((EntityType<?>)((RegistryEntry<?>)optional.get()).value()).spawn(world, blockPos, SpawnReason.TRIGGERED);
                if (summonedEntity != null) {
                    if (summonedEntity instanceof LightningEntity lightningEntity && entity instanceof ServerPlayerEntity serverPlayerEntity) {
                        lightningEntity.setChanneler(serverPlayerEntity);
                    }

                    if (this.joinTeam && entity.getScoreboardTeam() != null) {
                        world.getScoreboard().addScoreHolderToTeam(entity.getNameForScoreboard(), entity.getScoreboardTeam());
                    }

                    summonedEntity.refreshPositionAndAngles(spawnPosition, yaw, pitch);
                }
            }
        }
    }

    public Optional<Vec3d> getSurfacePosition(Vec3d position, World world) {
        if (requireSurfaceDistance <= 0) {
            return Optional.ofNullable(position);
        }
        BlockPos blockPos = BlockPos.ofFloored(position.getX(), position.getY() + requireSurfaceDistance, position.getZ());
        boolean foundSurface = false;
        double blockHeight = 0.0;
        do {
            BlockState blockState = world.getBlockState(blockPos);
            VoxelShape voxelShape = blockState.getCollisionShape(world, blockPos);
            BlockPos LoweredBlockPos = blockPos.down();
            BlockState LoweredBlockState = world.getBlockState(LoweredBlockPos);
            if (!LoweredBlockState.isSideSolidFullSquare(world, LoweredBlockPos, Direction.UP)) continue;
            if (!world.isAir(blockPos) && !voxelShape.isEmpty()) {
                blockHeight = voxelShape.getMax(Direction.Axis.Y);
            }
            foundSurface = true;
            break;
        } while ((blockPos = blockPos.down()).getY() >= MathHelper.floor(position.getY() - requireSurfaceDistance));
        if (foundSurface) {
            return Optional.of(new Vec3d(position.getX(), blockPos.getY() + blockHeight, position.getZ()));
        }
        return Optional.empty();
    }

    @Override
    public MapCodec<SummonEntitySpellEffect> getCodec() {
        return CODEC;
    }
}
