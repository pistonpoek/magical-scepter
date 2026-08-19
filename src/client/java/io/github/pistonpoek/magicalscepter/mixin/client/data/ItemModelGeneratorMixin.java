package io.github.pistonpoek.magicalscepter.mixin.client.data;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.BiConsumer;

@Environment(EnvType.CLIENT)
@Mixin(ItemModelGenerators.class)
public interface ItemModelGeneratorMixin {
    @Accessor
    ItemModelOutput getItemModelOutput();

    @Accessor
    BiConsumer<Identifier, ModelInstance> getModelOutput();
}
