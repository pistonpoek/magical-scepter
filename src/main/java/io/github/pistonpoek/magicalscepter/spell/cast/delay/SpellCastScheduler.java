package io.github.pistonpoek.magicalscepter.spell.cast.delay;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.timer.Timer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface SpellCastScheduler {

    /**
     * Schedule a spell cast callback after a specified delay for a specified caster.
     *
     * @param caster Living entity for whom the callback should be scheduled.
     * @param callback Spell cast timer callback to schedule.
     * @param delay Time delay to set for the callback in ticks.
     */
    static void schedule(@NotNull LivingEntity caster, @NotNull SpellCastTimerCallback callback, int delay) {
        MinecraftServer minecraftServer = caster.getServer();
        if (minecraftServer == null) {
            return;
        }
        if (delay <= 0) {
            throw new IllegalArgumentException("Delay is %d when trying to schedule spell cast".formatted(delay));
        }
        Timer<MinecraftServer> timer = minecraftServer.getSaveProperties().getMainWorldProperties().getScheduledEvents();
        long cast_time = caster.getWorld().getTime() + (long) delay;
        timer.setEvent(createSpellCastEventName(caster, minecraftServer),
                cast_time,
                callback
        );
    }

    /**
     * Clear the spell casts for a specified living entity.
     *
     * @param caster Living entity to clear currently scheduled spell casts for.
     * @return Truth assignment, if a scheduled spell cast was cleared.
     */
    static boolean clear(@NotNull LivingEntity caster) {
        MinecraftServer minecraftServer = caster.getServer();
        if (minecraftServer == null) {
            return false;
        }
        Timer<MinecraftServer> timer = minecraftServer.getSaveProperties().getMainWorldProperties().getScheduledEvents();
        Collection<String> scheduledSpellCasts = getScheduledSpellCasts(caster, minecraftServer);
        if (scheduledSpellCasts.isEmpty()) {
            return false;
        }
        scheduledSpellCasts.forEach(timer::remove);
        return true;
    }

    static String createSpellCastEventName(@NotNull LivingEntity caster,
                                           @NotNull MinecraftServer minecraftServer) {
        String spellCastEventNameFormat = "spell_cast_%d_%s";
        Collection<String> spellCastEvents = getScheduledSpellCasts(caster, minecraftServer);
        return spellCastEventNameFormat.formatted(spellCastEvents.size(), caster.getUuid().toString());
    }

    static Collection<String> getScheduledSpellCasts(@NotNull LivingEntity caster,
                                                     @NotNull MinecraftServer minecraftServer) {
        Timer<MinecraftServer> timer = minecraftServer.getSaveProperties().getMainWorldProperties().getScheduledEvents();
        Collection<String> spellCastEvents = new HashSet<>();
        for (String eventName : timer.getEventNames()) {
            if (!eventName.startsWith("spell_cast_")) {
                continue;
            }

            if (!eventName.endsWith(caster.getUuid().toString())) {
                continue;
            }

            spellCastEvents.add(eventName);
        }
        return spellCastEvents;
    }

    static void afterDeath(LivingEntity entity, DamageSource damageSource) {
        if (entity == null) {
            return;
        }
        clear(entity);
    }

}
