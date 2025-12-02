package io.github.pistonpoek.magicalscepter.command;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.github.pistonpoek.magicalscepter.command.argument.ModRegistryEntryReferenceArgumentType;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import io.github.pistonpoek.magicalscepter.spell.cast.delay.SpellCastingManager;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
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
    public static final String CAST_FAILED_KEY = createTranslationKey("cast.failed");
    public static final String CLEAR_FAILED_KEY = createTranslationKey("clear.failed");
    public static final String CAST_SUCCESS_SINGLE_KEY = createTranslationKey("cast.success.single");
    public static final String CAST_SUCCESS_MULTIPLE_KEY = createTranslationKey("cast.success.multiple");
    public static final String CLEAR_SUCCESS_SINGLE_KEY = createTranslationKey("clear.success.single");
    public static final String CLEAR_SUCCESS_MULTIPLE_KEY = createTranslationKey("clear.success.multiple");

    private static final SimpleCommandExceptionType CAST_FAILED_EXCEPTION =
            new SimpleCommandExceptionType(Text.translatable(CAST_FAILED_KEY));
    private static final SimpleCommandExceptionType CLEAR_FAILED_EXCEPTION =
            new SimpleCommandExceptionType(Text.translatable(CLEAR_FAILED_KEY));

    /**
     * Register the spell command.
     *
     * @param dispatcher     Dispatcher to register the command to.
     * @param registryAccess Entry point to access registries used by the command.
     * @param environment    Environment that the command is being registered for.
     * @see net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
     */
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

    /**
     * Try to cast the specified spells for the specified group of entities.
     *
     * @param source   Server command source to send feedback to.
     * @param entities Entities selected to cast the spell.
     * @param spell    Spell to be cast by the entities.
     * @return Number of successful spell casts.
     * @throws CommandSyntaxException When no entities were able to cast the spell.
     */
    private static int executeCast(
            ServerCommandSource source,
            Collection<? extends Entity> entities,
            RegistryEntry<Spell> spell
    ) throws CommandSyntaxException {
        Spell spellInstance = spell.value();
        int successes = 0;

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) {
                spellInstance.castSpell(livingEntity);
                successes++;
            }
        }

        if (successes == 0) {
            throw CAST_FAILED_EXCEPTION.create();
        } else {
            if (entities.size() == 1) {
                source.sendFeedback(
                        () -> Text.translatable(CAST_SUCCESS_SINGLE_KEY,
                                Spell.getName(spell), entities.iterator().next().getDisplayName()),
                        true
                );
            } else {
                source.sendFeedback(() -> Text.translatable(CAST_SUCCESS_MULTIPLE_KEY,
                        Spell.getName(spell), entities.size()), true);
            }

            return successes;
        }
    }

    /**
     * Try to clear scheduled spells for the specified group of entities.
     *
     * @param source   Server command source to send feedback to.
     * @param entities Entities selected to clear scheduled spells.
     * @return Number of successful entities cleared.
     * @throws CommandSyntaxException When no entities had scheduled spell casts to clear.
     */
    private static int executeClear(ServerCommandSource source, Collection<? extends Entity> entities)
            throws CommandSyntaxException {
        int successes = 0;

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity && SpellCastingManager.clear(livingEntity)) {
                successes++;
            }
        }

        if (successes == 0) {
            throw CLEAR_FAILED_EXCEPTION.create();
        } else {
            if (entities.size() == 1) {
                source.sendFeedback(() -> Text.translatable(CLEAR_SUCCESS_SINGLE_KEY,
                        entities.iterator().next().getDisplayName()), true);
            } else {
                source.sendFeedback(() -> Text.translatable(CLEAR_SUCCESS_MULTIPLE_KEY,
                        entities.size()), true);
            }

            return successes;
        }
    }
    private static String createTranslationKey(String path) {
        return ModIdentifier.createTranslationKey("commands", "spell." + path);
    }
}
