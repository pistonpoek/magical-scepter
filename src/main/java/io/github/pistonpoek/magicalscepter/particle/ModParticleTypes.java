package io.github.pistonpoek.magicalscepter.particle;

import com.mojang.serialization.MapCodec;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.core.particles.ParticleTypes
 */
public class ModParticleTypes {
    /**
     * Initialize the class for the static fields.
     */
    public static void init() {

    }

    /**
     * Register a particle type.
     *
     * @param name Name to register the particle type with.
     * @param alwaysShow Truth assignment, if the particle should always show.
     * @param codecGetter Function to get the codec from the particle type.
     * @param packetCodecGetter Function to get the packet codec from the particle type.
     * @return Particle effect registered.
     * @param <T> Particle effect being registered.
     */
    private static <T extends ParticleOptions> ParticleType<T> register(
            String name,
            boolean alwaysShow,
            Function<ParticleType<T>, MapCodec<T>> codecGetter,
            Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> packetCodecGetter
    ) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, ModIdentifier.of(name), new ParticleType<T>(alwaysShow) {
            @Override
            public MapCodec<T> codec() {
                return codecGetter.apply(this);
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return packetCodecGetter.apply(this);
            }
        });
    }
}
