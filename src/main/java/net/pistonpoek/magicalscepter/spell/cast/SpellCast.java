package net.pistonpoek.magicalscepter.spell.cast;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.pistonpoek.magicalscepter.MagicalScepter;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistries;
import net.pistonpoek.magicalscepter.spell.effect.SpellEffect;
import net.pistonpoek.magicalscepter.spell.effect.projectile.SpellProjectileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public interface SpellCast {
    Codec<SpellCast> CODEC = ModRegistries.SPELL_CAST_TYPE.getCodec().dispatch(SpellCast::getCodec, Function.identity());

    static void register(Registry<MapCodec<? extends SpellCast>> registry) {
        Registry.register(registry, ModIdentifier.of("cast"), DelayedSpellCast.CODEC);
        Registry.register(registry, ModIdentifier.of("line_cast"), LineSpellCast.CODEC);
    }

    void apply(@NotNull LivingEntity caster);

    default int getDelay() {
        return 0;
    }

    MapCodec<? extends SpellCast> getCodec();

    static void apply(List<SpellEffect> effects, Entity entity, PositionSource position, RotationSource rotation) {
        ServerWorld serverWorld = (ServerWorld) entity.getWorld();
        for (SpellEffect spellEffect : effects) {
            spellEffect.apply(serverWorld, entity, position.getPosition(entity), rotation.getRotation(entity));
        }
    }

    record PositionSource(Vec3d value, Type type) {
        static Codec<PositionSource> CODEC = RecordCodecBuilder.create(
                        instance -> instance.group(
                                Vec3d.CODEC.fieldOf("value").forGetter(PositionSource::value),
                                StringIdentifiable.createBasicCodec(Type::values).fieldOf("type").forGetter(PositionSource::type)
                        ).apply(instance, PositionSource::new)
        );

        public enum Type implements StringIdentifiable {
            VALUE("value"),
            PROJECTILE("projectile"),
            ENTITY_FEET("entity_feet"),
            ENTITY_EYE("entity_eye"),
            RELATIVE("relative"),
            ABSOLUTE("absolute");

            final String name;

            Type(String name) {
                this.name = name;
            }

            @Override
            public String asString() {
                return name;
            }
        }

        Vec3d getPosition(@NotNull Entity entity) {
            return switch (type) {
                case VALUE -> value;
                case ENTITY_EYE -> entity.getEyePos();
                case ENTITY_FEET -> entity.getPos();
                case PROJECTILE -> SpellProjectileHelper.getProjectilePosition(entity);
                case RELATIVE -> getRelativeVector(entity.getPos(), entity, value);
                case ABSOLUTE -> entity.getPos().add(value);
            };
        }

        private Vec3d getRelativeVector(Vec3d base, Entity entity, Vec3d value) {
            float pitch = entity.getPitch();
            float yaw = entity.getYaw();
            return base
                    .add(entity.getRotationVector(0, MathHelper.wrapDegrees(yaw - 90)).normalize().multiply(value.x))
                    .add(entity.getRotationVector(MathHelper.wrapDegrees(pitch - 90), yaw).normalize().multiply(value.y))
                    .add(entity.getRotationVector().normalize().multiply(value.z));
        }
    }

    record RotationSource(Vec3d value, Type type) {
        static Codec<RotationSource> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Vec3d.CODEC.fieldOf("value").forGetter(RotationSource::value),
                        StringIdentifiable.createBasicCodec(Type::values).fieldOf("type").forGetter(RotationSource::type)
                ).apply(instance, RotationSource::new)
        );

        enum Type implements StringIdentifiable {
            VALUE("value"),
            ENTITY("entity"),
            RELATIVE("relative");

            final String name;

            Type(String name) {
                this.name = name;
            }

            @Override
            public String asString() {
                return name;
            }
        }

        Vec3d getRotation(@NotNull Entity entity) {
            return switch (type) {
                case VALUE -> value;
                case ENTITY -> entity.getRotationVector();
                case RELATIVE -> entity.getRotationVector().add(value);
            };
        }
    }
}
