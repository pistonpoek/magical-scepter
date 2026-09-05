package io.github.pistonpoek.magicalscepter.loot.function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.component.ScepterExperienceComponent;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.Optional;

/**
 * Loot function to set the experience in the scepter experience component of an item itemStack.
 */
public class SetExperienceLootFunction extends LootItemConditionalFunction {
    public static final MapCodec<SetExperienceLootFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance)
                    .and(
                            instance.group(
                                    ContextIntProviders.CODEC.fieldOf("count")
                                            .forGetter(function -> function.count),
                                    Codec.BOOL.fieldOf("add").orElse(false)
                                            .forGetter(function -> function.add)
                            )
                    )
                    .apply(instance, SetExperienceLootFunction::new)
    );
    private final Holder<ContextIntProvider> count;
    private final boolean add;

    /**
     * Construct a set experience loot function.
     *
     * @param condition Condition to create the loot function with.
     * @param count     Loot number provider to create loot function with.
     * @param add       Truth assignment, if number should be added to existing data value.
     */
    private SetExperienceLootFunction(Optional<Holder<LootItemCondition>> condition, Holder<ContextIntProvider> count, boolean add) {
        super(condition);
        this.count = count;
        this.add = add;
    }

    @Override
    public MapCodec<SetExperienceLootFunction> codec() {
        return ModLootFunctionTypes.SET_EXPERIENCE;
    }

    @Override
    public ItemStack run(ItemStack itemStack, LootContext context) {
        int experience = count.value().getInt(context);
        if (add) {
            experience += ScepterExperienceComponent.getExperience(itemStack);
        }
        ScepterExperienceComponent.set(itemStack, experience);
        return itemStack;
    }

    /**
     * Create a set experience loot function with specified loot number provider.
     *
     * @param count Loot number provider to create loot function with.
     * @return Set experience loot function with the number provider.
     */
    public static LootItemConditionalFunction.Builder<?> builder(Holder<ContextIntProvider> count) {
        return simpleBuilder(list -> new SetExperienceLootFunction(list, count, false));
    }

    /**
     * Create a set experience loot function with specified loot number provider.
     *
     * @param count Loot number provider to create loot function with.
     * @param add   Truth assignment, if number should be added to existing data value.
     * @return Set experience loot function with the number provider.
     */
    public static LootItemConditionalFunction.Builder<?> builder(Holder<ContextIntProvider> count, boolean add) {
        return simpleBuilder(list -> new SetExperienceLootFunction(list, count, add));
    }
}