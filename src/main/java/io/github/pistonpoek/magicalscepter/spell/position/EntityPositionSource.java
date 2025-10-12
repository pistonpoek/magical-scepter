package io.github.pistonpoek.magicalscepter.spell.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public record EntityPositionSource(Anchor anchor) implements PositionSource {
    static MapCodec<EntityPositionSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Anchor.CODEC.optionalFieldOf("anchor", Anchor.FEET).forGetter(EntityPositionSource::anchor)
            ).apply(instance, EntityPositionSource::new)
    );

    @Override
    public Vec3d getPosition(@NotNull SpellContext context) {
        return switch (anchor) {
            case EYES -> context.caster().getEyePos();
            case FEET -> context.caster().getEntityPos();
        };
    }

    public enum Anchor implements StringIdentifiable {
        EYES("eyes"),
        FEET("feet");

        public final static Codec<Anchor> CODEC = StringIdentifiable.createBasicCodec(Anchor::values);
        private final String identifier;

        Anchor(String identifier) {
            this.identifier = identifier;
        }

        @Override
        public String asString() {
            return identifier;
        }
    }

    @Override
    public MapCodec<EntityPositionSource> getCodec() {
        return MAP_CODEC;
    }

    public static Builder builder(Anchor anchor) {
        return new Builder(anchor);
    }

    public static class Builder {
        private final Anchor anchor;

        public Builder(Anchor anchor) {
            this.anchor = anchor;
        }

        public EntityPositionSource build() {
            return new EntityPositionSource(anchor);
        }
    }

}

