package io.github.pistonpoek.magicalscepter.command;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.github.pistonpoek.magicalscepter.advancement.criterion.ModCriteria;
import io.github.pistonpoek.magicalscepter.command.argument.ModRegistryEntryReferenceArgumentType;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import io.github.pistonpoek.magicalscepter.spell.cast.delay.SpellCastingManager;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import java.util.Collection;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Command that can be used by living entities to cast or clear spells.
 */
public class SpellCommand {
    public static final String CAST_FAILED_KEY = createTranslationKey("cast.failed");
    public static final String CLEAR_FAILED_KEY = createTranslationKey("clear.failed");
    public static final String CAST_SUCCESS_SINGLE_KEY = createTranslationKey("cast.success.single");
    public static final String CAST_SUCCESS_MULTIPLE_KEY = createTranslationKey("cast.success.multiple");
    public static final String CLEAR_SUCCESS_SINGLE_KEY = createTranslationKey("clear.success.single");
    public static final String CLEAR_SUCCESS_MULTIPLE_KEY = createTranslationKey("clear.success.multiple");

    private static final SimpleCommandExceptionType CAST_FAILED_EXCEPTION =
            new SimpleCommandExceptionType(Component.translatable(CAST_FAILED_KEY));
    private static final SimpleCommandExceptionType CLEAR_FAILED_EXCEPTION =
            new SimpleCommandExceptionType(Component.translatable(CLEAR_FAILED_KEY));

    /**
     * Register the spell command.
     *
     * @param dispatcher     Dispatcher to register the command to.
     * @param registryAccess Entry point to access registries used by the command.
     * @param environment    Environment that the command is being registered for.
     * @see net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
     */
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher,
                                CommandBuildContext registryAccess,
                                Commands.CommandSelection environment) {
        dispatcher.register(
                Commands.literal("spell")
                        .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(
                                Commands.literal("clear")
                                        .executes(context -> executeClear(
                                                context.getSource(),
                                                ImmutableList.of(context.getSource().getEntityOrException())))
                                        .then(
                                                Commands.argument("targets", EntityArgument.entities())
                                                        .executes(context -> executeClear(
                                                                context.getSource(),
                                                                EntityArgument.getEntities(context, "targets")))
                                        )
                        )
                        .then(
                                Commands.literal("cast")
                                        .then(
                                                Commands.argument("targets", EntityArgument.entities())
                                                        .then(
                                                                Commands.argument("spell", ResourceArgument
                                                                                .resource(registryAccess, ModRegistryKeys.SPELL))
                                                                        .executes(
                                                                                context -> executeCast(
                                                                                        context.getSource(),
                                                                                        EntityArgument.getEntities(context, "targets"),
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
            CommandSourceStack source,
            Collection<? extends Entity> entities,
            Holder<Spell> spell
    ) throws CommandSyntaxException {
        Spell spellInstance = spell.value();
        int successes = 0;

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) {
                spellInstance.castSpell(livingEntity);
                successes++;
                if (livingEntity instanceof ServerPlayer serverPlayerEntity) {
                    ModCriteria.CAST_SCEPTER.trigger(serverPlayerEntity, ItemStack.EMPTY);
                }
            }
        }

        if (successes == 0) {
            throw CAST_FAILED_EXCEPTION.create();
        } else {
            if (entities.size() == 1) {
                source.sendSuccess(
                        () -> Component.translatable(CAST_SUCCESS_SINGLE_KEY,
                                Spell.getName(spell), entities.iterator().next().getDisplayName()),
                        true
                );
            } else {
                source.sendSuccess(() -> Component.translatable(CAST_SUCCESS_MULTIPLE_KEY,
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
    private static int executeClear(CommandSourceStack source, Collection<? extends Entity> entities)
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
                source.sendSuccess(() -> Component.translatable(CLEAR_SUCCESS_SINGLE_KEY,
                        entities.iterator().next().getDisplayName()), true);
            } else {
                source.sendSuccess(() -> Component.translatable(CLEAR_SUCCESS_MULTIPLE_KEY,
                        entities.size()), true);
            }

            return successes;
        }
    }

    /**
     * Create a spell commands translation key for the specified path.
     *
     * @param path String path to create the translation key with.
     * @return String translation key based on the specified path for the spell command.
     */
    private static String createTranslationKey(String path) {
        return ModIdentifier.createTranslationKey("commands", "spell." + path);
    }
}
