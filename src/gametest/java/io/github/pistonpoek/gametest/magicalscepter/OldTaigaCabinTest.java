package io.github.pistonpoek.gametest.magicalscepter;

import io.github.pistonpoek.gametest.TestBlockChecker;
import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import java.lang.reflect.Method;
import java.util.List;

public class OldTaigaCabinTest implements CustomTestMethodInvoker {
    @Override
    public void invokeTestMethod(GameTestHelper context, Method method) throws ReflectiveOperationException {
        TestBlockChecker checker = new TestBlockChecker(context);
        method.invoke(this, context, checker);
    }

    @GameTest(structure="gametest:old_taiga_cabin/generate_with_structure_block")
    public void generateWithStructureBlock(GameTestHelper context, TestBlockChecker checker) {
        checker.start();
        context.assertEntityPresent(ModEntityType.SORCERER, BlockPos.containing(2, 1, 3));
        context.assertBlockPresent(Blocks.CRAFTING_TABLE, BlockPos.containing(3, 1, 2));
        context.assertBlockPresent(Blocks.CHEST, BlockPos.containing(4, 1, 2));
        context.assertBlockPresent(Blocks.POTTED_BROWN_MUSHROOM, BlockPos.containing(1, 2, 3));
        context.assertBlockPresent(Blocks.COBBLESTONE, BlockPos.containing(4, 0, 5));
        context.assertBlockPresent(Blocks.SPRUCE_LOG, BlockPos.containing(2, 5, 4));
        context.assertBlockPresent(Blocks.SPRUCE_PLANKS, BlockPos.containing(5, 2, 3));
        context.assertBlockPresent(Blocks.SPRUCE_FENCE, BlockPos.containing(3, 4, 1));
        context.assertBlockPresent(Blocks.SPRUCE_SLAB, BlockPos.containing(3, 3, 7));
        context.assertBlockPresent(Blocks.SPRUCE_FENCE, BlockPos.containing(2, 2, 6));
        context.assertBlockPresent(Blocks.AIR, BlockPos.containing(4, 2, 6));

        context.destroyBlock(BlockPos.containing(4, 1, 2));
        checkLootItems(context);
        context.succeed();
    }

    @GameTest(structure="gametest:old_taiga_cabin/generate_with_structure_block_mirrored")
    public void generateWithStructureBlockMirrored(GameTestHelper context, TestBlockChecker checker) {
        checker.start();
        context.assertEntityPresent(ModEntityType.SORCERER, BlockPos.containing(4, 1, 3));
        context.assertBlockPresent(Blocks.CRAFTING_TABLE, BlockPos.containing(3, 1, 2));
        context.assertBlockPresent(Blocks.CHEST, BlockPos.containing(2, 1, 2));
        context.assertBlockPresent(Blocks.POTTED_BROWN_MUSHROOM, BlockPos.containing(5, 2, 3));
        context.assertBlockPresent(Blocks.COBBLESTONE, BlockPos.containing(2, 0, 5));
        context.assertBlockPresent(Blocks.SPRUCE_LOG, BlockPos.containing(4, 5, 4));
        context.assertBlockPresent(Blocks.SPRUCE_PLANKS, BlockPos.containing(1, 2, 3));
        context.assertBlockPresent(Blocks.SPRUCE_FENCE, BlockPos.containing(3, 4, 1));
        context.assertBlockPresent(Blocks.SPRUCE_SLAB, BlockPos.containing(3, 3, 7));
        context.assertBlockPresent(Blocks.SPRUCE_FENCE, BlockPos.containing(4, 2, 6));
        context.assertBlockPresent(Blocks.AIR, BlockPos.containing(2, 2, 6));

        context.destroyBlock(BlockPos.containing(2, 1, 2));
        checkLootItems(context);
        context.succeed();
    }

    @GameTest(structure="gametest:old_taiga_cabin/loot_with_chest")
    public void lootWithChest(GameTestHelper context, TestBlockChecker checker) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        context.useBlock(BlockPos.containing(0, 1, 0), player);
        context.destroyBlock(BlockPos.containing(0, 1, 0));
        checkLootItems(context);
        context.succeed();
    }

    @GameTest(structure="gametest:old_taiga_cabin/loot_with_command")
    public void lootWithCommand(GameTestHelper context, TestBlockChecker checker) {
        checker.start();
        context.succeedIf(() -> checkLootItems(context));
        context.succeed();
    }

    private void checkLootItems(GameTestHelper context) {
        List<ItemEntity> itemEntities = context.getEntities(EntityType.ITEM);
        List<Item> items = itemEntities.stream().map(ItemEntity::getItem).map(ItemStack::getItem).toList();

        context.assertTrue(items.contains(ModItems.SCEPTER) ||
                        items.contains(ModItems.MAGICAL_SCEPTER),
                Component.nullToEmpty("Loot does not contain scepter or magical scepter!"));

        context.assertTrue(items.contains(Items.LAPIS_LAZULI) ||
                        items.contains(Items.BROWN_MUSHROOM) ||
                        items.contains(Items.EXPERIENCE_BOTTLE),
                Component.nullToEmpty("Loot does not contain magical essence!"));

        context.assertTrue(items.contains(Items.ROTTEN_FLESH) ||
                        items.contains(Items.BONE) ||
                        items.contains(Items.STRING) ||
                        items.contains(Items.GUNPOWDER),
                Component.nullToEmpty("Loot does not contain mob drops!"));

        context.assertTrue(items.contains(Items.SWEET_BERRIES) ||
                        items.contains(Items.BREAD) ||
                        items.contains(Items.WHEAT),
                Component.nullToEmpty("Loot does not contain local food!"));

        context.assertTrue(items.contains(Items.EMERALD) ||
                        items.contains(Items.MAP) ||
                        items.contains(Items.COAL),
                Component.nullToEmpty("Loot does not contain exploration treasure!"));
    }
}


