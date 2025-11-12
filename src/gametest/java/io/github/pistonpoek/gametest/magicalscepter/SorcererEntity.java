package io.github.pistonpoek.gametest.magicalscepter;

import io.github.pistonpoek.gametest.TestBlockChecker;
import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Method;

public class SorcererEntity implements CustomTestMethodInvoker {
    @Override
    public void invokeTestMethod(TestContext context, Method method) throws ReflectiveOperationException {
        method.invoke(this, context);
    }

    @GameTest(structure="gametest:sorcerer/exists")
    public void exists(TestContext context) {
        context.expectEntityAt(ModEntityType.SORCERER, BlockPos.ofFloored(0, 1, 0));
        context.complete();
    }

    @GameTest(structure="gametest:sorcerer/spawns")
    public void spawns(TestContext context) {
        context.spawnMob(ModEntityType.SORCERER, BlockPos.ofFloored(0, 1, 0));
        context.expectEntityAt(ModEntityType.SORCERER, BlockPos.ofFloored(0, 1, 0));
        context.complete();
    }

    @GameTest(structure="gametest:sorcerer/spawn_with_command_block")
    public void spawnWithCommandBlock(TestContext context) {
        new TestBlockChecker(context).start();
        context.expectEntityAtEnd(ModEntityType.SORCERER, BlockPos.ofFloored(0, 3, 0));
    }

    @GameTest(structure="gametest:sorcerer/spawn_with_dispenser")
    public void spawnWithDispenser(TestContext context) {
        new TestBlockChecker(context).start();
        context.expectEntityAtEnd(ModEntityType.SORCERER, BlockPos.ofFloored(0, 3, 0));
    }
}
