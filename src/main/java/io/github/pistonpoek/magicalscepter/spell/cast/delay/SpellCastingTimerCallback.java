package io.github.pistonpoek.magicalscepter.spell.cast.delay;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.world.timer.Timer;
import net.minecraft.world.timer.TimerCallback;

import java.util.Optional;
import java.util.UUID;

/**
 * TODO
 *
 * @param caster
 * @param key
 */
public record SpellCastingTimerCallback(UUID caster, Integer key)
        implements TimerCallback<MinecraftServer> {
    public static final Identifier ID = ModIdentifier.of("spell_cast");
    public static final MapCodec<SpellCastingTimerCallback> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Uuids.CODEC.fieldOf("caster").forGetter(SpellCastingTimerCallback::caster),
                    Codec.INT.fieldOf("key").forGetter(SpellCastingTimerCallback::key)
            ).apply(instance, SpellCastingTimerCallback::new)
    );

    @Override
    public void call(MinecraftServer server, Timer<MinecraftServer> events, long time) {
        Optional<SpellCasting> spellCasting = SpellCastingManager.load(server).retrieve(server, caster(), key());
        if (spellCasting.isEmpty()) {
            MagicalScepter.LOGGER.info("Could not load scheduled spell casting");
            return;
        }
        spellCasting.get().invoke();
    }

    @Override
    public MapCodec<SpellCastingTimerCallback> getCodec() {
        return MAP_CODEC;
    }
}
