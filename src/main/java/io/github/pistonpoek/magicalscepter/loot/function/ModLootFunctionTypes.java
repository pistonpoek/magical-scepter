package io.github.pistonpoek.magicalscepter.loot.function;

import com.mojang.serialization.MapCodec;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;

public class ModLootFunctionTypes {
    public static void init() {
        MagicalScepter.LOGGER.info("Registering Loot Functions for " + ModIdentifier.MOD_ID);
    }

    public static final LootFunctionType<SetScepterLootFunction> SET_SCEPTER =
            init("set_scepter", SetScepterLootFunction.CODEC);

    private static <T extends LootFunction> LootFunctionType<T> init(String id, MapCodec<T> codec) {
        return Registry.register(Registries.LOOT_FUNCTION_TYPE, ModIdentifier.of(id), new LootFunctionType<>(codec));
    }
}
