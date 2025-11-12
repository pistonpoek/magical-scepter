package io.github.pistonpoek.gametest.magicalscepter;

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.minecraft.test.TestContext;

import java.lang.reflect.Method;

public class MagicalScepterClientGameTest implements CustomTestMethodInvoker, FabricClientGameTest {
    @Override
    public void invokeTestMethod(TestContext context, Method method) throws ReflectiveOperationException {

    }

    @Override
    public void runTest(ClientGameTestContext context) {
    }
}
