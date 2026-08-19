package io.github.pistonpoek.magicalscepter.component;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import java.util.function.UnaryOperator;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.core.component.DataComponents
 */
public class ModDataComponentTypes {
    public static final DataComponentType<ScepterExperienceComponent> SCEPTER_EXPERIENCE = register(
            "scepter_experience", builder ->
                    builder.persistent(ScepterExperienceComponent.CODEC)
                            .networkSynchronized(ScepterExperienceComponent.PACKET_CODEC)
    );
    public static final DataComponentType<ScepterContentsComponent> SCEPTER_CONTENTS = register(
            "scepter_contents", builder ->
                    builder.persistent(ScepterContentsComponent.CODEC)
                            .networkSynchronized(ScepterContentsComponent.PACKET_CODEC).cacheEncoding()
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
    private static <T> DataComponentType<T> register(String identifier, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ModIdentifier.of(identifier),
                builderOperator.apply(DataComponentType.builder()).build());
    }
}
