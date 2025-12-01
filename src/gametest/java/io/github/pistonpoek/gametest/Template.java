package io.github.pistonpoek.gametest;

import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.test.TestContext;

import java.lang.reflect.Method;

public class Template implements CustomTestMethodInvoker {
    @Override
    public void invokeTestMethod(TestContext context, Method method) throws ReflectiveOperationException {
        method.invoke(this, context);
    }

    @GameTest(structure="gametest:template/empty")
    public void empty(TestContext context) {
        new TestBlockChecker(context).start();
        context.complete();
    }
}
