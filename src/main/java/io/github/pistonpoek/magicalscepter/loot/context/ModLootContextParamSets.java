package io.github.pistonpoek.magicalscepter.loot.context;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.function.Consumer;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
 */
public class ModLootContextParamSets {
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
     * Register a mod context key set for the specified name.
     *
     * @param name String name to register for.
     * @param consumer Context key set builder consumer to register.
     * @return Registered context key set.
     */
    private static ContextKeySet register(final String name, final Consumer<ContextKeySet.Builder> consumer) {
        ResourceKey<ContextKeySet> key = ResourceKey.create(Registries.CONTEXT_KEY_SET, ModIdentifier.of(name));
        return register(key, consumer);
    }

    /**
     * Register a mod context key set for the specified name.
     *
     * @param key Resource key to register for.
     * @param consumer Context key set builder consumer to register.
     * @return Registered context key set.
     */
    private static ContextKeySet register(final ResourceKey<ContextKeySet> key, final Consumer<ContextKeySet.Builder> consumer) {
        ContextKeySet.Builder builder = new ContextKeySet.Builder();
        consumer.accept(builder);
        return Registry.register(BuiltInRegistries.CONTEXT_KEY_SET, key, builder.build());
    }
}
