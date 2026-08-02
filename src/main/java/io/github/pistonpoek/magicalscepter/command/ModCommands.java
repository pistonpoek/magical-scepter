package io.github.pistonpoek.magicalscepter.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.server.command.CommandManager
 */
public class ModCommands {

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {
        CommandRegistrationCallback.EVENT.register(SpellCommand::register);
    }
}
