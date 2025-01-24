package io.github.pistonpoek.magicalscepter.command;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.github.pistonpoek.magicalscepter.command.argument.ModRegistryEntryReferenceArgumentType;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import io.github.pistonpoek.magicalscepter.spell.cast.delay.SpellCastingManager;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;

public class SpellCommand {
    private static final SimpleCommandExceptionType CAST_FAILED_EXCEPTION =
            new SimpleCommandExceptionType(Text.translatable("commands.spell.cast.failed"));
    private static final SimpleCommandExceptionType CLEAR_FAILED_EXCEPTION =
            new SimpleCommandExceptionType(ModIdentifier.translatable("commands.spell.clear.failed")
    );

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess registryAccess,
                                CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
            CommandManager.literal("spell")
                .requires(source -> source.hasPermissionLevel(2))
                .then(
                    CommandManager.literal("clear")
                        .executes(context -> executeClear(
                            context.getSource(),
                            ImmutableList.of(context.getSource().getEntityOrThrow())))
                        .then(
                            CommandManager.argument("targets", EntityArgumentType.entities())
                                .executes(context -> executeClear(
                                    context.getSource(),
                                    EntityArgumentType.getEntities(context, "targets")))
                        )
                )
                .then(
                    CommandManager.literal("cast")
                        .then(
                            CommandManager.argument("targets", EntityArgumentType.entities())
                                .then(
                                    CommandManager.argument("spell", RegistryEntryReferenceArgumentType
                                                    .registryEntry(registryAccess, ModRegistryKeys.SPELL))
                                        .executes(
                                            context -> executeCast(
                                                context.getSource(),
                                                EntityArgumentType.getEntities(context, "targets"),
                                                ModRegistryEntryReferenceArgumentType.getSpell(context, "spell")
                                            )
                                        )
                                )
                        )
                )
        );
    }

    private static int executeCast(
            ServerCommandSource source,
            Collection<? extends Entity> targets,
            RegistryEntry<Spell> spell
    ) throws CommandSyntaxException {
        Spell spellInstance = spell.value();
        int successes = 0;

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity livingEntity) {
                spellInstance.castSpell(livingEntity);
                successes++;
            }
        }

        if (successes == 0) {
            throw CAST_FAILED_EXCEPTION.create();
        } else {
            if (targets.size() == 1) {
                source.sendFeedback(
                        () -> ModIdentifier.translatable("commands.spell.cast.success.single",
                                Spell.getName(spell), targets.iterator().next().getDisplayName()),
                        true
                );
            } else {
                source.sendFeedback(() -> ModIdentifier.translatable("commands.spell.cast.success.multiple",
                        Spell.getName(spell), targets.size()), true);
            }

            return successes;
        }
    }

    private static int executeClear(ServerCommandSource source, Collection<? extends Entity> targets)
            throws CommandSyntaxException {
        int successes = 0;

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity livingEntity && SpellCastingManager.clear(livingEntity)) {
                successes++;
            }
        }

        if (successes == 0) {
            throw CLEAR_FAILED_EXCEPTION.create();
        } else {
            if (targets.size() == 1) {
                source.sendFeedback(() -> ModIdentifier.translatable("commands.spell.clear.success.single",
                        (targets.iterator().next()).getDisplayName()), true);
            } else {
                source.sendFeedback(() -> ModIdentifier.translatable("commands.spell.clear.success.multiple",
                        targets.size()), true);
            }

            return successes;
        }
    }
}
