package io.github.pistonpoek.magicalscepter.spell.target;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Uuids;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public record AbsoluteTargetSource(UUID targetUUID) implements TargetSource {
    public static MapCodec<AbsoluteTargetSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Uuids.CODEC.fieldOf("target").forGetter(AbsoluteTargetSource::targetUUID)
            ).apply(instance, AbsoluteTargetSource::new)
    );

    @Override
    public Entity getTarget(@NotNull SpellContext spellContext) {
        MinecraftServer server = spellContext.caster().getEntityWorld().getServer();
        if (server == null) {
            return spellContext.target();
        }

        return load(server).orElse(spellContext.target());
    }

    public Optional<Entity> load(@NotNull MinecraftServer server) {
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
