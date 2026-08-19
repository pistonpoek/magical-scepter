package io.github.pistonpoek.magicalscepter.world;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
import net.minecraft.world.level.gamerules.GameRules;

import java.util.function.ToIntFunction;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.world.level.gamerules.GameRules
 */
public class ModGameRules {
    public static final GameRule<Integer> MAX_SPELL_CASTS = registerIntRule(
            "max_spell_casts", GameRuleCategory.MISC, 65536, 0);

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {

    }

    public static String getDescriptionTranslationKey(GameRule<?> gameRule) {
        return gameRule.getDescriptionId() + ".description";
    }

    private static GameRule<Boolean> registerBooleanRule(String name, GameRuleCategory category, boolean defaultValue) {
        return register(
                name,
                category,
                GameRuleType.BOOL,
                BoolArgumentType.bool(),
                Codec.BOOL,
                defaultValue,
                FeatureFlagSet.of(),
                GameRuleTypeVisitor::visitBoolean,
                value -> value ? 1 : 0
        );
    }

    private static GameRule<Integer> registerIntRule(String name, GameRuleCategory category, int defaultValue,
                                                     int minValue) {
        return registerIntRule(name, category, defaultValue, minValue, Integer.MAX_VALUE, FeatureFlagSet.of());
    }

    private static GameRule<Integer> registerIntRule(String name, GameRuleCategory category, int defaultValue,
                                                     int minValue, int maxValue) {
        return registerIntRule(name, category, defaultValue, minValue, maxValue, FeatureFlagSet.of());
    }

    private static GameRule<Integer> registerIntRule(String name, GameRuleCategory category, int defaultValue,
                                                     int minValue, int maxValue, FeatureFlagSet requiredFeatures
    ) {
        return register(
                name,
                category,
                GameRuleType.INT,
                IntegerArgumentType.integer(minValue, maxValue),
                Codec.intRange(minValue, maxValue),
                defaultValue,
                requiredFeatures,
                GameRuleTypeVisitor::visitInteger,
                value -> value
        );
    }

    private static <T> GameRule<T> register(
            String name,
            GameRuleCategory category,
            GameRuleType type,
            ArgumentType<T> argumentType,
            Codec<T> codec,
            T defaultValue,
            FeatureFlagSet requiredFeatures,
            GameRules.VisitorCaller<T> acceptor,
            ToIntFunction<T> commandResultSupplier
    ) {
        return Registry.register(
                BuiltInRegistries.GAME_RULE, ModIdentifier.of(name),
                new GameRule<>(category, type, argumentType, acceptor, codec, commandResultSupplier,
                        defaultValue, requiredFeatures)
        );
    }
}
