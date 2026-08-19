package io.github.pistonpoek.gametest.magicalscepter;

import io.github.pistonpoek.gametest.TestBlockChecker;
import io.github.pistonpoek.magicalscepter.entity.ModEntityTypes;
import io.github.pistonpoek.magicalscepter.entity.mob.SorcererEntity;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Items;
import java.lang.reflect.Method;

public class SorcererTest implements CustomTestMethodInvoker {
    @Override
    public void invokeTestMethod(GameTestHelper context, Method method) throws ReflectiveOperationException {
        TestBlockChecker checker = new TestBlockChecker(context);
        method.invoke(this, context, checker);
    }

    @GameTest(structure="gametest:sorcerer/exists")
    public void exists(GameTestHelper context, TestBlockChecker checker) {
        context.assertEntityPresent(ModEntityTypes.SORCERER, BlockPos.containing(0, 1, 0));
        context.succeed();
    }

    @GameTest(structure="gametest:sorcerer/spawns")
    public void spawns(GameTestHelper context, TestBlockChecker checker) {
        context.spawnWithNoFreeWill(ModEntityTypes.SORCERER, BlockPos.containing(0, 1, 0));
        context.assertEntityPresent(ModEntityTypes.SORCERER, BlockPos.containing(0, 1, 0));
        context.succeed();
    }

    @GameTest(structure="gametest:sorcerer/spawn_with_command_block")
    public void spawnWithCommandBlock(GameTestHelper context, TestBlockChecker checker) {
        checker.start();
        context.succeedWhenEntityPresent(ModEntityTypes.SORCERER, BlockPos.containing(0, 3, 0));
    }

    @GameTest(structure="gametest:sorcerer/spawn_with_dispenser")
    public void spawnWithDispenser(GameTestHelper context, TestBlockChecker checker) {
        checker.start();
        context.succeedWhenEntityPresent(ModEntityTypes.SORCERER, BlockPos.containing(0, 3, 0));
    }

    @GameTest(structure="gametest:sorcerer/exists")
    public void dropsLapisLazuli(GameTestHelper context, TestBlockChecker checker) {
        SorcererEntity sorcerer = context.getEntities(ModEntityTypes.SORCERER).getFirst();
        context.kill(sorcerer);
        context.assertItemEntityPresent(Items.LAPIS_LAZULI);
        context.succeed();
    }

    @GameTest(structure="gametest:sorcerer/spawns")
    public void dropsBrownMushroom(GameTestHelper context, TestBlockChecker checker) {
        for (int i = 0; i < 20; i++) context.spawnWithNoFreeWill(ModEntityTypes.SORCERER, BlockPos.containing(0, 1, 0));
        context.killAllEntitiesOfClass(SorcererEntity.class);
        context.assertItemEntityPresent(Items.BROWN_MUSHROOM);
        context.succeed();
    }
}
