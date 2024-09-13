package net.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.random.Random;

public record SpawnParticleSpellEffect(
        ParticleEffect particle,
        SpawnParticleSpellEffect.PositionSource horizontalPosition,
        SpawnParticleSpellEffect.PositionSource verticalPosition,
        SpawnParticleSpellEffect.VelocitySource horizontalVelocity,
        SpawnParticleSpellEffect.VelocitySource verticalVelocity,
        FloatProvider speed
) implements SpellEffect {
    public static final MapCodec<SpawnParticleSpellEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            ParticleTypes.TYPE_CODEC.fieldOf("particle").forGetter(SpawnParticleSpellEffect::particle),
                            SpawnParticleSpellEffect.PositionSource.CODEC.fieldOf("horizontal_position").forGetter(SpawnParticleSpellEffect::horizontalPosition),
                            SpawnParticleSpellEffect.PositionSource.CODEC.fieldOf("vertical_position").forGetter(SpawnParticleSpellEffect::verticalPosition),
                            SpawnParticleSpellEffect.VelocitySource.CODEC.fieldOf("horizontal_velocity").forGetter(SpawnParticleSpellEffect::horizontalVelocity),
                            SpawnParticleSpellEffect.VelocitySource.CODEC.fieldOf("vertical_velocity").forGetter(SpawnParticleSpellEffect::verticalVelocity),
                            FloatProvider.VALUE_CODEC.optionalFieldOf("speed", ConstantFloatProvider.ZERO).forGetter(SpawnParticleSpellEffect::speed)
                    )
                    .apply(instance, SpawnParticleSpellEffect::new)
    );

    public static SpawnParticleSpellEffect.PositionSource entityPosition(float offset) {
        return new SpawnParticleSpellEffect.PositionSource(SpawnParticleSpellEffect.PositionSourceType.ENTITY_POSITION, offset, 1.0F);
    }

    public static SpawnParticleSpellEffect.PositionSource withinBoundingBox() {
        return new SpawnParticleSpellEffect.PositionSource(SpawnParticleSpellEffect.PositionSourceType.BOUNDING_BOX, 0.0F, 1.0F);
    }

    public static SpawnParticleSpellEffect.VelocitySource scaledVelocity(float movementScale) {
        return new SpawnParticleSpellEffect.VelocitySource(movementScale, ConstantFloatProvider.ZERO);
    }

    public static SpawnParticleSpellEffect.VelocitySource fixedVelocity(FloatProvider base) {
        return new SpawnParticleSpellEffect.VelocitySource(0.0F, base);
    }

    @Override
    public void apply(ServerWorld world, Entity caster, Vec3d pos) {
        Random random = caster.getRandom();
        Vec3d vec3d = caster.getMovement();
        float width = caster.getWidth();
        float height = caster.getHeight();
        world.spawnParticles(
                this.particle,
                this.horizontalPosition.getPosition(pos.getX(), pos.getX(), width, random),
                this.verticalPosition.getPosition(pos.getY(), pos.getY() + (double)(height / 2.0F), height, random),
                this.horizontalPosition.getPosition(pos.getZ(), pos.getZ(), width, random),
                0,
                this.horizontalVelocity.getVelocity(vec3d.getX(), random),
                this.verticalVelocity.getVelocity(vec3d.getY(), random),
                this.horizontalVelocity.getVelocity(vec3d.getZ(), random),
                (double)this.speed.get(random)
        );
    }

    @Override
    public MapCodec<SpawnParticleSpellEffect> getCodec() {
        return CODEC;
    }

    public static record PositionSource(SpawnParticleSpellEffect.PositionSourceType type, float offset, float scale) {
        public static final MapCodec<SpawnParticleSpellEffect.PositionSource> CODEC = RecordCodecBuilder.<SpawnParticleSpellEffect.PositionSource>mapCodec(
                        instance -> instance.group(
                                        SpawnParticleSpellEffect.PositionSourceType.CODEC.fieldOf("type").forGetter(SpawnParticleSpellEffect.PositionSource::type),
                                        Codec.FLOAT.optionalFieldOf("offset", Float.valueOf(0.0F)).forGetter(SpawnParticleSpellEffect.PositionSource::offset),
                                        Codecs.POSITIVE_FLOAT.optionalFieldOf("scale", 1.0F).forGetter(SpawnParticleSpellEffect.PositionSource::scale)
                                )
                                .apply(instance, SpawnParticleSpellEffect.PositionSource::new)
                )
                .validate(
                        source -> source.type() == SpawnParticleSpellEffect.PositionSourceType.ENTITY_POSITION && source.scale() != 1.0F
                                ? DataResult.error(() -> "Cannot scale an entity position coordinate source")
                                : DataResult.success(source)
                );

        public double getPosition(double entityPosition, double boundingBoxCenter, float boundingBoxSize, Random random) {
            return this.type.getCoordinate(entityPosition, boundingBoxCenter, boundingBoxSize * this.scale, random) + (double)this.offset;
        }
    }

    public static enum PositionSourceType implements StringIdentifiable {
        ENTITY_POSITION("entity_position", (entityPosition, boundingBoxCenter, boundingBoxSize, random) -> entityPosition),
        BOUNDING_BOX(
                "in_bounding_box", (entityPosition, boundingBoxCenter, boundingBoxSize, random) -> boundingBoxCenter + (random.nextDouble() - 0.5) * (double)boundingBoxSize
        );

        public static final Codec<SpawnParticleSpellEffect.PositionSourceType> CODEC = StringIdentifiable.createCodec(
                SpawnParticleSpellEffect.PositionSourceType::values
        );
        private final String id;
        private final SpawnParticleSpellEffect.PositionSourceType.CoordinateSource coordinateSource;

        private PositionSourceType(final String id, final SpawnParticleSpellEffect.PositionSourceType.CoordinateSource coordinateSource) {
            this.id = id;
            this.coordinateSource = coordinateSource;
        }

        public double getCoordinate(double entityPosition, double boundingBoxCenter, float boundingBoxSize, Random random) {
            return this.coordinateSource.getCoordinate(entityPosition, boundingBoxCenter, boundingBoxSize, random);
        }

        @Override
        public String asString() {
            return this.id;
        }

        @FunctionalInterface
        interface CoordinateSource {
            double getCoordinate(double entityPosition, double boundingBoxCenter, float boundingBoxSize, Random random);
        }
    }

    public static record VelocitySource(float movementScale, FloatProvider base) {
        public static final MapCodec<SpawnParticleSpellEffect.VelocitySource> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Codec.FLOAT.optionalFieldOf("movement_scale", 0.0F).forGetter(SpawnParticleSpellEffect.VelocitySource::movementScale),
                                FloatProvider.VALUE_CODEC.optionalFieldOf("base", ConstantFloatProvider.ZERO).forGetter(SpawnParticleSpellEffect.VelocitySource::base)
                        )
                        .apply(instance, SpawnParticleSpellEffect.VelocitySource::new)
        );

        public double getVelocity(double entityVelocity, Random random) {
            return entityVelocity * (double)this.movementScale + (double)this.base.get(random);
        }
    }
}

