package io.github.pistonpoek.magicalscepter.loot.function;

import com.mojang.serialization.MapCodec;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.loot.function.LootFunctionTypes
 */
public class ModLootFunctionTypes {
    public static final LootFunctionType<SetExperienceLootFunction> SET_EXPERIENCE =
            register("set_experience", SetExperienceLootFunction.CODEC);
    public static final LootFunctionType<SetScepterLootFunction> SET_SCEPTER =
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
    private static <T extends LootFunction> LootFunctionType<T> register(String identifier, MapCodec<T> codec) {
        return Registry.register(Registries.LOOT_FUNCTION_TYPE,
                ModIdentifier.of(identifier), new LootFunctionType<>(codec));
    }
}
