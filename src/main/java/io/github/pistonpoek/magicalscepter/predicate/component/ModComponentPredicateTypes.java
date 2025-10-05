package io.github.pistonpoek.magicalscepter.predicate.component;

import com.mojang.serialization.Codec;
import io.github.pistonpoek.magicalscepter.predicate.item.ScepterContentsPredicate;
import io.github.pistonpoek.magicalscepter.predicate.item.ScepterExperiencePredicate;
import net.minecraft.predicate.component.ComponentPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.predicate.component.ComponentPredicateTypes
 */
public class ModComponentPredicateTypes {
    public static final ComponentPredicate.Type<ScepterExperiencePredicate> SCEPTER_EXPERIENCE =
            register("scepter_experience", ScepterExperiencePredicate.CODEC);
    public static final ComponentPredicate.Type<ScepterContentsPredicate> SCEPTER_CONTENTS =
            register("scepter_contents", ScepterContentsPredicate.CODEC);

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {

    }

    /**
     * Register a mod data component type for a specified identifier.
     *
     * @param identifier String identifier to register component predicate type for.
     * @param codec      Codec to register as component predicate reference.
     * @param <T>        Type of the component predicate type to register.
     */
    private static <T extends ComponentPredicate> ComponentPredicate.Type<T> register(String identifier, Codec<T> codec) {
        return Registry.register(Registries.DATA_COMPONENT_PREDICATE_TYPE,
                identifier, new ComponentPredicate.Type<>(codec));
    }
}
