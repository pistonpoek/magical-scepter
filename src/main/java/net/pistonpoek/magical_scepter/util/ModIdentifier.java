package net.pistonpoek.magical_scepter.util;

public class ModIdentifier {
    /**
     * Identifier specified for this mod.
     */
    public static final String MOD_ID = "magical-scepter";

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
}
