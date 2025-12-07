package io.github.pistonpoek.magicalscepter.spell.target;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Uuids;

import java.util.Optional;
import java.util.UUID;

/**
 * TODO
 *
 * @param targetUUID
 */
public record AbsoluteTargetSource(UUID targetUUID) implements TargetSource {
    public static final MapCodec<AbsoluteTargetSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Uuids.CODEC.fieldOf("target").forGetter(AbsoluteTargetSource::targetUUID)
            ).apply(instance, AbsoluteTargetSource::new)
    );

    @Override
    public Entity getTarget(SpellContext spellContext) {
        MinecraftServer server = spellContext.caster().getEntityWorld().getServer();
        if (server == null) {
            return spellContext.target();
        }

        return load(server).orElse(spellContext.target());
    }

    /**
     * TODO
     *
     * @param server
     * @return
     */
    public Optional<Entity> load(MinecraftServer server) {
        Entity target = null;
        for (ServerWorld world : server.getWorlds()) {
            target = world.getEntity(targetUUID);
            if (target != null) {
                break;
            }
        }

        return Optional.ofNullable(target);
    }

    @Override
    public MapCodec<AbsoluteTargetSource> getCodec() {
        return MAP_CODEC;
    }
}
