package io.github.pistonpoek.magicalscepter.spell.cast.delay;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.timers.TimerCallback;
import net.minecraft.world.level.timers.TimerQueue;
import java.util.Optional;
import java.util.UUID;

public record SpellCastingTimerCallback(UUID caster, Integer key)
        implements TimerCallback<MinecraftServer> {
    public static final Identifier ID = ModIdentifier.of("spell_cast");
    public static final MapCodec<SpellCastingTimerCallback> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    UUIDUtil.AUTHLIB_CODEC.fieldOf("caster").forGetter(SpellCastingTimerCallback::caster),
                    Codec.INT.fieldOf("key").forGetter(SpellCastingTimerCallback::key)
            ).apply(instance, SpellCastingTimerCallback::new)
    );

    @Override
    public void handle(MinecraftServer server, TimerQueue<MinecraftServer> events, long time) {
        Optional<SpellCasting> spellCasting = SpellCastingManager.load(server).retrieve(server, caster(), key());
        if (spellCasting.isEmpty()) {
            MagicalScepter.LOGGER.info("Could not load scheduled spell casting");
            return;
        }
        spellCasting.get().invoke();
    }

    @Override
    public MapCodec<SpellCastingTimerCallback> codec() {
        return MAP_CODEC;
    }
}
