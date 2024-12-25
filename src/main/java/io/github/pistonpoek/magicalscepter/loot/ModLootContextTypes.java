package io.github.pistonpoek.magicalscepter.loot;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextType;

import java.util.function.Consumer;

public class ModLootContextTypes {
    private static final BiMap<Identifier, ContextType> MAP = HashBiMap.create();

    public static ContextType SPELL_CAST = register(
            "spell_cast",
            builder -> builder.require(LootContextParameters.THIS_ENTITY)
                    .require(LootContextParameters.ORIGIN)
                    .require(LootContextParameters.TOOL)
                    .require(LootContextParameters.BLOCK_STATE)
    );

    public static void init() {

    }

    public static BiMap<Identifier, ContextType> getLootContextMap() {
        return MAP;
    }

    private static ContextType register(String name, Consumer<ContextType.Builder> type) {
        ContextType.Builder builder = new ContextType.Builder();
        type.accept(builder);
        ContextType contextType = builder.build();
        Identifier identifier = ModIdentifier.of(name);
        ContextType putContextType = MAP.put(identifier, contextType);
        if (putContextType != null) {
            throw new IllegalStateException("Loot table parameter set " + identifier + " is already registered");
        } else {
            return contextType;
        }
    }
}
