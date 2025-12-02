package io.github.pistonpoek.magicalscepter.advancement;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class ModAdvancements {
    public static final Identifier CAST_SCEPTER = ModIdentifier.of("adventure/cast_scepter");
    public static final Identifier ALL_SCEPTER_INFUSIONS = ModIdentifier.of("adventure/all_scepter_infusions");

    public static String createTranslationKey(Identifier identifier) {
        return Util.createTranslationKey("advancements", identifier);
    }

    public static String createTitleTranslationKey(Identifier identifier) {
        return createTranslationKey(identifier.withSuffixedPath(".title"));
    }

    public static String createDescriptionTranslationKey(Identifier identifier) {
        return createTranslationKey(identifier.withSuffixedPath(".description"));
    }
}
