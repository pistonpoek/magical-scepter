package io.github.pistonpoek.magicalscepter.spell.cast.delay;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.timer.Timer;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.effect.SpellEffect;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface SpellCastScheduler {

    static void schedule(@NotNull SpellCasting cast, List<SpellEffect> effects) {
        LivingEntity caster = cast.getCaster();
        MinecraftServer minecraftServer = caster.getServer();
        if (minecraftServer == null) {
            return;
        }
        if (cast.getDelay() <= 0) {
            cast.apply(effects);
            return;
        }
        Timer<MinecraftServer> timer = minecraftServer.getSaveProperties().getMainWorldProperties().getScheduledEvents();
        long cast_time = caster.getWorld().getTime() + (long) cast.getDelay();
        timer.setEvent(createSpellCastEventName(caster, minecraftServer), cast_time,
                new SpellCastTimerCallback(
                        effects,
                        caster.getUuid(),
                        cast.getContextSource()
                )
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
