package net.pistonpoek.magical_scepter;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.DefaultedRegistry;
import net.pistonpoek.magical_scepter.item.scepter.Scepter;
import net.pistonpoek.magical_scepter.util.ModIdentifier;

public class ModRegistries {

    public static final DefaultedRegistry<Scepter> SCEPTER =
            FabricRegistryBuilder.createDefaulted(ModRegistryKeys.SCEPTER, ModIdentifier.of("magical"))
                    .attribute(RegistryAttribute.SYNCED)
                    .buildAndRegister();
}
