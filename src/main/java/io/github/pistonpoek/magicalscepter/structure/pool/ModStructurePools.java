package io.github.pistonpoek.magicalscepter.structure.pool;

import io.github.pistonpoek.magicalscepter.structure.OldTaigaCabinGenerator;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePools;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ModStructurePools {
    public static final List<RegistryKey<StructurePool>> KEYS = new ArrayList<>();

    public static RegistryKey<StructurePool> of(String identifier) {
        return StructurePools.of(ModIdentifier.of(identifier));
    }

    public static void register(Registerable<StructurePool> registry, String identifier, StructurePool pool) {
        KEYS.add(of(identifier));
        registry.register(of(identifier), pool);
    }

    public static void bootstrap(Registerable<StructurePool> registry) {
        addStructurePool(OldTaigaCabinGenerator::bootstrap, registry);
    }

    private static void addStructurePool(Function<Registerable<StructurePool>, RegistryKey<StructurePool>> bootstrap,
                                        Registerable<StructurePool> registry) {
        KEYS.add(bootstrap.apply(registry));
    }
}
