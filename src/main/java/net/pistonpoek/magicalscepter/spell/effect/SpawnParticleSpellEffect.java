package net.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.random.Random;
import net.pistonpoek.magicalscepter.util.RotationVector;

public record SpawnParticleSpellEffect(
        ParticleEffect particle,
        FloatProvider speed
) implements SpellEffect {
    public static final MapCodec<SpawnParticleSpellEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            ParticleTypes.TYPE_CODEC.fieldOf("particle")
                                    .forGetter(SpawnParticleSpellEffect::particle),
                            FloatProvider.VALUE_CODEC.optionalFieldOf("speed",
                                    ConstantFloatProvider.ZERO).forGetter(SpawnParticleSpellEffect::speed)
                    )
                    .apply(instance, SpawnParticleSpellEffect::new)
    );

    @Override
    public void apply(ServerWorld world, LivingEntity entity, Vec3d position, float pitch, float yaw) {
        Random random = entity.getRandom();
        Vec3d rotation = RotationVector.get(pitch, yaw).normalize();
        world.spawnParticles(
                this.particle,
                position.getX(),
                position.getY(),
                position.getZ(),
                0,
                rotation.getX(),
                rotation.getY(),
                rotation.getZ(),
                (double)this.speed.get(random)
        );
    }

    @Override
    public MapCodec<SpawnParticleSpellEffect> getCodec() {
        return CODEC;
    }

}

