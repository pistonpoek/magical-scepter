package io.github.pistonpoek.magicalscepter.spell.cast.delay;

import io.github.pistonpoek.magicalscepter.MagicalScepter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.timer.Timer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public abstract class SpellCastScheduler {
    private static final String EVENT_NAME_PREFIX = "spell_casting_";
    private static final int MAX_CASTER_CASTINGS = Integer.MAX_VALUE;

    /**
     * Schedule a spell cast callback after a specified delay.
     *
     * @param world Server world to schedule callback with.
     * @param callback Spell cast timer callback to schedule.
     * @param delay Positive time delay to set for the callback in ticks.
     */
    public static void schedule(ServerWorld world, @NotNull SpellCastTimerCallback callback, int delay) {
        if (delay <= 0) {
            throw new IllegalArgumentException("Delay is %d when trying to schedule spell cast".formatted(delay));
        }
        long cast_time = world.getTime() + (long) delay;

        MinecraftServer server = world.getServer();

        Timer<MinecraftServer> timer = server.getSaveProperties()
                .getMainWorldProperties().getScheduledEvents();

        int key = generateKey(server, callback.casterUUID());

        if (key == -1) {
            MagicalScepter.LOGGER.info("Max casting amount reached for caster {}", callback.casterUUID());
            return;
        }

        timer.setEvent(getEventName(key, callback.casterUUID()),
                cast_time,
                callback
        );
    }

    private static String getEventName(int key, UUID casterUUID) {
        return EVENT_NAME_PREFIX + "%d%s".formatted(key, casterUUID);
    }

    public static boolean clear(LivingEntity caster) {
        MinecraftServer server = caster.getServer();
        if (server == null) {
            return false;
        }
        Timer<MinecraftServer> timer = server.getSaveProperties().getMainWorldProperties().getScheduledEvents();
        boolean removed = false;
        for (String eventName :getScheduledSpellCasts(server, caster.getUuid())) {
            timer.remove(eventName);
            removed = true;
        }
        return removed;
    }

    private static int generateKey(@NotNull MinecraftServer server, @NotNull UUID casterUUID) {
        ArrayList<Integer> currentKeys = new ArrayList<>();

        for (String eventName : getScheduledSpellCasts(server, casterUUID)) {
            String strippedName = eventName.substring(EVENT_NAME_PREFIX.length(),
                    eventName.length() - casterUUID.toString().length());
            currentKeys.add(Integer.parseInt(strippedName));
        }
        if (currentKeys.isEmpty()) {
            return 0;
        }
        int nextKey = currentKeys.getLast();
        int startKey = nextKey;
        do {
            if (!currentKeys.contains(nextKey)) {
                return nextKey;
            }
            nextKey = (nextKey + 1) % MAX_CASTER_CASTINGS;
        } while (nextKey != startKey);

        MagicalScepter.LOGGER.warn("Caster reached max scheduled castings.");
        return -1;
    }

    private static Collection<String> getScheduledSpellCasts(@NotNull MinecraftServer server,
                                                             @NotNull UUID casterUUID) {
        Timer<MinecraftServer> timer = server.getSaveProperties().getMainWorldProperties().getScheduledEvents();
        Collection<String> spellCastEvents = new HashSet<>();
        for (String eventName : timer.getEventNames()) {
            if (!eventName.startsWith(EVENT_NAME_PREFIX)) {
                continue;
            }

            if (!eventName.endsWith(casterUUID.toString())) {
                continue;
            }

            spellCastEvents.add(eventName);
        }
        return spellCastEvents;
    }

    public static void afterDeath(LivingEntity entity, DamageSource damageSource) {
        if (entity == null) {
            return;
        }
        clear(entity);
    }

}
