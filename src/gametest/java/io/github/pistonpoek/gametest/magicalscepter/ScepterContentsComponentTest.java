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
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;

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
    private static final String FILLED = "{\"infusable\":true,\"custom_attack_spell\":\"magicalscepter:wither_skull\",\"custom_protect_spell\":\"magicalscepter:guardian_haste\",\"scepter\":\"magicalscepter:shulker\",\"custom_color\":123456,\"custom_experience_cost\":10}";
    private static final ScepterContentsComponentFromContext OPPOSITE_COMPONENT = (context) -> new ScepterContentsComponent(
            Optional.of(getScepter(context, Scepters.MAGICAL_KEY)),
            Optional.of(0),
            Optional.of(0),
            Optional.of(false),
            Optional.of(getSpell(context, Spells.WARDEN_SONIC_BOOM_KEY)),
            Optional.of(getSpell(context, Spells.DRAGON_FIREBALL_KEY))
    );
    private static final String OPPOSITE = "{\"infusable\":false,\"custom_attack_spell\":\"magicalscepter:warden_sonic_boom\",\"custom_protect_spell\":\"magicalscepter:dragon_fireball\",\"scepter\":\"magicalscepter:magical\",\"custom_color\":0,\"custom_experience_cost\":0}";

    @Override
    public void invokeTestMethod(TestContext context, Method method) throws ReflectiveOperationException {
        method.invoke(this, context);
    }

    @GameTest(structure = "gametest:template/empty")
    void codecFromJson(TestContext context) {
        checkFromJson(context, DEFAULT_COMPONENT, DEFAULT);
        checkFromJson(context, FILLED_COMPONENT, FILLED);
        checkFromJson(context, OPPOSITE_COMPONENT, OPPOSITE);
        context.complete();
    }

    @GameTest(structure = "gametest:template/empty")
    void codecToJson(TestContext context) {
        checkToJson(context, DEFAULT, DEFAULT_COMPONENT);
        checkToJson(context, FILLED, FILLED_COMPONENT);
        checkToJson(context, OPPOSITE, OPPOSITE_COMPONENT);
        context.complete();
    }

    private void checkFromJson(TestContext context, ScepterContentsComponentFromContext expected, String json) {
        checkFromJson(context, expected.getComponentFromContext(context), json);
    }

    private void checkFromJson(TestContext context, ScepterContentsComponent expected, String json) {
        ScepterContentsComponent component =
                ScepterContentsComponent.CODEC.decode(getRegistryOps(context, JsonOps.INSTANCE),
                        GSON.fromJson(json, JsonElement.class)).getOrThrow().getFirst();
        assertEqualsScepterContentsComponent(context, expected, component);
    }

    private void assertEqualsScepterContentsComponent(TestContext context,
                                                      ScepterContentsComponent expected,
                                                      ScepterContentsComponent actual) {
        context.assertEquals(expected.scepter(), actual.scepter(),
                Text.of("scepter property"));
        context.assertEquals(expected.customColor(), actual.customColor(),
                Text.of("custom color property"));
        context.assertEquals(expected.customExperienceCost(), actual.customExperienceCost(),
                Text.of("custom experience cost property"));
        context.assertEquals(expected.infusable(), actual.infusable(),
                Text.of("infusable property"));
        context.assertEquals(expected.customAttackSpell(), actual.customAttackSpell(),
                Text.of("custom attack spell property"));
        context.assertEquals(expected.customProtectSpell(), actual.customProtectSpell(),
                Text.of("custom protect spell property"));
    }

    private void checkToJson(TestContext context, String expected, ScepterContentsComponentFromContext component) {
        checkToJson(context, expected, component.getComponentFromContext(context));
    }

    private void checkToJson(TestContext context, String expected, ScepterContentsComponent component) {
        JsonElement json = ScepterContentsComponent.CODEC.encodeStart(
                getRegistryOps(context, JsonOps.INSTANCE), component).getOrThrow();
        context.assertEquals(expected, GSON.toJson(json),
                Text.of("scepter contents component json string"));
    }

    @FunctionalInterface
    private interface ScepterContentsComponentFromContext {
        ScepterContentsComponent getComponentFromContext(TestContext context);
    }

}
