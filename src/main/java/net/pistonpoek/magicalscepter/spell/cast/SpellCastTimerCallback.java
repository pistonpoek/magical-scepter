package net.pistonpoek.magicalscepter.spell.cast;

import com.mojang.serialization.DataResult;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.timer.Timer;
import net.minecraft.world.timer.TimerCallback;
import net.pistonpoek.magicalscepter.MagicalScepter;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.spell.effect.SpellEffect;

import java.util.List;
import java.util.UUID;

public record SpellCastTimerCallback(List<SpellEffect> effects,
                                     UUID entityUUID,
                                     SpellCast.PositionSource position,
                                     SpellCast.RotationSource rotation) implements TimerCallback<MinecraftServer> {

    @Override
    public void call(MinecraftServer server, Timer<MinecraftServer> events, long time) {
        Entity entity = null;
        for (ServerWorld world : server.getWorlds()) {
            entity = world.getEntity(entityUUID);
            if (entity != null) {
                break;
            }
        }
        if (entity == null || !entity.isAlive()) {
            MagicalScepter.LOGGER.info("Spell cast is missing an entity.");
            return;
        }
        SpellCast.apply(effects, entity, position, rotation);
    }

    public static class Serializer extends TimerCallback.Serializer<MinecraftServer, SpellCastTimerCallback> {
        public Serializer() {
            super(ModIdentifier.of("spell_cast"), SpellCastTimerCallback.class);
        }

        public void serialize(NbtCompound nbtCompound, SpellCastTimerCallback spellCastTimerCallback) {
            final DataResult<NbtElement> encodedEffects = SpellEffect.CODEC.listOf()
                    .encodeStart(NbtOps.INSTANCE, spellCastTimerCallback.effects);
            nbtCompound.put("effects", encodedEffects.getOrThrow());
            nbtCompound.putUuid("entity", spellCastTimerCallback.entityUUID);
            final DataResult<NbtElement> encodedPositionSource = SpellCast.PositionSource.CODEC
                    .encodeStart(NbtOps.INSTANCE, spellCastTimerCallback.position);
            nbtCompound.put("position", encodedPositionSource.getOrThrow());
            final DataResult<NbtElement> encodedRotationSource = SpellCast.RotationSource.CODEC
                    .encodeStart(NbtOps.INSTANCE, spellCastTimerCallback.rotation);
            nbtCompound.put("rotation", encodedRotationSource.getOrThrow());
        }

        public SpellCastTimerCallback deserialize(NbtCompound nbtCompound) {
            List<SpellEffect> effects = SpellEffect.CODEC.listOf()
                    .decode(NbtOps.INSTANCE, nbtCompound.get("effects")).getOrThrow().getFirst();
            UUID entityUUID = nbtCompound.getUuid("entity");
            SpellCast.PositionSource position = SpellCast.PositionSource.CODEC
                    .decode(NbtOps.INSTANCE, nbtCompound.get("position")).getOrThrow().getFirst();
            SpellCast.RotationSource rotation = SpellCast.RotationSource.CODEC
                    .decode(NbtOps.INSTANCE, nbtCompound.get("rotation")).getOrThrow().getFirst();

            return new SpellCastTimerCallback(effects, entityUUID, position, rotation);
        }
    }
}
