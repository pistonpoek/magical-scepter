package io.github.pistonpoek.magicalscepter.loot.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.List;

/**
 * Loot function to set a scepter in the scepter contents component of an item stack.
 */
public class SetScepterLootFunction extends ConditionalLootFunction {
    public static final MapCodec<SetScepterLootFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> addConditionsField(instance).and(Scepter.ENTRY_CODEC.fieldOf("id")
                    .forGetter(function -> function.scepter)).apply(instance, SetScepterLootFunction::new)
    );
    private final RegistryEntry<Scepter> scepter;

    /**
     * Construct a set scepter loot function.
     *
     * @param conditions List of conditions to create the loot function with.
     * @param scepter    Registry entry of the scepter to set.
     */
    private SetScepterLootFunction(List<LootCondition> conditions, RegistryEntry<Scepter> scepter) {
        super(conditions);
        this.scepter = scepter;
    }

    @Override
    public LootFunctionType<SetScepterLootFunction> getType() {
        return ModLootFunctionTypes.SET_SCEPTER;
    }

    @Override
    public ItemStack process(ItemStack stack, LootContext context) {
        stack.apply(ModDataComponentTypes.SCEPTER_CONTENTS, ScepterContentsComponent.DEFAULT,
                this.scepter, ScepterContentsComponent::with);
        return stack;
    }

    /**
     * Create a set scepter loot function with specified scepter registry entry.
     *
     * @param registries TODO
     * @param key Registry key of the scepter to set with the loot function.
     * @return Set scepter loot function with the specified scepter.
     */
    public static ConditionalLootFunction.Builder<?> builder(RegistryWrapper.WrapperLookup  registries,
                                                             RegistryKey<Scepter> key) {
        RegistryWrapper.Impl<Scepter> impl = registries.getOrThrow(ModRegistryKeys.SCEPTER);
        RegistryEntry<Scepter> scepter = impl.getOrThrow(key);
        return builder(conditions -> new SetScepterLootFunction(conditions, scepter));
    }
}