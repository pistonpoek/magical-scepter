package io.github.pistonpoek.magicalscepter.spell.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record EntityPositionSource(Anchor anchor) implements PositionSource {
    static MapCodec<EntityPositionSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Anchor.CODEC.optionalFieldOf("anchor", Anchor.FEET).forGetter(EntityPositionSource::anchor)
            ).apply(instance, EntityPositionSource::new)
    );

    @Override
    public Vec3 getPosition(@NotNull SpellContext context) {
        return switch (anchor) {
            case EYES -> context.caster().getEyePosition();
            case FEET -> context.caster().position();
        };
    }

    public enum Anchor implements StringRepresentable {
        EYES("eyes"),
        FEET("feet");

        public final static Codec<Anchor> CODEC = StringRepresentable.fromValues(Anchor::values);
        private final String identifier;

        Anchor(String identifier) {
            this.identifier = identifier;
        }

        @Override
        public String getSerializedName() {
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

