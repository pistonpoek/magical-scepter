package net.pistonpoek.magicalscepter.spell.cast;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public record LineSpellCast(DelayedSpellCast spellCast,
                            PositionSource startPosition,
                            PositionSource endPosition,
                            RotationSource rotation,
                            int amount,
                            int stepDelay) implements SpellCast {
    public static final MapCodec<LineSpellCast> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            DelayedSpellCast.CODEC.fieldOf("spell_cast").forGetter(LineSpellCast::spellCast),
                            PositionSource.CODEC.fieldOf("start_position").forGetter(LineSpellCast::startPosition),
                            PositionSource.CODEC.fieldOf("end_position").forGetter(LineSpellCast::endPosition),
                            SpellCast.RotationSource.CODEC.optionalFieldOf("rotation",
                                            new RotationSource(Vec3d.ZERO, RotationSource.Type.ENTITY))
                                    .forGetter(LineSpellCast::rotation),
                            Codec.INT.fieldOf("amount").forGetter(LineSpellCast::amount),
                            Codec.INT.optionalFieldOf("step_delay", 0).forGetter(LineSpellCast::stepDelay)
                    ).apply(instance, LineSpellCast::new)
    );

    @Override
    public void apply(@NotNull LivingEntity caster) {
        Vec3d startPos = startPosition.getPosition(caster);
        Vec3d lineVector = endPosition.getPosition(caster).subtract(startPos);
        for (int i = 0; i <= amount; i++) {
            new DelayedSpellCast(
                    spellCast.getDelay() + i * stepDelay,
                    spellCast.effects(),
                    new PositionSource(startPos.add(lineVector.multiply(((double)i) / (amount - 1))), PositionSource.Type.VALUE),
                    new RotationSource(rotation.getRotation(caster), RotationSource.Type.VALUE)
                    ).apply(caster);
        }
    }

    @Override
    public int getDelay() {
        return spellCast.getDelay() + (amount - 1) * stepDelay;
    }

    public static Builder builder(int amount,
                                  PositionSource startPosition,
                                  PositionSource endPosition,
                                  DelayedSpellCast spellCast) {
        return new Builder(spellCast, startPosition, endPosition, amount);
    }

    public static class Builder {
        private final DelayedSpellCast spellCast;
        private final PositionSource startPosition;
        private final PositionSource endPosition;
        private RotationSource rotation = new RotationSource(Vec3d.ZERO, RotationSource.Type.ENTITY);
        private final int amount;
        private int stepDelay = 0;

        public Builder(DelayedSpellCast spellCast,
                       PositionSource startPosition,
                       PositionSource endPosition,
                       int amount) {
            this.spellCast = spellCast;
            this.startPosition = startPosition;
            this.endPosition = endPosition;
            this.amount = amount;
        }

        public Builder stepDelay(int stepDelay) {
            this.stepDelay = stepDelay;
            return this;
        }

        public Builder rotation(RotationSource rotation) {
            this.rotation = rotation;
            return this;
        }

        public LineSpellCast build() {
            return new LineSpellCast(spellCast, startPosition, endPosition, rotation, amount, stepDelay);
        }
    }

    @Override
    public MapCodec<? extends SpellCast> getCodec() {
        return CODEC;
    }
}
