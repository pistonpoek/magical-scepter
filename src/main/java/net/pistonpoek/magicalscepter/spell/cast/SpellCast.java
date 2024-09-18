package net.pistonpoek.magicalscepter.spell.cast;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.timer.Timer;
import net.pistonpoek.magicalscepter.spell.effect.SpellEffect;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record SpellCast(int delay, List<SpellEffect> effects) {
    public static final Codec<SpellCast> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.INT.optionalFieldOf("delay", 0).forGetter(SpellCast::delay),
                            SpellEffect.CODEC.listOf().fieldOf("effects").forGetter(SpellCast::effects)
                    )
                    .apply(instance, SpellCast::new)
    );

    public void apply(LivingEntity caster) {
        apply(effects, caster);
    }

    public static void apply(List<SpellEffect> effects, LivingEntity caster) {
        ServerWorld serverWorld = (ServerWorld)caster.getWorld();
        for (SpellEffect spellEffect: effects) {
            spellEffect.apply(serverWorld, caster, caster.getPos());
        }
    }

    public void schedule(@NotNull LivingEntity caster) {
        MinecraftServer minecraftServer = caster.getServer();
        if (minecraftServer == null) {
            return;
        }
        if (delay <= 0) {
            apply(caster);
            return;
        }
        Timer<MinecraftServer> timer = minecraftServer.getSaveProperties().getMainWorldProperties().getScheduledEvents();
        long cast_time = caster.getWorld().getTime() + (long)delay;
        timer.setEvent(caster.getUuid().toString(), cast_time, new SpellCastTimerCallback(effects, caster.getUuid()));
    }

    public static void clear(@NotNull LivingEntity caster) {
        MinecraftServer minecraftServer = caster.getServer();
        if (minecraftServer == null) {
            return;
        }
        Timer<MinecraftServer> timer = minecraftServer.getSaveProperties().getMainWorldProperties().getScheduledEvents();
        timer.remove(caster.getUuid().toString());
    }

    public static void afterDeath(LivingEntity entity, DamageSource damageSource) {
        if (entity == null) {
            return;
        }
        clear(entity);
    }

    public static SpellCast.Builder builder() {
        return new SpellCast.Builder();
    }

    public static class Builder {
        private int delay = 0;
        private final List<SpellEffect> effects = new ArrayList<>();

        public SpellCast.Builder delay(int delay) {
            this.delay = delay;
            return this;
        }

        public SpellCast.Builder addEffect(SpellEffect effect) {
            effects.add(effect);
            return this;
        }

        public SpellCast build() {
            return new SpellCast(delay, effects);
        }
    }
}
