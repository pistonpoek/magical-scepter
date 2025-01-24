package io.github.pistonpoek.magicalscepter.component;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;

import java.util.function.UnaryOperator;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.component.DataComponentTypes
 */
public class ModDataComponentTypes {
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

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, ModIdentifier.of(id),
                builderOperator.apply(ComponentType.builder()).build());
    }
}
