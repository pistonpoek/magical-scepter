package net.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.random.Random;
import net.pistonpoek.magicalscepter.spell.cast.context.SpellContext;

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
    public void apply(SpellContext context) {
        Random random = context.getRandom();
        Vec3d rotation = context.getRotationVector().normalize();
        Vec3d position = context.position();

        context.getWorld().spawnParticles(
                this.particle,
                position.getX(),
                position.getY(),
                position.getZ(),
                0,
                rotation.getX(),
                rotation.getY(),
                rotation.getZ(),
                this.speed.get(random)
        );
    }

    @Override
    public MapCodec<SpawnParticleSpellEffect> getCodec() {
        return CODEC;
    }

}

