package io.github.pistonpoek.magicalscepter.loot.context;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import java.util.function.Consumer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
 */
public class ModLootContextTypes {
    private static final BiMap<Identifier, ContextKeySet> MAP = HashBiMap.create();

    public static final ContextKeySet SPELL_CAST = register(
            "spell_cast",
            builder -> builder.required(LootContextParams.THIS_ENTITY)
                    .required(LootContextParams.ORIGIN)
                    .required(LootContextParams.TOOL)
                    .required(LootContextParams.BLOCK_STATE)
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
    public static BiMap<Identifier, ContextKeySet> getLootContextMap() {
        return MAP;
    }

    /**
     * Register a mod context type for the specified name.
     *
     * @param name String name to register for.
     * @param type Context type builder consumer to register.
     * @return Registered context type.
     */
    private static ContextKeySet register(String name, Consumer<ContextKeySet.Builder> type) {
        ContextKeySet.Builder builder = new ContextKeySet.Builder();
        type.accept(builder);
        ContextKeySet contextType = builder.build();
        ContextKeySet putContextType = MAP.put(ModIdentifier.of(name), contextType);
        if (putContextType != null) {
            throw new IllegalStateException("Loot table parameter set " +
                    ModIdentifier.of(name) + " is already registered");
        } else {
            return contextType;
        }
    }
}
