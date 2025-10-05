package io.github.pistonpoek.magicalscepter.mixin.client.data;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.ItemModelOutput;
import net.minecraft.client.data.ModelSupplier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.BiConsumer;

@Environment(EnvType.CLIENT)
@Mixin(ItemModelGenerator.class)
public interface ItemModelGeneratorMixin {
    @Accessor
    ItemModelOutput getOutput();

    @Accessor
    BiConsumer<Identifier, ModelSupplier> getModelCollector();
}
