package net.pistonpoek.magicalscepter.spell.cast;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.timer.Timer;
import net.pistonpoek.magicalscepter.spell.effect.SpellEffect;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record DelayedSpellCast(int delay,
                               List<SpellEffect> effects,
                               PositionSource position,
                               RotationSource rotation) implements SpellCast {
    public static final MapCodec<DelayedSpellCast> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codec.INT.optionalFieldOf("delay", 0).forGetter(DelayedSpellCast::delay),
                            SpellEffect.CODEC.listOf().fieldOf("effects").forGetter(DelayedSpellCast::effects),
                            SpellCast.PositionSource.CODEC.optionalFieldOf("position",
                                    new PositionSource(Vec3d.ZERO, PositionSource.Type.ENTITY_EYE))
                                    .forGetter(DelayedSpellCast::position),
                            SpellCast.RotationSource.CODEC.optionalFieldOf("rotation",
                                    new RotationSource(Vec3d.ZERO, RotationSource.Type.ENTITY))
                                    .forGetter(DelayedSpellCast::rotation)
                    )
                    .apply(instance, DelayedSpellCast::new)
    );

    @Override
    public void apply(@NotNull LivingEntity caster) {
        MinecraftServer minecraftServer = caster.getServer();
        if (minecraftServer == null) {
            return;
        }
        if (delay <= 0) {
            SpellCast.apply(effects, caster, position, rotation);
            return;
        }
        Timer<MinecraftServer> timer = minecraftServer.getSaveProperties().getMainWorldProperties().getScheduledEvents();
        long cast_time = caster.getWorld().getTime() + (long) delay;
        timer.setEvent(caster.getUuid().toString(), cast_time,
                new SpellCastTimerCallback(
                        effects,
                        caster.getUuid(),
                        position,
                        rotation
                )
        );
    }

    @Override
    public int getDelay() {
        return delay;
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

    public static DelayedSpellCast.Builder builder() {
        return new DelayedSpellCast.Builder();
    }

    public static class Builder {
        private int delay = 0;
        private final List<SpellEffect> effects = new ArrayList<>();
        private PositionSource position = new PositionSource(Vec3d.ZERO, PositionSource.Type.ENTITY_EYE);
        private RotationSource rotation = new RotationSource(Vec3d.ZERO, RotationSource.Type.ENTITY);

        public DelayedSpellCast.Builder delay(int delay) {
            this.delay = delay;
            return this;
        }

        public DelayedSpellCast.Builder addEffect(SpellEffect effect) {
            effects.add(effect);
            return this;
        }

        public DelayedSpellCast.Builder position(PositionSource position) {
            this.position = position;
            return this;
        }

        public DelayedSpellCast.Builder rotation(RotationSource rotation) {
            this.rotation = rotation;
            return this;
        }

        public DelayedSpellCast build() {
            return new DelayedSpellCast(delay, effects, position, rotation);
        }
    }

    @Override
    public MapCodec<? extends SpellCast> getCodec() {
        return CODEC;
    }
}
