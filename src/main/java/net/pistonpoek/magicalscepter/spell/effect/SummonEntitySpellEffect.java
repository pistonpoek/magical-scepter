package net.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
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
        boolean joinTeam
) implements SpellEffect {
    public static final MapCodec<SummonEntitySpellEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            RegistryCodecs.entryList(RegistryKeys.ENTITY_TYPE).fieldOf("entity").forGetter(SummonEntitySpellEffect::entityTypes),
                            Codec.BOOL.optionalFieldOf("join_team", Boolean.FALSE).forGetter(SummonEntitySpellEffect::joinTeam)
                    )
                    .apply(instance, SummonEntitySpellEffect::new)
    );

    @Override
    public void apply(ServerWorld world, LivingEntity entity, Vec3d position, float pitch, float yaw) {
        BlockPos blockPos = BlockPos.ofFloored(position);
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

                    summonedEntity.refreshPositionAndAngles(position, yaw, pitch);
                }
            }
        }
    }

    @Override
    public MapCodec<SummonEntitySpellEffect> getCodec() {
        return CODEC;
    }
}
