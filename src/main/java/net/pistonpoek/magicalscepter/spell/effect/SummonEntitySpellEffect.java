package net.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
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
import net.minecraft.util.math.Vec3d;
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
    public void apply(ServerWorld world, Entity caster, Vec3d pos) {
        BlockPos blockPos = BlockPos.ofFloored(pos);
        if (World.isValid(blockPos)) {
            Optional<RegistryEntry<EntityType<?>>> optional = this.entityTypes().getRandom(world.getRandom());
            if (optional.isPresent()) {
                Entity entity = ((EntityType<?>)((RegistryEntry<?>)optional.get()).value()).spawn(world, blockPos, SpawnReason.TRIGGERED);
                if (entity != null) {
                    if (entity instanceof LightningEntity lightningEntity && caster instanceof ServerPlayerEntity serverPlayerEntity) {
                        lightningEntity.setChanneler(serverPlayerEntity);
                    }

                    if (this.joinTeam && caster.getScoreboardTeam() != null) {
                        world.getScoreboard().addScoreHolderToTeam(entity.getNameForScoreboard(), caster.getScoreboardTeam());
                    }

                    entity.refreshPositionAndAngles(pos.x, pos.y, pos.z, entity.getYaw(), entity.getPitch());
                }
            }
        }
    }

    @Override
    public MapCodec<SummonEntitySpellEffect> getCodec() {
        return CODEC;
    }
}
