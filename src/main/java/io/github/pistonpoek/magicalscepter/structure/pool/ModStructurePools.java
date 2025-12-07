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

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.structure.pool.StructurePools
 */
public class ModStructurePools {
    public static final List<RegistryKey<StructurePool>> KEYS = new ArrayList<>();

    /**
     * Get a structure pool registry key for the specified name.
     *
     * @param name String name to get structure pool registry key for.
     * @return Structure pool registry key with the specified name.
     */
    public static RegistryKey<StructurePool> of(String name) {
        return StructurePools.of(ModIdentifier.of(name));
    }

    /**
     * Register a structure pool in the registry under the specified identifier.
     *
     * @param registry Structure pool registry to register in.
     * @param identifier Identifier to register under.
     * @param pool Structure pool to register.
     */
    public static void register(Registerable<StructurePool> registry, String identifier, StructurePool pool) {
        KEYS.add(of(identifier));
        registry.register(of(identifier), pool);
    }

    /**
     * Bootstrap the structure pool registry.
     *
     * @param registry Structure pool registry to bootstrap.
     */
    public static void bootstrap(Registerable<StructurePool> registry) {
        addStructurePool(OldTaigaCabinGenerator::bootstrap, registry);
    }

    /**
     * Add a structure pool generator to bootstrap and store main structure pool key for.
     *
     * @param bootstrap Function to boostrap to get main structure pool key from.
     * @param registry Structure pool registry to use.
     */
    private static void addStructurePool(Function<Registerable<StructurePool>, RegistryKey<StructurePool>> bootstrap,
                                        Registerable<StructurePool> registry) {
        KEYS.add(bootstrap.apply(registry));
    }
}
