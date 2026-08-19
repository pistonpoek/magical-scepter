package io.github.pistonpoek.magicalscepter.structure.pool;

import io.github.pistonpoek.magicalscepter.structure.OldTaigaCabinGenerator;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.data.worldgen.Pools
 */
public class ModStructurePools {
    public static final List<ResourceKey<StructureTemplatePool>> KEYS = new ArrayList<>();

    /**
     * Get a structure pool registry key for the specified name.
     *
     * @param name String name to get structure pool registry key for.
     * @return Structure pool registry key with the specified name.
     */
    public static ResourceKey<StructureTemplatePool> of(String name) {
        return Pools.createKey(ModIdentifier.of(name));
    }

    /**
     * Register a structure pool in the registry under the specified identifier.
     *
     * @param registry Structure pool registry to register in.
     * @param identifier Identifier to register under.
     * @param pool Structure pool to register.
     */
    public static void register(BootstrapContext<StructureTemplatePool> registry, String identifier, StructureTemplatePool pool) {
        KEYS.add(of(identifier));
        registry.register(of(identifier), pool);
    }

    /**
     * Bootstrap the structure pool registry.
     *
     * @param registry Structure pool registry to bootstrap.
     */
    public static void bootstrap(BootstrapContext<StructureTemplatePool> registry) {
        addStructurePool(OldTaigaCabinGenerator::bootstrap, registry);
    }

    /**
     * Add a structure pool generator to bootstrap and store main structure pool key for.
     *
     * @param bootstrap Function to boostrap to get main structure pool key from.
     * @param registry Structure pool registry to use.
     */
    private static void addStructurePool(Function<BootstrapContext<StructureTemplatePool>, ResourceKey<StructureTemplatePool>> bootstrap,
                                        BootstrapContext<StructureTemplatePool> registry) {
        KEYS.add(bootstrap.apply(registry));
    }
}
