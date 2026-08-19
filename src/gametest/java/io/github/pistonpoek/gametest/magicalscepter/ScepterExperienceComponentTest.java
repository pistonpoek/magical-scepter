package io.github.pistonpoek.gametest.magicalscepter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.pistonpoek.magicalscepter.component.ScepterExperienceComponent;
import io.github.pistonpoek.magicalscepter.item.ArcaneScepterItem;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import java.lang.reflect.Method;

import static io.github.pistonpoek.gametest.magicalscepter.util.ContextUtil.getRegistryOps;

public class ScepterExperienceComponentTest implements CustomTestMethodInvoker {
    private static final Gson GSON = new GsonBuilder().create();
    private static final ScepterExperienceComponent DEFAULT_COMPONENT = ScepterExperienceComponent.DEFAULT;
    private static final String DEFAULT = "0";
    private static final ScepterExperienceComponent CHARGED_COMPONENT =
            new ScepterExperienceComponent(ArcaneScepterItem.EXPERIENCE_STEP);
    private static final String CHARGED = String.valueOf(ArcaneScepterItem.EXPERIENCE_STEP);

    @Override
    public void invokeTestMethod(GameTestHelper context, Method method) throws ReflectiveOperationException {
        method.invoke(this, context);
    }

    @GameTest(structure = "gametest:template/empty")
    void codecFromJson(GameTestHelper context) {
        checkFromJson(context, DEFAULT_COMPONENT, DEFAULT);
        checkFromJson(context, CHARGED_COMPONENT, CHARGED);
        context.succeed();
    }

    @GameTest(structure = "gametest:template/empty")
    void codecToJson(GameTestHelper context) {
        checkToJson(context, DEFAULT, DEFAULT_COMPONENT);
        checkToJson(context, CHARGED, CHARGED_COMPONENT);
        context.succeed();
    }

    private void checkFromJson(GameTestHelper context, ScepterExperienceComponent expected, String json) {
        ScepterExperienceComponent component =
                ScepterExperienceComponent.CODEC.decode(getRegistryOps(context, JsonOps.INSTANCE),
                        GSON.fromJson(json, JsonElement.class)).getOrThrow().getFirst();
        context.assertValueEqual(expected.experience(), component.experience(),
                Component.nullToEmpty("experience property"));
    }

    private void checkToJson(GameTestHelper context, String expected, ScepterExperienceComponent component) {
        JsonElement json = ScepterExperienceComponent.CODEC.encodeStart(
                getRegistryOps(context, JsonOps.INSTANCE), component).getOrThrow();
        context.assertValueEqual(expected, GSON.toJson(json),
                Component.nullToEmpty("scepter experience component json string"));
    }
}