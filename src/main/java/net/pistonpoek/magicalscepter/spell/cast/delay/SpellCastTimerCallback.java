package net.pistonpoek.magicalscepter.spell.cast.delay;

import com.mojang.serialization.DataResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.timer.Timer;
import net.minecraft.world.timer.TimerCallback;
import net.pistonpoek.magicalscepter.MagicalScepter;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import net.pistonpoek.magicalscepter.spell.cast.context.SpellContextSource;
import net.pistonpoek.magicalscepter.spell.effect.SpellEffect;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public record SpellCastTimerCallback(List<SpellEffect> effects,
                                     UUID casterUUID,
                                     SpellContextSource contextSource) implements TimerCallback<MinecraftServer> {

    @Override
    public void call(MinecraftServer minecraftServer, Timer<MinecraftServer> events, long time) {
        new SpellCasting(loadCaster(minecraftServer))
                .addContextSource(contextSource).apply(effects);
    }

    public static class Serializer extends TimerCallback.Serializer<MinecraftServer, SpellCastTimerCallback> {
        public Serializer() {
            super(ModIdentifier.of("spell_cast"), SpellCastTimerCallback.class);
        }

        public void serialize(NbtCompound nbtCompound, SpellCastTimerCallback spellCastTimerCallback) {
            final DataResult<NbtElement> encodedEffects = SpellEffect.CODEC.listOf()
                    .encodeStart(NbtOps.INSTANCE, spellCastTimerCallback.effects);
            nbtCompound.put("effects", encodedEffects.getOrThrow());
            nbtCompound.putUuid("caster", spellCastTimerCallback.casterUUID);
            final DataResult<NbtElement> encodedContextSource = SpellContextSource.CODEC
                    .encodeStart(NbtOps.INSTANCE, spellCastTimerCallback.contextSource);
            nbtCompound.put("context", encodedContextSource.getOrThrow());
        }

        public SpellCastTimerCallback deserialize(NbtCompound nbtCompound) {
            List<SpellEffect> effects = SpellEffect.CODEC.listOf()
                    .decode(NbtOps.INSTANCE, nbtCompound.get("effects")).getOrThrow().getFirst();
            UUID casterUUID = nbtCompound.getUuid("caster");
            UUID targetUUID = nbtCompound.getUuid("target");
            SpellContextSource contextSource = SpellContextSource.CODEC
                    .decode(NbtOps.INSTANCE, nbtCompound.get("context")).getOrThrow().getFirst();

            return new SpellCastTimerCallback(effects, casterUUID, contextSource);
        }
    }

    public LivingEntity loadCaster(@NotNull MinecraftServer minecraftServer) {
        Entity caster = null;
        for (ServerWorld world : minecraftServer.getWorlds()) {
            caster = world.getEntity(casterUUID);
            if (caster != null) {
                break;
            }
        }
        if (caster == null || !caster.isAlive()) {
            MagicalScepter.LOGGER.info("Spell cast is missing caster entity.");
            return null;
        }
        return (LivingEntity) caster;
    }

}
