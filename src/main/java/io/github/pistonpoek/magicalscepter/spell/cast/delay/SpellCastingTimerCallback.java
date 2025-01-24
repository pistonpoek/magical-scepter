package io.github.pistonpoek.magicalscepter.spell.cast.delay;

import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.timer.Timer;
import net.minecraft.world.timer.TimerCallback;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;

import java.util.Optional;
import java.util.UUID;

public record SpellCastingTimerCallback(UUID caster, Integer key)
        implements TimerCallback<MinecraftServer> {

    @Override
    public void call(MinecraftServer server, Timer<MinecraftServer> events, long time) {
        Optional<SpellCasting> spellCasting = SpellCastingManager.load(server).retrieve(server, caster(), key());
        if (spellCasting.isEmpty()) {
            MagicalScepter.LOGGER.info("Could not load scheduled spell casting");
            return;
        }
        spellCasting.get().invoke();
    }

    public static class Serializer extends TimerCallback.Serializer<MinecraftServer, SpellCastingTimerCallback> {
        public Serializer() {
            super(ModIdentifier.of("spell_casting"), SpellCastingTimerCallback.class);
        }

        public void serialize(NbtCompound nbtCompound, SpellCastingTimerCallback spellCastingTimerCallback) {
            nbtCompound.putUuid("Caster", spellCastingTimerCallback.caster());
            nbtCompound.putInt("Key", spellCastingTimerCallback.key());
        }

        public SpellCastingTimerCallback deserialize(NbtCompound nbtCompound) {
            UUID caster = nbtCompound.getUuid("Caster");
            int key = nbtCompound.getInt("Key");
            return new SpellCastingTimerCallback(caster, key);
        }
    }
}
