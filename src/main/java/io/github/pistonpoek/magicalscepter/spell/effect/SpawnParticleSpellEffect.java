package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.phys.Vec3;

public record SpawnParticleSpellEffect(
        ParticleOptions particle,
        Vec3 delta,
        FloatProvider speed
) implements SpellEffect {
    public static final MapCodec<SpawnParticleSpellEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            ParticleTypes.CODEC.fieldOf("particle")
                                    .forGetter(SpawnParticleSpellEffect::particle),
                            Vec3.CODEC.optionalFieldOf("delta", Vec3.ZERO)
                                    .forGetter(SpawnParticleSpellEffect::delta),
                            FloatProvider.CODEC.optionalFieldOf("speed",
                                    ConstantFloat.ZERO).forGetter(SpawnParticleSpellEffect::speed)
                    )
                    .apply(instance, SpawnParticleSpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        RandomSource random = context.getRandom();
        Vec3 position = context.position();

        context.getWorld().sendParticles(
                this.particle,
                position.x(),
                position.y(),
                position.z(),
                0,
                delta.x,
                delta.y,
                delta.z,
                this.speed.sample(random)
        );
    }

    @Override
    public MapCodec<SpawnParticleSpellEffect> getCodec() {
        return MAP_CODEC;
    }

    public static SpawnParticleSpellEffect.Builder builder(ParticleOptions particleEffect) {
        return new SpawnParticleSpellEffect.Builder(particleEffect);
    }

    public static class Builder {
        private final ParticleOptions particleEffect;
        private Vec3 delta = Vec3.ZERO;
        private FloatProvider speed = ConstantFloat.ZERO;

        public Builder(ParticleOptions particleEffect) {
            this.particleEffect = particleEffect;
        }

        public Builder delta(Vec3 delta) {
            this.delta = delta;
            return this;
        }

        public Builder speed(FloatProvider speed) {
            this.speed = speed;
            return this;
        }

        public SpawnParticleSpellEffect build() {
            return new SpawnParticleSpellEffect(particleEffect, delta, speed);
        }
    }
}

