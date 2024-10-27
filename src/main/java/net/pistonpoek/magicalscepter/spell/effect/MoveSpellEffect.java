package net.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.random.Random;
import net.pistonpoek.magicalscepter.spell.cast.context.SpellContext;

import java.util.Optional;

public record MoveSpellEffect(FloatProvider power, boolean knockback) implements SpellEffect {
    public static final MapCodec<MoveSpellEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            FloatProvider.VALUE_CODEC.fieldOf("power").forGetter(MoveSpellEffect::power),
                            Codec.BOOL.optionalFieldOf("knockback", false).forGetter(MoveSpellEffect::knockback)
                    ).apply(instance, MoveSpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        Random random = context.getRandom();
        Vec3d rotation = context.getRotationVector().normalize();
        Optional<LivingEntity> target = context.getLivingTarget();

        if (knockback && target.isPresent()) {
            target.get().takeKnockback(power.get(random), -rotation.getX(), -rotation.getZ());
        } else {
            Entity entityTarget = context.target();
            entityTarget.velocityDirty = true;
            Vec3d force = rotation.multiply(power.get(random));
            Vec3d velocity = entityTarget.getVelocity().add(force);
            entityTarget.setVelocity(velocity);
        }
    }

    @Override
    public MapCodec<MoveSpellEffect> getCodec() {
        return CODEC;
    }
}
