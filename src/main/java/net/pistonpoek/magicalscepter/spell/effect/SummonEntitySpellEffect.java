package net.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;

import net.minecraft.entity.*;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.pistonpoek.magicalscepter.spell.cast.SpellContext;

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
// TODO add list of spell effect to apply on the summoned entity as target.

    @Override
    public void apply(SpellContext context) {
        Vec3d position = context.position();
        Random random = context.getRandom();
        ServerWorld world = context.getWorld();
        Entity target = context.target();

        BlockPos blockPos = BlockPos.ofFloored(position);
        if (World.isValid(blockPos)) {
            Optional<RegistryEntry<EntityType<?>>> optional = this.entityTypes().getRandom(random);
            if (optional.isPresent()) {
                Entity summonedEntity = ((EntityType<?>)((RegistryEntry<?>)optional.get()).value()).spawn(world, blockPos, SpawnReason.TRIGGERED);
                if (summonedEntity != null) {
                    if (summonedEntity instanceof LightningEntity lightningEntity && target instanceof ServerPlayerEntity serverPlayerEntity) {
                        lightningEntity.setChanneler(serverPlayerEntity);
                    }

                    if (this.joinTeam && target.getScoreboardTeam() != null) {
                        world.getScoreboard().addScoreHolderToTeam(target.getNameForScoreboard(), target.getScoreboardTeam());
                    }

                    summonedEntity.refreshPositionAndAngles(position, context.yaw(), context.pitch());
                }
            }
        }
    }

    @Override
    public MapCodec<SummonEntitySpellEffect> getCodec() {
        return CODEC;
    }
}
