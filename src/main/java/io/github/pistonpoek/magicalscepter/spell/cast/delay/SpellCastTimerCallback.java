package io.github.pistonpoek.magicalscepter.spell.cast.delay;

import com.mojang.serialization.DataResult;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.spell.cast.SpellCast;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContextSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.timer.Timer;
import net.minecraft.world.timer.TimerCallback;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record SpellCastTimerCallback(SpellCast spellCast, UUID casterUUID, SpellContextSource context)
        implements TimerCallback<MinecraftServer> {

    @Override
    public void call(MinecraftServer server, Timer<MinecraftServer> events, long time) {
        LivingEntity caster = loadCaster(server, casterUUID);
        if (caster == null || !caster.isAlive()) {
            MagicalScepter.LOGGER.info("Spell casting is missing caster entity.");
            return;
        }
        new SpellCasting(spellCast, caster, context).invoke();
    }

    private static LivingEntity loadCaster(@NotNull MinecraftServer server, UUID casterUUID) {
        Entity caster = null;
        for (ServerWorld world: server.getWorlds()) {
            caster = world.getEntity(casterUUID);
            if (caster != null) {
                break;
            }
        }
        return (LivingEntity) caster;
    }

    public static class Serializer extends TimerCallback.Serializer<MinecraftServer, SpellCastTimerCallback> {
        public Serializer() {
            super(ModIdentifier.of("spell_casting"), SpellCastTimerCallback.class);
        }

        public void serialize(NbtCompound nbtCompound, SpellCastTimerCallback spellCastTimerCallback) {
            final DataResult<NbtElement> encodedSpellCast = SpellCast.CODEC
                    .encodeStart(NbtOps.INSTANCE, spellCastTimerCallback.spellCast());
            nbtCompound.put("spell_cast", encodedSpellCast.getOrThrow());
            nbtCompound.putUuid("caster", spellCastTimerCallback.casterUUID());
            final DataResult<NbtElement> encodedContextSource = SpellContextSource.CODEC
                    .encodeStart(NbtOps.INSTANCE, spellCastTimerCallback.context());
            nbtCompound.put("context", encodedContextSource.getOrThrow());
        }

        public SpellCastTimerCallback deserialize(NbtCompound nbtCompound) {
            SpellCast spellCast = SpellCast.CODEC
                    .decode(NbtOps.INSTANCE, nbtCompound.get("spell_cast")).getOrThrow().getFirst();
            UUID casterUUID = nbtCompound.getUuid("caster");
            SpellContextSource contextSource = SpellContextSource.CODEC
                    .decode(NbtOps.INSTANCE, nbtCompound.get("context")).getOrThrow().getFirst();
            return new SpellCastTimerCallback(spellCast, casterUUID, contextSource);
        }
    }
}
