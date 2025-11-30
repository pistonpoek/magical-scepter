package io.github.pistonpoek.gametest.magicalscepter;

import io.github.pistonpoek.gametest.TestBlockChecker;
import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

import java.lang.reflect.Method;
import java.util.List;

public class OldTaigaCabinTest implements CustomTestMethodInvoker {
    @Override
    public void invokeTestMethod(TestContext context, Method method) throws ReflectiveOperationException {
        TestBlockChecker checker = new TestBlockChecker(context);
        method.invoke(this, context, checker);
    }

    @GameTest(structure="gametest:old_taiga_cabin/generate_with_structure_block")
    public void generateWithStructureBlock(TestContext context, TestBlockChecker checker) {
        checker.start();
        context.expectEntityAt(ModEntityType.SORCERER, BlockPos.ofFloored(2, 1, 3));
        context.expectBlock(Blocks.CRAFTING_TABLE, BlockPos.ofFloored(3, 1, 2));
        context.expectBlock(Blocks.CHEST, BlockPos.ofFloored(4, 1, 2));
        context.expectBlock(Blocks.POTTED_BROWN_MUSHROOM, BlockPos.ofFloored(1, 2, 3));
        context.expectBlock(Blocks.COBBLESTONE, BlockPos.ofFloored(4, 0, 5));
        context.expectBlock(Blocks.SPRUCE_LOG, BlockPos.ofFloored(2, 5, 4));
        context.expectBlock(Blocks.SPRUCE_PLANKS, BlockPos.ofFloored(5, 2, 3));
        context.expectBlock(Blocks.SPRUCE_FENCE, BlockPos.ofFloored(3, 4, 1));
        context.expectBlock(Blocks.SPRUCE_SLAB, BlockPos.ofFloored(3, 3, 7));
        context.expectBlock(Blocks.SPRUCE_FENCE, BlockPos.ofFloored(2, 2, 6));
        context.expectBlock(Blocks.AIR, BlockPos.ofFloored(4, 2, 6));

        context.removeBlock(BlockPos.ofFloored(4, 1, 2));
        checkLootItems(context);
        context.complete();
    }

    @GameTest(structure="gametest:old_taiga_cabin/generate_with_structure_block_mirrored")
    public void generateWithStructureBlockMirrored(TestContext context, TestBlockChecker checker) {
        checker.start();
        context.expectEntityAt(ModEntityType.SORCERER, BlockPos.ofFloored(4, 1, 3));
        context.expectBlock(Blocks.CRAFTING_TABLE, BlockPos.ofFloored(3, 1, 2));
        context.expectBlock(Blocks.CHEST, BlockPos.ofFloored(2, 1, 2));
        context.expectBlock(Blocks.POTTED_BROWN_MUSHROOM, BlockPos.ofFloored(5, 2, 3));
        context.expectBlock(Blocks.COBBLESTONE, BlockPos.ofFloored(2, 0, 5));
        context.expectBlock(Blocks.SPRUCE_LOG, BlockPos.ofFloored(4, 5, 4));
        context.expectBlock(Blocks.SPRUCE_PLANKS, BlockPos.ofFloored(1, 2, 3));
        context.expectBlock(Blocks.SPRUCE_FENCE, BlockPos.ofFloored(3, 4, 1));
        context.expectBlock(Blocks.SPRUCE_SLAB, BlockPos.ofFloored(3, 3, 7));
        context.expectBlock(Blocks.SPRUCE_FENCE, BlockPos.ofFloored(4, 2, 6));
        context.expectBlock(Blocks.AIR, BlockPos.ofFloored(2, 2, 6));

        context.removeBlock(BlockPos.ofFloored(2, 1, 2));
        checkLootItems(context);
        context.complete();
    }

    @GameTest(structure="gametest:old_taiga_cabin/loot_with_chest")
    public void lootWithChest(TestContext context, TestBlockChecker checker) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        context.useBlock(BlockPos.ofFloored(0, 1, 0), player);
        context.removeBlock(BlockPos.ofFloored(0, 1, 0));
        checkLootItems(context);
        context.complete();
    }

    @GameTest(structure="gametest:old_taiga_cabin/loot_with_command")
    public void lootWithCommand(TestContext context, TestBlockChecker checker) {
        checker.start();
        context.addFinalTask(() -> checkLootItems(context));
        context.complete();
    }

    private void checkLootItems(TestContext context) {
        List<ItemEntity> itemEntities = context.getEntities(EntityType.ITEM);
        List<Item> items = itemEntities.stream().map(ItemEntity::getStack).map(ItemStack::getItem).toList();

        context.assertTrue(items.contains(ModItems.SCEPTER) ||
                        items.contains(ModItems.MAGICAL_SCEPTER),
                Text.of("Loot does not contain scepter or magical scepter!"));

        context.assertTrue(items.contains(Items.LAPIS_LAZULI) ||
                        items.contains(Items.BROWN_MUSHROOM) ||
                        items.contains(Items.EXPERIENCE_BOTTLE),
                Text.of("Loot does not contain magical essence!"));

        context.assertTrue(items.contains(Items.ROTTEN_FLESH) ||
                        items.contains(Items.BONE) ||
                        items.contains(Items.STRING) ||
                        items.contains(Items.GUNPOWDER),
                Text.of("Loot does not contain mob drops!"));

        context.assertTrue(items.contains(Items.SWEET_BERRIES) ||
                        items.contains(Items.BREAD) ||
                        items.contains(Items.WHEAT),
                Text.of("Loot does not contain local food!"));

        context.assertTrue(items.contains(Items.EMERALD) ||
                        items.contains(Items.MAP) ||
                        items.contains(Items.COAL),
                Text.of("Loot does not contain exploration treasure!"));
    }
}


