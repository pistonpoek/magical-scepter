package io.github.pistonpoek.gametest.magicalscepter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import io.github.pistonpoek.magicalscepter.spell.Spells;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import java.lang.reflect.Method;
import java.util.Optional;

import static io.github.pistonpoek.gametest.magicalscepter.util.ContextUtil.*;

public class ScepterContentsComponentTest implements CustomTestMethodInvoker {
    private static final Gson GSON = new GsonBuilder().create();
    private static final ScepterContentsComponent DEFAULT_COMPONENT = ScepterContentsComponent.DEFAULT;
    private static final String DEFAULT = "{}";
    private static final ScepterContentsComponentFromContext FILLED_COMPONENT = (context) -> new ScepterContentsComponent(
            Optional.of(getScepter(context, Scepters.SHULKER_KEY)),
            Optional.of(123456),
            Optional.of(10),
            Optional.of(true),
            Optional.of(getSpell(context, Spells.WITHER_SKULL_KEY)),
            Optional.of(getSpell(context, Spells.GUARDIAN_HASTE_KEY))
    );
    private static final String FILLED = "{\"scepter\":\"magicalscepter:shulker\",\"custom_color\":123456,\"custom_experience_cost\":10,\"infusable\":true,\"custom_attack_spell\":\"magicalscepter:wither_skull\",\"custom_protect_spell\":\"magicalscepter:guardian_haste\"}";
    private static final ScepterContentsComponentFromContext OPPOSITE_COMPONENT = (context) -> new ScepterContentsComponent(
            Optional.of(getScepter(context, Scepters.MAGICAL_KEY)),
            Optional.of(0),
            Optional.of(0),
            Optional.of(false),
            Optional.of(getSpell(context, Spells.WARDEN_SONIC_BOOM_KEY)),
            Optional.of(getSpell(context, Spells.DRAGON_FIREBALL_KEY))
    );
    private static final String OPPOSITE = "{\"scepter\":\"magicalscepter:magical\",\"custom_color\":0,\"custom_experience_cost\":0,\"infusable\":false,\"custom_attack_spell\":\"magicalscepter:warden_sonic_boom\",\"custom_protect_spell\":\"magicalscepter:dragon_fireball\"}";

    @Override
    public void invokeTestMethod(GameTestHelper context, Method method) throws ReflectiveOperationException {
        method.invoke(this, context);
    }

    @GameTest(structure = "gametest:template/empty")
    void codecFromJson(GameTestHelper context) {
        checkFromJson(context, DEFAULT_COMPONENT, DEFAULT);
        checkFromJson(context, FILLED_COMPONENT, FILLED);
        checkFromJson(context, OPPOSITE_COMPONENT, OPPOSITE);
        context.succeed();
    }

    @GameTest(structure = "gametest:template/empty")
    void codecToJson(GameTestHelper context) {
        checkToJson(context, DEFAULT, DEFAULT_COMPONENT);
        checkToJson(context, FILLED, FILLED_COMPONENT);
        checkToJson(context, OPPOSITE, OPPOSITE_COMPONENT);
        context.succeed();
    }

    private void checkFromJson(GameTestHelper context, ScepterContentsComponentFromContext expected, String json) {
        checkFromJson(context, expected.getComponentFromContext(context), json);
    }

    private void checkFromJson(GameTestHelper context, ScepterContentsComponent expected, String json) {
        ScepterContentsComponent component =
                ScepterContentsComponent.CODEC.decode(getRegistryOps(context, JsonOps.INSTANCE),
                        GSON.fromJson(json, JsonElement.class)).getOrThrow().getFirst();
        assertEqualsScepterContentsComponent(context, expected, component);
    }

    private void assertEqualsScepterContentsComponent(GameTestHelper context,
                                                      ScepterContentsComponent expected,
                                                      ScepterContentsComponent actual) {
        context.assertValueEqual(expected.scepter(), actual.scepter(),
                Component.nullToEmpty("scepter property"));
        context.assertValueEqual(expected.customColor(), actual.customColor(),
                Component.nullToEmpty("custom color property"));
        context.assertValueEqual(expected.customExperienceCost(), actual.customExperienceCost(),
                Component.nullToEmpty("custom experience cost property"));
        context.assertValueEqual(expected.infusable(), actual.infusable(),
                Component.nullToEmpty("infusable property"));
        context.assertValueEqual(expected.customAttackSpell(), actual.customAttackSpell(),
                Component.nullToEmpty("custom attack spell property"));
        context.assertValueEqual(expected.customProtectSpell(), actual.customProtectSpell(),
                Component.nullToEmpty("custom protect spell property"));
    }

    private void checkToJson(GameTestHelper context, String expected, ScepterContentsComponentFromContext component) {
        checkToJson(context, expected, component.getComponentFromContext(context));
    }

    private void checkToJson(GameTestHelper context, String expected, ScepterContentsComponent component) {
        JsonElement json = ScepterContentsComponent.CODEC.encodeStart(
                getRegistryOps(context, JsonOps.INSTANCE), component).getOrThrow();
        context.assertValueEqual(expected, GSON.toJson(json),
                Component.nullToEmpty("scepter contents component json string"));
    }

    @FunctionalInterface
    private interface ScepterContentsComponentFromContext {
        ScepterContentsComponent getComponentFromContext(GameTestHelper context);
    }

}
