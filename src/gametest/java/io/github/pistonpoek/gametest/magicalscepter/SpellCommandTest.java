package io.github.pistonpoek.gametest.magicalscepter;

import io.github.pistonpoek.gametest.TestBlockChecker;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.test.TestContext;

import java.lang.reflect.Method;

public class SpellCommandTest implements CustomTestMethodInvoker {
    @Override
    public void invokeTestMethod(TestContext context, Method method) throws ReflectiveOperationException {
        new TestBlockChecker(context).start();
        method.invoke(this, context);
    }

    @GameTest(structure="gametest:spell_command/cast_1")
    public void cast_1(TestContext context) { }

    @GameTest(structure="gametest:spell_command/cast_0")
    public void cast_0(TestContext context) { }

    @GameTest(structure="gametest:spell_command/cast_4")
    public void cast_4(TestContext context) { }

    @GameTest(structure="gametest:spell_command/clear_1")
    public void clear_1(TestContext context) { }

    @GameTest(structure="gametest:spell_command/clear_0")
    public void clear_0(TestContext context) { }

    @GameTest(structure="gametest:spell_command/clear_4")
    public void clear_4(TestContext context) { }
}
