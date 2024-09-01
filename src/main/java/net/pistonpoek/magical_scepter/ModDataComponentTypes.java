package net.pistonpoek.magical_scepter;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.pistonpoek.magical_scepter.item.scepter.Scepter;
import net.pistonpoek.magical_scepter.util.ModIdentifier;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final ComponentType<RegistryEntry<Scepter>> SCEPTER = register(
            "scepter_contents", builder -> builder.codec(Scepter.CODEC).packetCodec(Scepter.PACKET_CODEC).cache()
    );

    public static void init() {

    }

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, ModIdentifier.of(id),
                ((ComponentType.Builder<T>)builderOperator.apply(ComponentType.builder())).build());
    }
}
