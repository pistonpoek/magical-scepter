package io.github.pistonpoek.gametest.magicalscepter;

import io.github.pistonpoek.gametest.TestBlockChecker;
import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import io.github.pistonpoek.magicalscepter.entity.mob.SorcererEntity;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.item.Items;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Method;

/**
 * TODO
 */
public class SorcererTest implements CustomTestMethodInvoker {
    @Override
    public void invokeTestMethod(TestContext context, Method method) throws ReflectiveOperationException {
        TestBlockChecker checker = new TestBlockChecker(context);
        method.invoke(this, context, checker);
    }

    @GameTest(structure="gametest:sorcerer/exists")
    public void exists(TestContext context, TestBlockChecker checker) {
        context.expectEntityAt(ModEntityType.SORCERER, BlockPos.ofFloored(0, 1, 0));
        context.complete();
    }

    @GameTest(structure="gametest:sorcerer/spawns")
    public void spawns(TestContext context, TestBlockChecker checker) {
        context.spawnMob(ModEntityType.SORCERER, BlockPos.ofFloored(0, 1, 0));
        context.expectEntityAt(ModEntityType.SORCERER, BlockPos.ofFloored(0, 1, 0));
        context.complete();
    }

    @GameTest(structure="gametest:sorcerer/spawn_with_command_block")
    public void spawnWithCommandBlock(TestContext context, TestBlockChecker checker) {
        checker.start();
        context.expectEntityAtEnd(ModEntityType.SORCERER, BlockPos.ofFloored(0, 3, 0));
    }

    @GameTest(structure="gametest:sorcerer/spawn_with_dispenser")
    public void spawnWithDispenser(TestContext context, TestBlockChecker checker) {
        checker.start();
        context.expectEntityAtEnd(ModEntityType.SORCERER, BlockPos.ofFloored(0, 3, 0));
    }

    @GameTest(structure="gametest:sorcerer/exists")
    public void dropsLapisLazuli(TestContext context, TestBlockChecker checker) {
        SorcererEntity sorcerer = context.getEntities(ModEntityType.SORCERER).getFirst();
        context.killEntity(sorcerer);
        context.expectItem(Items.LAPIS_LAZULI);
        context.complete();
    }

    @GameTest(structure="gametest:sorcerer/spawns")
    public void dropsBrownMushroom(TestContext context, TestBlockChecker checker) {
        for (int i = 0; i < 20; i++) context.spawnMob(ModEntityType.SORCERER, BlockPos.ofFloored(0, 1, 0));
        context.killAllEntities(SorcererEntity.class);
        context.expectItem(Items.BROWN_MUSHROOM);
        context.complete();
    }
}
