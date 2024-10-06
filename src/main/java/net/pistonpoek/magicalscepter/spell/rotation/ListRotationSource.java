package net.pistonpoek.magicalscepter.spell.rotation;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Pair;
import net.pistonpoek.magicalscepter.spell.cast.Cast;
import net.pistonpoek.magicalscepter.spell.cast.SpellContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ListRotationSource(List<RotationSource> rotations) implements RotationSource {
    static MapCodec<ListRotationSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    RotationSource.CODEC.listOf().fieldOf("rotations").forGetter(ListRotationSource::rotations)
            ).apply(instance, ListRotationSource::new)
    );

    @Override
    public RotationSource getSource(@NotNull Cast cast) {
        return this.append(cast.getRotation());
    }

    @Override
    public Pair<Float, Float> getRotation(@NotNull SpellContext context) {
        for (RotationSource rotation: rotations) {
            context = new SpellContext(
                    context.caster(),
                    context.position(),
                    rotation.getPitch(context),
                    rotation.getYaw(context));
        }
        return new Pair<>(context.pitch(), context.yaw());
    }

    public static RotationSource combine(RotationSource rotation1, RotationSource rotation2) {
        if (rotation1 instanceof ListRotationSource) {
            return ((ListRotationSource)rotation1).append(rotation2);
        }
        if (rotation2 instanceof ListRotationSource) {
            return ((ListRotationSource)rotation2).append(rotation1);
        }
        return new ListRotationSource(List.of(rotation1, rotation2));
    }

    public RotationSource append(RotationSource rotation) {
        if (rotation instanceof ListRotationSource) {
            for (RotationSource listRotation: ((ListRotationSource)rotation).rotations) {
                this.append(listRotation);
            }
            return this;
        }
        rotations.add(rotation);
        return this;
    }

    @Override
    public MapCodec<? extends RotationSource> getCodec() {
        return CODEC;
    }
}
