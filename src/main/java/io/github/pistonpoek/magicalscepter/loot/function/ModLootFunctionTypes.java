package io.github.pistonpoek.magicalscepter.loot.function;

import com.mojang.serialization.MapCodec;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.loot.function.LootFunctionTypes
 */
public class ModLootFunctionTypes {
    public static final LootFunctionType<SetScepterLootFunction> SET_SCEPTER =
            init("set_scepter", SetScepterLootFunction.CODEC);

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {
        MagicalScepter.LOGGER.info("Registering Loot Functions for " + ModIdentifier.MOD_NAME);
    }

    private static <T extends LootFunction> LootFunctionType<T> init(String id, MapCodec<T> codec) {
        return Registry.register(Registries.LOOT_FUNCTION_TYPE, ModIdentifier.of(id), new LootFunctionType<>(codec));
    }
}
