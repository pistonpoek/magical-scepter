package io.github.pistonpoek.magicalscepter.loot;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.loot.LootTables
 */
public class ModLootTables {
    private static final Set<RegistryKey<LootTable>> LOOT_TABLES = new HashSet<>();
    private static final Set<RegistryKey<LootTable>> LOOT_TABLES_READ_ONLY = Collections.unmodifiableSet(LOOT_TABLES);

    public static final RegistryKey<LootTable> OLD_TAIGA_CABIN_CHEST = register("chests/old_taiga_cabin");

    /**
     * Register a loot table for the specified name.
     *
     * @param name String name to register a loot table at.
     * @return Loot table registry key for the specified name.
     */
    private static RegistryKey<LootTable> register(String name) {
        return registerLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, ModIdentifier.of(name)));
    }

    /**
     * Register a loot table for the specified registry key.
     *
     * @param key Loot table registry key to register a loot table at.
     * @return Loot table registry key specified.
     * @throws IllegalArgumentException Exception thrown if the registry key was already registered.
     */
    private static RegistryKey<LootTable> registerLootTable(RegistryKey<LootTable> key)
            throws IllegalArgumentException {
        if (LOOT_TABLES.add(key)) {
            return key;
        } else {
            throw new IllegalArgumentException(key.getValue() + " is already a registered built-in loot table");
        }
    }

    /**
     * Get all loot table registry keys.
     *
     * @return Set of all loot table registry keys.
     */
    public static Set<RegistryKey<LootTable>> getAll() {
        return LOOT_TABLES_READ_ONLY;
    }
}
