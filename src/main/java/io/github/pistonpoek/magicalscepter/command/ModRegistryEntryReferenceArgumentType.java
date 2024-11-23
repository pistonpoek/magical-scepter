package io.github.pistonpoek.magicalscepter.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.ServerCommandSource;

public class ModRegistryEntryReferenceArgumentType {
    public static RegistryEntry.Reference<Spell> getSpell(
            CommandContext<ServerCommandSource> context, String name) throws CommandSyntaxException {
        return RegistryEntryReferenceArgumentType.getRegistryEntry(context, name, ModRegistryKeys.SPELL);
    }
}
