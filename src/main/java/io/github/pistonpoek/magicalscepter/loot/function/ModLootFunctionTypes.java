package io.github.pistonpoek.magicalscepter.loot.function;

import com.mojang.serialization.MapCodec;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.world.level.storage.loot.functions.LootItemFunctions
 */
public class ModLootFunctionTypes {
    public static final MapCodec<SetExperienceLootFunction> SET_EXPERIENCE =
            register("set_experience", SetExperienceLootFunction.CODEC);
    public static final MapCodec<SetScepterLootFunction> SET_SCEPTER =
            register("set_scepter", SetScepterLootFunction.CODEC);

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {
        MagicalScepter.LOGGER.info("Registering Loot Functions for " + ModIdentifier.MOD_NAME);
    }

    /**
     * Register a mod loot function type for the specified identifier.
     *
     * @param identifier String identifier to register for.
     * @param codec      Loot function codec to register.
     * @param <T>        Loot function to register.
     * @return Registered loot function type.
     */
    private static <T extends LootItemFunction> MapCodec<T> register(String identifier, MapCodec<T> codec) {
        return Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE,
                ModIdentifier.of(identifier), codec);
    }
}
