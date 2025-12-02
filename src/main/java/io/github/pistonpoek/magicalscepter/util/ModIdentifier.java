package io.github.pistonpoek.magicalscepter.util;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.util.Identifier
 */
public class ModIdentifier {
    /**
     * Identifier specified for this mod.
     */
    public static final String MOD_IDENTIFIER = "magicalscepter";

    /**
     * Name specified for this mod.
     */
    public static final String MOD_NAME = "MagicalScepter";

    /**
     * Create an identifier with mod namespace {@value MOD_IDENTIFIER} with the specified path.
     *
     * @param path Path to create identifier for.
     * @return Identifier that is validated.
     * @see net.minecraft.util.Identifier
     */
    public static net.minecraft.util.Identifier of(String path) {
        return net.minecraft.util.Identifier.of(MOD_IDENTIFIER, path);
    }

    /**
     * Create the string representation for the identifier with mod namespace {@value MOD_IDENTIFIER} and the specified path.
     *
     * @param path String to create mod identifier string representation for.
     * @return String that represents the mod identifier with the specified path.
     * @see #of(String)
     * @see #key(String, String)
     */
    public static String identifier(String path) {
        return key(path, ":");
    }

    /**
     * Create a string key for the mod with id {@value MOD_IDENTIFIER} and the specified value.
     *
     * @param value     String to create mod represented key for.
     * @param delimiter String to separate the mod id from the specified value.
     * @return String key that joins the specified value with the mod id using the specified delimiter.
     */
    public static String key(String value, String delimiter) {
        return String.join(delimiter, MOD_IDENTIFIER, value);
    }

    /**
     * Create a mutable text for the specified path in the {@value MOD_IDENTIFIER} namespace with arguments to use in the text.
     *
     * @param path String to create mutable text with.
     * @param args Arguments to create mutable text with.
     * @return MutableText created for the path and arguments with the mod namespace.
     * @see net.minecraft.text.Text#translatable(String, Object...)
     */
    public static MutableText translatable(String path, Object... args) {
        return Text.translatable(of(path).toTranslationKey(), args);
    }

    public static String createTranslationKey(String type, String path) {
        return Util.createTranslationKey(type, ModIdentifier.of(path));
    }

    public static String translationKey(String path) {
        return String.join(".", MOD_IDENTIFIER, path.replace("/", "."));
    }
}
