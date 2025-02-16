package io.github.pistonpoek.magicalscepter.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.ServerCommandSource;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.command.argument.RegistryEntryReferenceArgumentType
 */
public class ModRegistryEntryReferenceArgumentType {
    /**
     * Get a spell from the specified name in the specified command context.
     *
     * @param context Command context to get the spell in.
     * @param name String to find a spell for.
     * @return Spell registry reference.
     * @throws CommandSyntaxException When spell registry entry could not be found for the name.
     */
    public static RegistryEntry.Reference<Spell> getSpell(
            CommandContext<ServerCommandSource> context, String name) throws CommandSyntaxException {
        return RegistryEntryReferenceArgumentType.getRegistryEntry(context, name, ModRegistryKeys.SPELL);
    }
}
