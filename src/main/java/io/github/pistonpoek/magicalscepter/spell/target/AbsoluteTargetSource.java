package io.github.pistonpoek.magicalscepter.spell.target;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record AbsoluteTargetSource(UUID targetUUID) implements TargetSource {
    public static MapCodec<AbsoluteTargetSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.STRING.fieldOf("target").forGetter(value -> value.targetUUID.toString())
            ).apply(instance, value -> new AbsoluteTargetSource(UUID.fromString(value)))
    );

    @Override
    public Entity getTarget(@NotNull SpellContext spellContext) {
        MinecraftServer minecraftServer = spellContext.caster().getServer();
        if (minecraftServer == null) {
            return spellContext.target();
        }

        Entity target = null;
        for (ServerWorld world : minecraftServer.getWorlds()) {
            target = world.getEntity(targetUUID);
            if (target != null) {
                break;
            }
        }
        if (target == null) {
            return spellContext.target();
        }

        return target;
    }

    @Override
    public MapCodec<AbsoluteTargetSource> getCodec() {
        return MAP_CODEC;
    }
}
