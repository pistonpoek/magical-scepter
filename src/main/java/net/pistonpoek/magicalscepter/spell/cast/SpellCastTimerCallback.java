package net.pistonpoek.magicalscepter.spell.cast;

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
import net.pistonpoek.magicalscepter.spell.Spell;
import net.pistonpoek.magicalscepter.spell.effect.SpellEffect;

import java.util.List;
import java.util.UUID;

public record SpellCastTimerCallback(List<SpellEffect> effects,
                                     UUID caster) implements TimerCallback<MinecraftServer> {

    @Override
    public void call(MinecraftServer server, Timer<MinecraftServer> events, long time) {
        Entity foundCaster = null;
        for (ServerWorld world : server.getWorlds()) {
            foundCaster = world.getEntity(caster);
            if (foundCaster != null) {
                break;
            }
        }
        if (foundCaster == null || !foundCaster.isLiving() || !foundCaster.isAlive()) {
            MagicalScepter.LOGGER.info("Spell cast is missing a living caster.");
            return;
        }
        SpellCast.apply(effects, (LivingEntity) foundCaster);
    }

    public static class Serializer extends TimerCallback.Serializer<MinecraftServer, SpellCastTimerCallback> {
        public Serializer() {
            super(ModIdentifier.of("spell_cast"), SpellCastTimerCallback.class);
        }

        public void serialize(NbtCompound nbtCompound, SpellCastTimerCallback spellCastTimerCallback) {
            final DataResult<NbtElement> encodeResult = SpellEffect.CODEC.listOf()
                    .encodeStart(NbtOps.INSTANCE, spellCastTimerCallback.effects);
            nbtCompound.put("effects", encodeResult.getOrThrow());
            nbtCompound.putUuid("caster", spellCastTimerCallback.caster);
        }

        public SpellCastTimerCallback deserialize(NbtCompound nbtCompound) {
            UUID caster = nbtCompound.getUuid("caster");
            List<SpellEffect> effects = SpellEffect.CODEC.listOf()
                    .decode(NbtOps.INSTANCE, nbtCompound.get("effects")).getOrThrow().getFirst();

            return new SpellCastTimerCallback(effects, caster);
        }
    }
}
