package io.github.pistonpoek.magicalscepter.spell.cast.delay;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.timers.TimerQueue;
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
    static void schedule(@NotNull ServerLevel world, @NotNull SpellCastingTimerCallback callback, int delay) {
        if (delay <= 0) {
            throw new IllegalArgumentException("Delay is %d when trying to schedule spell cast".formatted(delay));
        }
        long cast_time = world.getGameTime() + (long) delay;

        MinecraftServer server = world.getServer();

        TimerQueue<MinecraftServer> timer = server.getScheduledEvents();

        timer.schedule(getEventName(callback),
                cast_time,
                callback
        );
    }

    private static String getEventName(@NotNull SpellCastingTimerCallback callback) {
        return EVENT_NAME_PREFIX + "%d%s".formatted(callback.key(), callback.caster());
    }

    static void clear(@NotNull MinecraftServer server, @NotNull SpellCastingTimerCallback callback) {
        TimerQueue<MinecraftServer> timer = server.getScheduledEvents();
        timer.remove(getEventName(callback));
    }
}
