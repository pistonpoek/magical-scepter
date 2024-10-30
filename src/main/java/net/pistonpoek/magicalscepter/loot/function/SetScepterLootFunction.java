package net.pistonpoek.magicalscepter.loot.function;


import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.entry.RegistryEntry;
import net.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import net.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import net.pistonpoek.magicalscepter.scepter.Scepter;

import java.util.List;

public class SetScepterLootFunction extends ConditionalLootFunction {
    public static final MapCodec<SetScepterLootFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> addConditionsField(instance).and(Scepter.ENTRY_CODEC.fieldOf("id")
                    .forGetter(function -> function.scepter)).apply(instance, SetScepterLootFunction::new)
    );
    private final RegistryEntry<Scepter> scepter;

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

    public static ConditionalLootFunction.Builder<?> builder(RegistryEntry<Scepter> scepter) {
        return builder(conditions -> new SetScepterLootFunction(conditions, scepter));
    }
}