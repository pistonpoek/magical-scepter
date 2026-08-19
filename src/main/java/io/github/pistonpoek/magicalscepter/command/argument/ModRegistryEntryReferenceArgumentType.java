package io.github.pistonpoek.magicalscepter.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.commands.arguments.ResourceArgument
 */
public class ModRegistryEntryReferenceArgumentType {
    /**
     * Get a spell from the specified name in the specified command context.
     *
     * @param context Command context to get the spell in.
     * @param name    String to find a spell for.
     * @return Spell registry reference.
     * @throws CommandSyntaxException When spell registry entry could not be found for the name.
     */
    public static Holder.Reference<Spell> getSpell(
            CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return ResourceArgument.getResource(context, name, ModRegistryKeys.SPELL);
    }
}
