package io.github.pistonpoek.magicalscepter.registry;

import com.mojang.serialization.MapCodec;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContextSource;
import io.github.pistonpoek.magicalscepter.spell.cast.transformer.CastTransformer;
import io.github.pistonpoek.magicalscepter.spell.effect.SpellEffect;
import io.github.pistonpoek.magicalscepter.spell.position.PositionSource;
import io.github.pistonpoek.magicalscepter.spell.rotation.RotationSource;
import io.github.pistonpoek.magicalscepter.spell.target.TargetSource;
import net.minecraft.util.Identifier;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.registry.RegistryKeys
 */
public class ModRegistryKeys {
    public static final RegistryKey<Registry<Scepter>> SCEPTER = of("scepter");
    public static final RegistryKey<Registry<Spell>> SPELL = of("spell");
    public static final RegistryKey<Registry<MapCodec<? extends SpellEffect>>> SPELL_EFFECT_TYPE =
            of("spell_effect_type");
    public static final RegistryKey<Registry<MapCodec<? extends CastTransformer>>> CAST_TRANSFORMER_TYPE =
            of("cast_transformer_type");
    public static final RegistryKey<Registry<MapCodec<? extends PositionSource>>> CAST_POSITION_SOURCE_TYPE =
            of("cast_position_source_type");
    public static final RegistryKey<Registry<MapCodec<? extends RotationSource>>> CAST_ROTATION_SOURCE_TYPE =
            of("cast_rotation_source_type");
    public static final RegistryKey<Registry<MapCodec<? extends TargetSource>>> CAST_TARGET_SOURCE_TYPE =
            of("cast_target_source_type");
    public static final RegistryKey<Registry<MapCodec<? extends SpellContextSource>>> CAST_CONTEXT_SOURCE_TYPE =
            of("cast_context_source_type");

    private static <T> RegistryKey<Registry<T>> of(String identifier) {
        return RegistryKey.ofRegistry(ModIdentifier.of(identifier));
    }

    public static String directory(RegistryKey<?> registryKey) {
        Identifier identifier = registryKey.getValue();
        return identifier.getNamespace().equals(Identifier.DEFAULT_NAMESPACE) ?
                identifier.getPath() :
                String.join("/", identifier.getNamespace(), identifier.getPath());
    }
}
