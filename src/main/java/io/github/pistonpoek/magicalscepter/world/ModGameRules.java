package io.github.pistonpoek.magicalscepter.world;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.world.GameRules
 */
public class ModGameRules {
    public static final GameRules.Key<GameRules.IntRule> MAX_SPELL_CASTS = register(
            "maxSpellCasts", GameRules.Category.MISC, GameRules.IntRule.create(65536)
    );

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {

    }

    public static String getTranslationKey(GameRules.Key<?> gamerule) {
        return gamerule.getTranslationKey();
    }

    public static String getDescriptionTranslationKey(GameRules.Key<?> gamerule) {
        return gamerule.getTranslationKey() + ".description";
    }

    private static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Category category, GameRules.Type<T> type) {
        return GameRuleRegistry.register(name, category, type);
    }
}
