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

public record SpawnParticlesSpellEffect(
        ParticleEffect particle,
        SpawnParticlesSpellEffect.PositionSource horizontalPosition,
        SpawnParticlesSpellEffect.PositionSource verticalPosition,
        SpawnParticlesSpellEffect.VelocitySource horizontalVelocity,
        SpawnParticlesSpellEffect.VelocitySource verticalVelocity,
        FloatProvider speed
) implements SpellEffect {
    public static final MapCodec<SpawnParticlesSpellEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            ParticleTypes.TYPE_CODEC.fieldOf("particle").forGetter(SpawnParticlesSpellEffect::particle),
                            SpawnParticlesSpellEffect.PositionSource.CODEC.fieldOf("horizontal_position").forGetter(SpawnParticlesSpellEffect::horizontalPosition),
                            SpawnParticlesSpellEffect.PositionSource.CODEC.fieldOf("vertical_position").forGetter(SpawnParticlesSpellEffect::verticalPosition),
                            SpawnParticlesSpellEffect.VelocitySource.CODEC.fieldOf("horizontal_velocity").forGetter(SpawnParticlesSpellEffect::horizontalVelocity),
                            SpawnParticlesSpellEffect.VelocitySource.CODEC.fieldOf("vertical_velocity").forGetter(SpawnParticlesSpellEffect::verticalVelocity),
                            FloatProvider.VALUE_CODEC.optionalFieldOf("speed", ConstantFloatProvider.ZERO).forGetter(SpawnParticlesSpellEffect::speed)
                    )
                    .apply(instance, SpawnParticlesSpellEffect::new)
    );

    public static SpawnParticlesSpellEffect.PositionSource entityPosition(float offset) {
        return new SpawnParticlesSpellEffect.PositionSource(SpawnParticlesSpellEffect.PositionSourceType.ENTITY_POSITION, offset, 1.0F);
    }

    public static SpawnParticlesSpellEffect.PositionSource withinBoundingBox() {
        return new SpawnParticlesSpellEffect.PositionSource(SpawnParticlesSpellEffect.PositionSourceType.BOUNDING_BOX, 0.0F, 1.0F);
    }

    public static SpawnParticlesSpellEffect.VelocitySource scaledVelocity(float movementScale) {
        return new SpawnParticlesSpellEffect.VelocitySource(movementScale, ConstantFloatProvider.ZERO);
    }

    public static SpawnParticlesSpellEffect.VelocitySource fixedVelocity(FloatProvider base) {
        return new SpawnParticlesSpellEffect.VelocitySource(0.0F, base);
    }

    @Override
    public void apply(ServerWorld world, Entity entity, Vec3d position, Vec3d rotation) {
        Random random = entity.getRandom();
        Vec3d vec3d = entity.getMovement();
        float width = entity.getWidth();
        float height = entity.getHeight();
        world.spawnParticles(
                this.particle,
                this.horizontalPosition.getPosition(position.getX(), position.getX(), width, random),
                this.verticalPosition.getPosition(position.getY(), position.getY() + (double)(height / 2.0F), height, random),
                this.horizontalPosition.getPosition(position.getZ(), position.getZ(), width, random),
                0,
                this.horizontalVelocity.getVelocity(vec3d.getX(), random),
                this.verticalVelocity.getVelocity(vec3d.getY(), random),
                this.horizontalVelocity.getVelocity(vec3d.getZ(), random),
                (double)this.speed.get(random)
        );
    }

    @Override
    public MapCodec<SpawnParticlesSpellEffect> getCodec() {
        return CODEC;
    }

    public static record PositionSource(SpawnParticlesSpellEffect.PositionSourceType type, float offset, float scale) {
        public static final MapCodec<SpawnParticlesSpellEffect.PositionSource> CODEC = RecordCodecBuilder.<SpawnParticlesSpellEffect.PositionSource>mapCodec(
                        instance -> instance.group(
                                        SpawnParticlesSpellEffect.PositionSourceType.CODEC.fieldOf("type").forGetter(SpawnParticlesSpellEffect.PositionSource::type),
                                        Codec.FLOAT.optionalFieldOf("offset", Float.valueOf(0.0F)).forGetter(SpawnParticlesSpellEffect.PositionSource::offset),
                                        Codecs.POSITIVE_FLOAT.optionalFieldOf("scale", 1.0F).forGetter(SpawnParticlesSpellEffect.PositionSource::scale)
                                )
                                .apply(instance, SpawnParticlesSpellEffect.PositionSource::new)
                )
                .validate(
                        source -> source.type() == SpawnParticlesSpellEffect.PositionSourceType.ENTITY_POSITION && source.scale() != 1.0F
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

        public static final Codec<SpawnParticlesSpellEffect.PositionSourceType> CODEC = StringIdentifiable.createCodec(
                SpawnParticlesSpellEffect.PositionSourceType::values
        );
        private final String id;
        private final SpawnParticlesSpellEffect.PositionSourceType.CoordinateSource coordinateSource;

        private PositionSourceType(final String id, final SpawnParticlesSpellEffect.PositionSourceType.CoordinateSource coordinateSource) {
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
        public static final MapCodec<SpawnParticlesSpellEffect.VelocitySource> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Codec.FLOAT.optionalFieldOf("movement_scale", 0.0F).forGetter(SpawnParticlesSpellEffect.VelocitySource::movementScale),
                                FloatProvider.VALUE_CODEC.optionalFieldOf("base", ConstantFloatProvider.ZERO).forGetter(SpawnParticlesSpellEffect.VelocitySource::base)
                        )
                        .apply(instance, SpawnParticlesSpellEffect.VelocitySource::new)
        );

        public double getVelocity(double entityVelocity, Random random) {
            return entityVelocity * (double)this.movementScale + (double)this.base.get(random);
        }
    }
}

