package net.pistonpoek.magicalscepter.spell.cast.delay;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.timer.Timer;
import net.pistonpoek.magicalscepter.spell.cast.*;
import net.pistonpoek.magicalscepter.spell.effect.SpellEffect;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface SpellCastScheduler {
    static void schedule(@NotNull Cast cast, List<SpellEffect> effects) {
        MinecraftServer minecraftServer = cast.getCaster().getServer();
        if (minecraftServer == null) {
            return;
        }
        if (cast.getDelay() <= 0) {
            cast.apply(effects);
            return;
        }
        Timer<MinecraftServer> timer = minecraftServer.getSaveProperties().getMainWorldProperties().getScheduledEvents();
        long cast_time = cast.getCaster().getWorld().getTime() + (long) cast.getDelay();
        timer.setEvent(cast.getCaster().getUuid().toString(), cast_time,
                new SpellCastTimerCallback(
                        effects,
                        cast.getCaster().getUuid(),
                        cast.getPositionSource(),
                        cast.getRotationSource()
                )
        );
    }

    static void clear(@NotNull LivingEntity caster) {
        MinecraftServer minecraftServer = caster.getServer();
        if (minecraftServer == null) {
            return;
        }
        Timer<MinecraftServer> timer = minecraftServer.getSaveProperties().getMainWorldProperties().getScheduledEvents();
        timer.remove(caster.getUuid().toString());
    }

    static void afterDeath(LivingEntity entity, DamageSource damageSource) {
        if (entity == null) {
            return;
        }
        clear(entity);
    }

}
