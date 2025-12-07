package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.random.Random;

/**
 * TODO
 *
 * @param particle
 * @param delta
 * @param speed
 */
public record SpawnParticleSpellEffect(
        ParticleEffect particle,
        Vec3d delta,
        FloatProvider speed
) implements SpellEffect {
    public static final MapCodec<SpawnParticleSpellEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            ParticleTypes.TYPE_CODEC.fieldOf("particle")
                                    .forGetter(SpawnParticleSpellEffect::particle),
                            Vec3d.CODEC.optionalFieldOf("delta", Vec3d.ZERO)
                                    .forGetter(SpawnParticleSpellEffect::delta),
                            FloatProvider.VALUE_CODEC.optionalFieldOf("speed",
                                    ConstantFloatProvider.ZERO).forGetter(SpawnParticleSpellEffect::speed)
                    )
                    .apply(instance, SpawnParticleSpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        Random random = context.getRandom();
        Vec3d position = context.position();

        context.getWorld().spawnParticles(
                this.particle,
                position.getX(),
                position.getY(),
                position.getZ(),
                0,
                delta.x,
                delta.y,
                delta.z,
                this.speed.get(random)
        );
    }

    @Override
    public MapCodec<SpawnParticleSpellEffect> getCodec() {
        return MAP_CODEC;
    }

    /**
     * TODO
     *
     * @param particleEffect
     * @return
     */
    public static SpawnParticleSpellEffect.Builder builder(ParticleEffect particleEffect) {
        return new SpawnParticleSpellEffect.Builder(particleEffect);
    }

    /**
     * TODO
     */
    public static class Builder {
        private final ParticleEffect particleEffect;
        private Vec3d delta = Vec3d.ZERO;
        private FloatProvider speed = ConstantFloatProvider.ZERO;

        /**
         * TODO
         *
         * @param particleEffect
         */
        public Builder(ParticleEffect particleEffect) {
            this.particleEffect = particleEffect;
        }

        /**
         * TODO
         *
         * @param delta
         * @return
         */
        public Builder delta(Vec3d delta) {
            this.delta = delta;
            return this;
        }

        /**
         * TODO
         *
         * @param speed
         * @return
         */
        public Builder speed(FloatProvider speed) {
            this.speed = speed;
            return this;
        }

        /**
         * TODO
         *
         * @return
         */
        public SpawnParticleSpellEffect build() {
            return new SpawnParticleSpellEffect(particleEffect, delta, speed);
        }
    }
}

