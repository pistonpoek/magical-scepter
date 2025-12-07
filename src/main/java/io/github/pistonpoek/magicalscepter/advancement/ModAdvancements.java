package io.github.pistonpoek.magicalscepter.advancement;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

/**
 * Identifier collection of mod advancements and helper methods for creating matching translation keys.
 */
public interface ModAdvancements {
    Identifier CAST_SCEPTER = ModIdentifier.of("adventure/cast_scepter");
    Identifier ALL_SCEPTER_INFUSIONS = ModIdentifier.of("adventure/all_scepter_infusions");

    /**
     * Create an advancements translation key for the specified identifier.
     *
     * @param identifier Identifier to create translation key with.
     * @return String translation key of the specified identifier with the advancements type.
     */
    static String createTranslationKey(Identifier identifier) {
        return Util.createTranslationKey("advancements", identifier);
    }

    /**
     * Create an advancements title translation key for the specified identifier.
     *
     * @param identifier Identifier to create translation key with.
     * @return String translation key of the specified identifier with the advancements type and title detail.
     */
    static String createTitleTranslationKey(Identifier identifier) {
        return createTranslationKey(identifier.withSuffixedPath(".title"));
    }

    /**
     * Create an advancements description translation key for the specified identifier.
     *
     * @param identifier Identifier to create translation key with.
     * @return String translation key of the specified identifier with the advancements type and description detail.
     */
    static String createDescriptionTranslationKey(Identifier identifier) {
        return createTranslationKey(identifier.withSuffixedPath(".description"));
    }
}
