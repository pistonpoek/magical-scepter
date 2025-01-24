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
    public static final String MOD_NAME = "MagicalScepter";

    /**
     * Create an identifier for the mod id {@value MOD_ID} with the specified path.
     *
     * @param path Path to create identifier for
     * @return Identifier that is validated
     *
     * @see net.minecraft.util.Identifier
     */
    public static net.minecraft.util.Identifier of(String path) {
        return net.minecraft.util.Identifier.of(MOD_ID, path);
    }

    public static String name(String path) {
        return MOD_ID + ":" + path;
    }

    public static String id(String id) {
        return MOD_ID + "_" + id;
    }

    public static MutableText translatable(String path, Object... args) {
        return Text.translatable(of(path).toTranslationKey(), args);
    }
}
