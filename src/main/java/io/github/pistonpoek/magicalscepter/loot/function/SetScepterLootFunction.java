package io.github.pistonpoek.magicalscepter.loot.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

/**
 * Loot function to set a scepter in the scepter contents component of an item stack.
 */
public class SetScepterLootFunction extends LootItemConditionalFunction {
    public static final MapCodec<SetScepterLootFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance).and(Scepter.ENTRY_CODEC.fieldOf("id")
                    .forGetter(function -> function.scepter)).apply(instance, SetScepterLootFunction::new)
    );
    private final Holder<Scepter> scepter;

    /**
     * Construct a set scepter loot function.
     *
     * @param conditions List of conditions to create the loot function with.
     * @param scepter    Registry entry of the scepter to set.
     */
    private SetScepterLootFunction(List<LootItemCondition> conditions, Holder<Scepter> scepter) {
        super(conditions);
        this.scepter = scepter;
    }

    @Override
    public MapCodec<SetScepterLootFunction> codec() {
        return ModLootFunctionTypes.SET_SCEPTER;
    }

    @Override
    public ItemStack run(ItemStack stack, LootContext context) {
        stack.update(ModDataComponentTypes.SCEPTER_CONTENTS, ScepterContentsComponent.DEFAULT,
                this.scepter, ScepterContentsComponent::with);
        return stack;
    }

    /**
     * Create a set scepter loot function with specified scepter registry entry.
     *
     * @param key Registry key of the scepter to set with the loot function.
     * @return Set scepter loot function with the specified scepter.
     */
    public static LootItemConditionalFunction.Builder<?> builder(HolderLookup.Provider  registries,
                                                             ResourceKey<Scepter> key) {
        HolderLookup.RegistryLookup<Scepter> impl = registries.lookupOrThrow(ModRegistryKeys.SCEPTER);
        Holder<Scepter> scepter = impl.getOrThrow(key);
        return simpleBuilder(conditions -> new SetScepterLootFunction(conditions, scepter));
    }
}