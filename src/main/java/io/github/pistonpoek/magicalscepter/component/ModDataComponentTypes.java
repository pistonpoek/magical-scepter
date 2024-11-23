package io.github.pistonpoek.magicalscepter.component;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;

import java.util.function.UnaryOperator;

//https://docs.fabricmc.net/develop/items/custom-data-components
public class ModDataComponentTypes {
    public static final ComponentType<ScepterContentsComponent> SCEPTER_CONTENTS = register(
            "scepter_contents", builder -> builder.codec(ScepterContentsComponent.CODEC).packetCodec(ScepterContentsComponent.PACKET_CODEC).cache()
    );

    public static void init() {

    }

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, ModIdentifier.of(id),
                ((ComponentType.Builder<T>)builderOperator.apply(ComponentType.builder())).build());
    }
}
