package io.github.pistonpoek.magicalscepter.loot;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.world.level.storage.loot.BuiltInLootTables
 */
public class ModLootTables {
    private static final Set<ResourceKey<LootTable>> LOOT_TABLES = new HashSet<>();
    private static final Set<ResourceKey<LootTable>> LOOT_TABLES_READ_ONLY = Collections.unmodifiableSet(LOOT_TABLES);

    public static final ResourceKey<LootTable> OLD_TAIGA_CABIN_CHEST = register("chests/old_taiga_cabin");

    /**
     * Register a loot table for the specified name.
     *
     * @param name String name to register a loot table at.
     * @return Loot table registry key for the specified name.
     */
    private static ResourceKey<LootTable> register(String name) {
        return registerLootTable(ResourceKey.create(Registries.LOOT_TABLE, ModIdentifier.of(name)));
    }

    /**
     * Register a loot table for the specified registry key.
     *
     * @param key Loot table registry key to register a loot table at.
     * @return Loot table registry key specified.
     * @throws IllegalArgumentException Exception thrown if the registry key was already registered.
     */
    private static ResourceKey<LootTable> registerLootTable(ResourceKey<LootTable> key)
            throws IllegalArgumentException {
        if (LOOT_TABLES.add(key)) {
            return key;
        } else {
            throw new IllegalArgumentException(key.identifier() + " is already a registered built-in loot table");
        }
    }

    /**
     * Get all loot table registry keys.
     *
     * @return Set of all loot table registry keys.
     */
    public static Set<ResourceKey<LootTable>> getAll() {
        return LOOT_TABLES_READ_ONLY;
    }
}
