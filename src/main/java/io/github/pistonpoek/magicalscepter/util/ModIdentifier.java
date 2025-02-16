package io.github.pistonpoek.magicalscepter.util;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.util.Identifier
 */
public class ModIdentifier {
    /**
     * Identifier specified for this mod.
     */
    public static final String MOD_ID = "magicalscepter";

    /**
     * Name specified for this mod.
     */
    public static final String MOD_NAME = "MagicalScepter";

    /**
     * Create an identifier with mod namespace {@value MOD_ID} with the specified path.
     *
     * @param path Path to create identifier for.
     * @return Identifier that is validated.
     *
     * @see net.minecraft.util.Identifier
     */
    public static net.minecraft.util.Identifier of(String path) {
        return net.minecraft.util.Identifier.of(MOD_ID, path);
    }

    /**
     * Create the string representation for the identifier with mod namespace {@value MOD_ID} and the specified path.
     *
     * @param path String to create mod identifier string representation for.
     * @return String that represents the mod identifier with the specified path.
     * 
     * @see #of(String)
     * @see #key(String, String) 
     */
    public static String id(String path) {
        return key(path, ":");
    }

    /**
     * Create a string key for the mod with id {@value MOD_ID} and the specified value.
     *
     * @param value String to create mod represented key for.
     * @param delimiter String to separate the mod id from the specified value.
     * @return String key that joins the specified value with the mod id using the specified delimiter.
     */
    public static String key(String value, String delimiter) {
        return String.join(delimiter, MOD_ID, value);
    }

    /**
     * Create a mutable text for the specified path in the {@value MOD_ID} namespace with arguments to use in the text.
     *
     * @param path String to create mutable text with.
     * @param args Arguments to create mutable text with.
     * @return MutableText created for the path and arguments with the mod namespace.
     *
     * @see net.minecraft.text.Text#translatable(String, Object...)
     */
    public static MutableText translatable(String path, Object... args) {
        return Text.translatable(of(path).toTranslationKey(), args);
    }
}
