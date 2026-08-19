package io.github.pistonpoek.gametest.magicalscepter;

import io.github.pistonpoek.gametest.TestBlockChecker;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import java.lang.reflect.Method;

public class SpellCommandTest implements CustomTestMethodInvoker {
    @Override
    public void invokeTestMethod(GameTestHelper context, Method method) throws ReflectiveOperationException {
        new TestBlockChecker(context).start();
        method.invoke(this, context);
    }

    @GameTest(structure="gametest:spell_command/cast_1")
    public void cast_1(GameTestHelper context) { }

    @GameTest(structure="gametest:spell_command/cast_0")
    public void cast_0(GameTestHelper context) { }

    @GameTest(structure="gametest:spell_command/cast_4")
    public void cast_4(GameTestHelper context) { }

    @GameTest(structure="gametest:spell_command/clear_1")
    public void clear_1(GameTestHelper context) { }

    @GameTest(structure="gametest:spell_command/clear_0")
    public void clear_0(GameTestHelper context) { }

    @GameTest(structure="gametest:spell_command/clear_4")
    public void clear_4(GameTestHelper context) { }
}
