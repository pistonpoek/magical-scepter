package io.github.pistonpoek.magicalscepter.loot.context;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextType;

import java.util.function.Consumer;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.loot.context.LootContextTypes
 */
public class ModLootContextTypes {
    private static final BiMap<Identifier, ContextType> MAP = HashBiMap.create();

    public static ContextType SPELL_CAST = register(
            "spell_cast",
            builder -> builder.require(LootContextParameters.THIS_ENTITY)
                    .require(LootContextParameters.ORIGIN)
                    .require(LootContextParameters.TOOL)
                    .require(LootContextParameters.BLOCK_STATE)
    );

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {

    }

    /**
     * Get the mod loot context map.
     *
     * @return Mod loot context map.
     */
    public static BiMap<Identifier, ContextType> getLootContextMap() {
        return MAP;
    }

    /**
     * Register a mod context type for the specified identifier.
     *
     * @param identifier String identifier to register for.
     * @param type Context type builder consumer to register.
     * @return Registered context type.
     */
    private static ContextType register(String identifier, Consumer<ContextType.Builder> type) {
        ContextType.Builder builder = new ContextType.Builder();
        type.accept(builder);
        ContextType contextType = builder.build();
        ContextType putContextType = MAP.put(ModIdentifier.of(identifier), contextType);
        if (putContextType != null) {
            throw new IllegalStateException("Loot table parameter set " +
                    ModIdentifier.of(identifier) + " is already registered");
        } else {
            return contextType;
        }
    }
}
