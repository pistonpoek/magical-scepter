package io.github.pistonpoek.magicalscepter.loot.function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.component.ScepterExperienceComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProviderTypes;

import java.util.List;

/**
 * Loot function to set the experience in the scepter experience component of an item itemStack.
 */
public class SetExperienceLootFunction extends ConditionalLootFunction {
    public static final MapCodec<SetExperienceLootFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> addConditionsField(instance)
                    .and(
                            instance.group(
                                    LootNumberProviderTypes.CODEC.fieldOf("count")
                                            .forGetter(function -> function.count),
                                    Codec.BOOL.fieldOf("add").orElse(false)
                                            .forGetter(function -> function.add)
                            )
                    )
                    .apply(instance, SetExperienceLootFunction::new)
    );
    private final LootNumberProvider count;
    private final boolean add;

    /**
     * Construct a set experience loot function.
     *
     * @param conditions List of conditions to create the loot function with.
     * @param count      Loot number provider to create loot function with.
     * @param add        Truth assignment, if number should be added to existing data value.
     */
    private SetExperienceLootFunction(List<LootCondition> conditions, LootNumberProvider count, boolean add) {
        super(conditions);
        this.count = count;
        this.add = add;
    }

    @Override
    public LootFunctionType<SetExperienceLootFunction> getType() {
        return ModLootFunctionTypes.SET_EXPERIENCE;
    }

    @Override
    public ItemStack process(ItemStack itemStack, LootContext context) {
        int experience = count.nextInt(context);
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
    public static ConditionalLootFunction.Builder<?> builder(LootNumberProvider count) {
        return builder(list -> new SetExperienceLootFunction(list, count, false));
    }

    /**
     * Create a set experience loot function with specified loot number provider.
     *
     * @param count Loot number provider to create loot function with.
     * @param add   Truth assignment, if number should be added to existing data value.
     * @return Set experience loot function with the number provider.
     */
    public static ConditionalLootFunction.Builder<?> builder(LootNumberProvider count, boolean add) {
        return builder(list -> new SetExperienceLootFunction(list, count, add));
    }
}