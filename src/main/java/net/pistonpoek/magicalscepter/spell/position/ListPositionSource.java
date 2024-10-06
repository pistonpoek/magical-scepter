package net.pistonpoek.magicalscepter.spell.position;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.Vec3d;
import net.pistonpoek.magicalscepter.spell.cast.Cast;
import net.pistonpoek.magicalscepter.spell.cast.SpellContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ListPositionSource(List<PositionSource> positions) implements PositionSource {
    static MapCodec<ListPositionSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    PositionSource.CODEC.listOf().fieldOf("positions").forGetter(ListPositionSource::positions)
            ).apply(instance, ListPositionSource::new)
    );

    @Override
    public PositionSource getSource(@NotNull Cast cast) {
        return append(cast.getPositionSource());
    }

    @Override
    public Vec3d getPosition(@NotNull SpellContext context) {
        for (PositionSource position: positions) {
            context = new SpellContext(
                    context.caster(),
                    position.getPosition(context),
                    context.pitch(),
                    context.yaw());
        }
        return context.position();
    }

    public static PositionSource combine(PositionSource position1, PositionSource position2) {
        if (position1 instanceof ListPositionSource) {
            return ((ListPositionSource)position1).append(position2);
        }
        if (position2 instanceof ListPositionSource) {
            return ((ListPositionSource)position2).append(position1);
        }
        return new ListPositionSource(List.of(position1, position2));
    }

    public PositionSource append(PositionSource position) {
        if (position instanceof ListPositionSource) {
            for (PositionSource listPosition: ((ListPositionSource)position).positions) {
                this.append(listPosition);
            }
            return this;
        }
        positions.add(position);
        return this;
    }

    @Override
    public MapCodec<ListPositionSource> getCodec() {
        return CODEC;
    }

}
