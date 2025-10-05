package io.github.pistonpoek.magicalscepter.component;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.UnaryOperator;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.component.DataComponentTypes
 */
public class ModDataComponentTypes {
    public static final ComponentType<ScepterExperienceComponent> SCEPTER_EXPERIENCE = register(
            "scepter_experience", builder ->
                    builder.codec(ScepterExperienceComponent.CODEC)
                            .packetCodec(ScepterExperienceComponent.PACKET_CODEC)
    );
    public static final ComponentType<ScepterContentsComponent> SCEPTER_CONTENTS = register(
            "scepter_contents", builder ->
                    builder.codec(ScepterContentsComponent.CODEC)
                            .packetCodec(ScepterContentsComponent.PACKET_CODEC).cache()
    );

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {

    }

    /**
     * Register a mod data component type for a specified identifier.
     *
     * @param identifier      String identifier to register data component type for.
     * @param builderOperator Component type builder operator to apply to the builder.
     * @param <T>             Type of the component type to register.
     * @return Registered component type.
     */
    private static <T> ComponentType<T> register(String identifier, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, ModIdentifier.of(identifier),
                builderOperator.apply(ComponentType.builder()).build());
    }
}
