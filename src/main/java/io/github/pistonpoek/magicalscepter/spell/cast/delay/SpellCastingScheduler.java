package io.github.pistonpoek.magicalscepter.spell.cast.delay;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.timer.Timer;
import org.jetbrains.annotations.NotNull;

public abstract class SpellCastingScheduler {
    private static final String EVENT_NAME_PREFIX = "spell_casting_";

    /**
     * Schedule a spell cast callback after a specified delay.
     *
     * @param world    Server world to schedule callback with.
     * @param callback Spell cast timer callback to schedule.
     * @param delay    Positive time delay to set for the callback in ticks.
     */
    static void schedule(@NotNull ServerWorld world, @NotNull SpellCastingTimerCallback callback, int delay) {
        if (delay <= 0) {
            throw new IllegalArgumentException("Delay is %d when trying to schedule spell cast".formatted(delay));
        }
        long cast_time = world.getTime() + (long) delay;

        MinecraftServer server = world.getServer();

        Timer<MinecraftServer> timer = server.getSaveProperties()
                .getMainWorldProperties().getScheduledEvents();

        timer.setEvent(getEventName(callback),
                cast_time,
                callback
        );
    }

    private static String getEventName(@NotNull SpellCastingTimerCallback callback) {
        return EVENT_NAME_PREFIX + "%d%s".formatted(callback.key(), callback.caster());
    }

    static void clear(@NotNull MinecraftServer server, @NotNull SpellCastingTimerCallback callback) {
        Timer<MinecraftServer> timer = server.getSaveProperties().getMainWorldProperties().getScheduledEvents();
        timer.remove(getEventName(callback));
    }
}
