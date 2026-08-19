package io.github.pistonpoek.magicalscepter.registry;

import com.mojang.serialization.MapCodec;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContextSource;
import io.github.pistonpoek.magicalscepter.spell.cast.transformer.CastTransformer;
import io.github.pistonpoek.magicalscepter.spell.effect.SpellEffect;
import io.github.pistonpoek.magicalscepter.spell.position.PositionSource;
import io.github.pistonpoek.magicalscepter.spell.rotation.RotationSource;
import io.github.pistonpoek.magicalscepter.spell.target.TargetSource;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.core.registries.Registries
 */
public class ModRegistryKeys {
    public static final ResourceKey<Registry<Scepter>> SCEPTER = of("scepter");
    public static final ResourceKey<Registry<Spell>> SPELL = of("spell");
    public static final ResourceKey<Registry<MapCodec<? extends SpellEffect>>> SPELL_EFFECT_TYPE =
            of("spell_effect_type");
    public static final ResourceKey<Registry<MapCodec<? extends CastTransformer>>> CAST_TRANSFORMER_TYPE =
            of("cast_transformer_type");
    public static final ResourceKey<Registry<MapCodec<? extends PositionSource>>> CAST_POSITION_SOURCE_TYPE =
            of("cast_position_source_type");
    public static final ResourceKey<Registry<MapCodec<? extends RotationSource>>> CAST_ROTATION_SOURCE_TYPE =
            of("cast_rotation_source_type");
    public static final ResourceKey<Registry<MapCodec<? extends TargetSource>>> CAST_TARGET_SOURCE_TYPE =
            of("cast_target_source_type");
    public static final ResourceKey<Registry<MapCodec<? extends SpellContextSource>>> CAST_CONTEXT_SOURCE_TYPE =
            of("cast_context_source_type");

    /**
     * Get a registry key for a mod registry of the specified name.
     *
     * @param name String name of the registry to get a registry key for.
     * @return Registry key of the registry with the specified name.
     * @param <T> Type of the registry to get the registry key for.
     */
    private static <T> ResourceKey<Registry<T>> of(String name) {
        return ResourceKey.createRegistryKey(ModIdentifier.of(name));
    }

    /**
     * Get the directory for the specified registry key.
     *
     * @param registryKey Registry key to get the directory for.
     * @return String directory path of the registry key.
     */
    public static String directory(ResourceKey<?> registryKey) {
        Identifier identifier = registryKey.identifier();
        return identifier.getNamespace().equals(Identifier.DEFAULT_NAMESPACE) ?
                identifier.getPath() :
                String.join("/", identifier.getNamespace(), identifier.getPath());
    }
}
